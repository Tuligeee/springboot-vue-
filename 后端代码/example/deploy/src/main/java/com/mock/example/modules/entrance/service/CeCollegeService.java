package com.mock.example.modules.entrance.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mock.example.common.entity.Response;
import com.mock.example.common.utils.EntityCopyUtil;
import com.mock.example.common.utils.ExcelUtil;
import com.mock.example.common.utils.ImportProgressContext;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.interfaces.body.entrance.college.CollegeBody;
import com.mock.example.interfaces.vo.entrance.college.CollegeVo;
import com.mock.example.modules.entrance.entity.model.CeCollege;
import com.mock.example.modules.entrance.entity.model.CeProfession;
import com.mock.example.modules.entrance.entity.model.CeScoreLine;
import com.mock.example.modules.entrance.mapper.CeProfessionMapper;
import com.mock.example.modules.entrance.mapper.CeScoreLineMapper;
import com.mock.example.modules.entrance.model.vo.CollegeImportVo;
import com.mock.example.modules.entrance.repository.ICeCollegeRepo;
import com.mock.example.modules.entrance.repository.ICeProfessionRepo;
import com.mock.example.modules.system.types.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 院校查询管理
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CeCollegeService {

    private final ICeCollegeRepo collegeRepo;
    private final ICeProfessionRepo professionRepo;
    private final CeProfessionMapper professionMapper;
    private final CeScoreLineMapper scoreLineMapper;

    /**
     * 请求院校列表
     */
    public List<CeCollege> selectCollegeList(CollegeBody collegeBody) {
        CeCollege queryEntity = EntityCopyUtil.copyEntity(CeCollege.class, collegeBody);
        try {
            LoginUser loginUser = SecurityUtil.getLoginUser();
            if (loginUser != null && !loginUser.getUserId().equals(1L)) {
                boolean isSchoolAdmin = loginUser.getUser().getRoles().stream()
                        .anyMatch(r -> "school_admin".equals(r.getRoleKey()));
                if (isSchoolAdmin) {
                    queryEntity.setManagerId(loginUser.getUserId());
                    List<CeCollege> list = collegeRepo.selectCollegeList(queryEntity);
                    if (list.isEmpty() && loginUser.getUser().getCollegeId() != null) {
                        queryEntity.setManagerId(null);
                        queryEntity.setId(loginUser.getUser().getCollegeId().intValue());
                        return collegeRepo.selectCollegeList(queryEntity);
                    }
                    return list;
                }
            }
        } catch (Exception e) {
            log.warn("获取用户信息失败: {}", e.getMessage());
        }
        return collegeRepo.selectCollegeList(queryEntity);
    }

    /**
     * 导出院校、专业、分数线联表数据
     */
    public List<CollegeImportVo> selectCollegeExportList(CollegeBody collegeBody) {
        List<CeCollege> colleges = this.selectCollegeList(collegeBody);
        List<CollegeImportVo> exportList = new ArrayList<>();
        for (CeCollege college : colleges) {
            // 使用 QueryWrapper 替换原本不存在的 selectProfessionList
            QueryWrapper<CeProfession> pQuery = new QueryWrapper<>();
            pQuery.eq("college_no", college.getCollegeNo());
            List<CeProfession> professions = professionMapper.selectList(pQuery);
            
            if (professions.isEmpty()) {
                exportList.add(EntityCopyUtil.copyEntity(CollegeImportVo.class, college));
            } else {
                for (CeProfession profession : professions) {
                        CollegeImportVo vo = EntityCopyUtil.copyEntity(CollegeImportVo.class, college);
                        vo.setCollegePersonCount(college.getPersonCount());
                        vo.setProfessionNo(profession.getProfessionNo());
                        vo.setProfessionName(profession.getProfessionName());
                        vo.setStudyYear(profession.getStudyYear());
                        vo.setSubjectRequirement(profession.getSubjectRequirement());
                        vo.setProfessionPersonCount(profession.getPersonCount());
                        exportList.add(vo);
                }
            }
        }
        return exportList;
    }

    /**
     * 异步导入处理（采用流式分批解析优化）
     */
    @Async
    @Transactional
    public void importCollegeDataAsync(String filePath, boolean updateSupport, String taskId, String operName) {
        File file = new File(filePath);
        try {
            log.info("【异步任务】开始流式解析文件: {}, 大小: {} bytes", file.getAbsolutePath(), file.length());
            ImportProgressContext.setProgress(taskId, 5);

            // 1. 预先加载所有数据到内存 (加速后续匹配)
            List<CeCollege> allColleges = collegeRepo.list();
            java.util.Map<String, CeCollege> collegeCache = allColleges.stream()
                .collect(java.util.stream.Collectors.toMap(CeCollege::getCollegeName, c -> c, (a, b) -> a));
            
            List<CeProfession> allProfessions = professionRepo.list();
            java.util.Map<String, CeProfession> professionCache = allProfessions.stream()
                .collect(java.util.stream.Collectors.toMap(p -> p.getCollegeNo() + "_" + p.getProfessionName(), p -> p, (a, b) -> a));

            ImportProgressContext.setProgress(taskId, 10);

            // 状态追踪
            int[] counts = {0, 0, 0}; // [total, success, failure]
            StringBuilder failureMsg = new StringBuilder();
            long timeBasis = System.currentTimeMillis();

            ExcelUtil<CollegeImportVo> util = new ExcelUtil<>(CollegeImportVo.class);
            
            // 2. 流式分批解析与入库 (每批以 2000 条为界)
            util.importExcelStreaming(file, batch -> {
                processImportBatch(batch, updateSupport, operName, collegeCache, professionCache, timeBasis, counts, failureMsg);
                
                // 更新进度：由于 SAX 难以提前预知总行数，这里根据处理量估算汇报（针对 9-10 万量级）
                if (taskId != null) {
                    int currentProgress = 10 + (int) (counts[0] * 85.0 / 100000); // 假定 10W 为 100%
                    ImportProgressContext.setProgress(taskId, Math.min(currentProgress, 95));
                }
            }, 2000);

            // 3. 构建结果消息
            StringBuilder successMsg = new StringBuilder();
            successMsg.append("恭喜您，数据处理完成！共成功处理 ").append(counts[1]).append(" 条记录。");
            if (counts[2] > 0) {
                successMsg.append("<br/><br/><span style='color:red; font-weight:bold;'>以下 ").append(counts[2]).append(" 条记录处理失败：</span>").append(failureMsg);
            }
            
            ImportProgressContext.setProgress(taskId, 100);
            ImportProgressContext.setResult(taskId, successMsg.toString());
            log.info("【异步任务】导入完成，总处理: {}, 成功: {}, 失败: {}", counts[0], counts[1], counts[2]);

        } catch (Exception e) {
            log.error("异步导入任务异常", e);
            ImportProgressContext.setResult(taskId, "导入过程中发生错误: " + e.getMessage());
        } finally {
            if (file.exists()) file.delete();
        }
    }

    /**
     * 处理导入批次（单次 DB 写入单位）
     */
    private void processImportBatch(List<CollegeImportVo> batch, boolean updateSupport, String operName,
                                   java.util.Map<String, CeCollege> collegeCache, 
                                   java.util.Map<String, CeProfession> professionCache,
                                   long timeBasis, int[] counts, StringBuilder failureMsg) {
        
        List<CeCollege> collegesToSave = new ArrayList<>();
        List<CeCollege> collegesToUpdate = new ArrayList<>();
        List<CeProfession> professionsToSave = new ArrayList<>();
        List<CeProfession> professionsToUpdate = new ArrayList<>();

        for (CollegeImportVo data : batch) {
            counts[0]++; // total
            if (data == null || StrUtil.isBlank(data.getCollegeName())) continue;

            try {
                // 处理院校
                CeCollege college = collegeCache.get(data.getCollegeName());
                String colNo;
                if (college != null) {
                    colNo = college.getCollegeNo();
                    if (updateSupport) {
                        updateCollegeFields(college, data, operName);
                        collegesToUpdate.add(college);
                    }
                } else {
                    college = EntityCopyUtil.copyEntity(CeCollege.class, data);
                    sanitizeCollegeData(college, data);
                    colNo = "C" + (timeBasis + counts[0]); 
                    college.setCollegeNo(colNo);
                    college.setCreatedUser(operName);
                    collegesToSave.add(college);
                    collegeCache.put(data.getCollegeName(), college);
                }

                // 处理专业
                if (StrUtil.isNotBlank(data.getProfessionName())) {
                    String profKey = colNo + "_" + data.getProfessionName();
                    CeProfession profession = professionCache.get(profKey);
                    if (profession == null) {
                        profession = createNewProfession(data, colNo, operName, timeBasis + counts[0]);
                        professionsToSave.add(profession);
                        professionCache.put(profKey, profession);
                    } else if (updateSupport) {
                        updateProfessionFields(profession, data, operName);
                        professionsToUpdate.add(profession);
                    }
                }
                counts[1]++; // success
            } catch (Exception e) {
                counts[2]++; // failure
                failureMsg.append("<br/>数据 [" + data.getCollegeName() + "] 处理异常：" + e.getMessage());
            }
        }

        // 下发批量写
        if (!collegesToSave.isEmpty()) collegeRepo.saveBatch(collegesToSave, 1000);
        if (!collegesToUpdate.isEmpty()) collegeRepo.updateBatchById(collegesToUpdate, 1000);
        if (!professionsToSave.isEmpty()) professionRepo.saveBatch(professionsToSave, 1000);
        if (!professionsToUpdate.isEmpty()) professionRepo.updateBatchById(professionsToUpdate, 1000);
    }

    private void updateCollegeFields(CeCollege college, CollegeImportVo data, String operName) {
        String level = data.getEducationLevel();
        if (level != null && level.length() > 50) level = level.substring(0, 47) + "...";
        college.setEducationLevel(level);
        college.setPersonCount(data.getCollegePersonCount() == null ? 0 : data.getCollegePersonCount()); 
        college.setUpdatedUser(operName);
    }

    private void sanitizeCollegeData(CeCollege college, CollegeImportVo data) {
        if (college.getCollegeName() != null && college.getCollegeName().length() > 100) {
            college.setCollegeName(college.getCollegeName().substring(0, 97) + "...");
        }
        if (college.getCity() != null && college.getCity().length() > 50) {
            college.setCity(college.getCity().substring(0, 47) + "...");
        }
        if (college.getEducationLevel() != null && college.getEducationLevel().length() > 50) {
            college.setEducationLevel(college.getEducationLevel().substring(0, 47) + "...");
        }
        college.setPersonCount(data.getCollegePersonCount() == null ? 0 : data.getCollegePersonCount());
    }

    private CeProfession createNewProfession(CollegeImportVo data, String colNo, String operName, long uniqueSuffix) {
        CeProfession p = new CeProfession();
        p.setCollegeNo(colNo);
        p.setProfessionNo("P" + (uniqueSuffix + 1000000)); 
        String profName = data.getProfessionName();
        if (profName != null && profName.length() > 100) profName = profName.substring(0, 97) + "...";
        p.setProfessionName(profName);
        p.setStudyYear(data.getStudyYear());
        p.setPersonCount(data.getProfessionPersonCount() == null ? 0 : data.getProfessionPersonCount()); 
        String subReq = StrUtil.isBlank(data.getSubjectRequirement()) ? "不提科目要求" : data.getSubjectRequirement();
        if (subReq.length() > 200) subReq = subReq.substring(0, 197) + "...";
        p.setSubjectRequirement(subReq);
        p.setCreatedUser(operName);
        return p;
    }

    private void updateProfessionFields(CeProfession profession, CollegeImportVo data, String operName) {
        profession.setStudyYear(data.getStudyYear());
        profession.setPersonCount(data.getProfessionPersonCount() == null ? 0 : data.getProfessionPersonCount()); 
        String subReq = StrUtil.isBlank(data.getSubjectRequirement()) ? "不提科目要求" : data.getSubjectRequirement();
        if (subReq.length() > 200) subReq = subReq.substring(0, 197) + "...";
        profession.setSubjectRequirement(subReq);
        profession.setUpdatedUser(operName);
    }

    /**
     * 导入数据核心逻辑（外部同步调用入口）
     */
    public String importCollegeData(List<CollegeImportVo> collegeList, boolean updateSupport) {
        String operName = SecurityUtil.getUsername();
        int[] counts = {0, 0, 0};
        StringBuilder failureMsg = new StringBuilder();
        
        // 同步导入依然可以沿用旧的缓存策略，但这里通过复用逻辑简化
        List<CeCollege> allColleges = collegeRepo.list();
        java.util.Map<String, CeCollege> collegeCache = allColleges.stream()
            .collect(java.util.stream.Collectors.toMap(CeCollege::getCollegeName, c -> c, (a, b) -> a));
        List<CeProfession> allProfessions = professionRepo.list();
        java.util.Map<String, CeProfession> professionCache = allProfessions.stream()
            .collect(java.util.stream.Collectors.toMap(p -> p.getCollegeNo() + "_" + p.getProfessionName(), p -> p, (a, b) -> a));

        processImportBatch(collegeList, updateSupport, operName, collegeCache, professionCache, System.currentTimeMillis(), counts, failureMsg);

        StringBuilder successMsg = new StringBuilder();
        successMsg.append("数据处理完成！共成功处理 ").append(counts[1]).append(" 条记录。");
        if (counts[2] > 0) successMsg.append("<br/>失败 ").append(counts[2]).append(" 条。");
        return successMsg.toString();
    }

    public Response<Boolean> addCollege(CollegeBody collegeBody) {
        if (StrUtil.isBlank(collegeBody.getCollegeNo())) {
            collegeBody.setCollegeNo(generateUniqueCollegeNo());
        }
        if (BooleanUtils.isFalse(uniqueCollegeNo(collegeBody.getCollegeNo(), null))) {
            return new Response<>().failMsg("保存院校失败,代码 '" + collegeBody.getCollegeNo() + "' 已存在");
        }
        CeCollege ceCollege = EntityCopyUtil.copyEntity(CeCollege.class, collegeBody);
        ceCollege.setCreatedUser(SecurityUtil.getUsername());
        collegeRepo.save(ceCollege);
        return new Response<>(Boolean.TRUE);
    }

    public Response<Boolean> editCollege(CollegeBody collegeBody) {
        if (StrUtil.isBlank(collegeBody.getCollegeNo()) && collegeBody.getId() != null) {
            CeCollege existing = collegeRepo.getById(collegeBody.getId());
            if (existing != null) {
                collegeBody.setCollegeNo(existing.getCollegeNo());
            }
        }
        LoginUser loginUser = SecurityUtil.getLoginUser();
        if (loginUser != null && !loginUser.getUserId().equals(1L)) {
            boolean isSchoolAdmin = loginUser.getUser().getRoles().stream()
                    .anyMatch(r -> "school_admin".equals(r.getRoleKey()));
            if (isSchoolAdmin) {
                Long myCollegeId = loginUser.getUser().getCollegeId();
                if (myCollegeId == null || !myCollegeId.equals(Long.valueOf(collegeBody.getId()))) {
                    return new Response<Boolean>().failMsg("禁止越权修改其他院校信息");
                }
            }
        }
        if (BooleanUtils.isFalse(uniqueCollegeNo(collegeBody.getCollegeNo(), collegeBody.getId()))) {
            return new Response<Boolean>().failMsg("编辑院校失败,代码 '" + collegeBody.getCollegeNo() + "' 已存在");
        }
        CeCollege ceCollege = EntityCopyUtil.copyEntity(CeCollege.class, collegeBody);
        ceCollege.setUpdatedUser(SecurityUtil.getUsername());
        collegeRepo.updateById(ceCollege);
        return new Response<>(Boolean.TRUE);
    }

    public CollegeVo getCollege(Integer collegeId) {
        return EntityCopyUtil.copyEntity(CollegeVo.class, collegeRepo.getById(collegeId));
    }

    /**
     * 根据ID获取院校实体
     */
    public CeCollege getById(Integer id) {
        return collegeRepo.getById(id);
    }

    public Response<Boolean> deleteCollegeByIds(Integer[] collegeIds) {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        if (loginUser != null && !loginUser.getUserId().equals(1L)) {
            boolean isSchoolAdmin = loginUser.getUser().getRoles().stream()
                    .anyMatch(r -> "school_admin".equals(r.getRoleKey()));
            if (isSchoolAdmin) {
                return new Response<Boolean>().failMsg("禁止学校用户执行院校删除操作");
            }
        }
        collegeRepo.removeByIds(Arrays.asList(collegeIds));
        return new Response<>(Boolean.TRUE);
    }

    public Response<CeCollege> getMyCollege() {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        if (loginUser == null || loginUser.getUserId().equals(1L)) return new Response<>(null);
        CeCollege query = new CeCollege();
        query.setManagerId(loginUser.getUserId());
        List<CeCollege> myColleges = collegeRepo.selectCollegeList(query);
        return (myColleges == null || myColleges.isEmpty()) ? new Response<>(null) : new Response<>(myColleges.get(0));
    }

    private Boolean uniqueCollegeNo(String collegeNo, Integer collegeId) {
        CeCollege ceCollege = collegeRepo.selectCollegeByNo(collegeNo);
        return ceCollege == null || ceCollege.getId().equals(collegeId);
    }

    private String generateUniqueCollegeNo() {
        String candidate;
        do {
            candidate = "C" + System.nanoTime();
        } while (BooleanUtils.isFalse(uniqueCollegeNo(candidate, null)));
        return candidate;
    }
}
