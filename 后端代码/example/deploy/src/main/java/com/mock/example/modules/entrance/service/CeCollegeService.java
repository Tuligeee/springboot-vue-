package com.mock.example.modules.entrance.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mock.example.common.entity.Response;
import com.mock.example.common.utils.EntityCopyUtil;
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
import com.mock.example.modules.system.types.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * 导入数据核心逻辑
     */
    @Transactional
    public String importCollegeData(List<CollegeImportVo> collegeList, boolean updateSupport) {
        if (collegeList == null || collegeList.isEmpty()) {
            return "导入院校数据不能为空！";
        }
        
        int successCount = 0;
        int failureCount = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        String operName = SecurityUtil.getUsername();

        // 1. 启发式检查：增加过滤 null 元素的鲁棒性
        boolean hasValidData = collegeList.stream()
            .filter(java.util.Objects::nonNull)
            .anyMatch(item -> StrUtil.isNotBlank(item.getCollegeName()));
            
        if (!hasValidData) {
            return "导入失败：未在文件中找到包含“学校”列的有效数据。请确保至少有一行填写了学校名称。";
        }
        
        java.util.Map<String, String> collegeNameToNo = new java.util.HashMap<>();
        java.util.Map<String, String> professionNameToNo = new java.util.HashMap<>();
        
        for (int i = 0; i < collegeList.size(); i++) {
            CollegeImportVo data = collegeList.get(i);
            if (data == null) continue; // 过滤空行
            
            int rowNum = i + 2; 
            log.info("--- 正在处理第 {} 行数据: 院校={}, 专业={} ---", rowNum, data.getCollegeName(), data.getProfessionName());
            try {
                if (StrUtil.isBlank(data.getCollegeName())) {
                    continue; 
                }
                
                // 1. 处理院校表数据 (相同院校名只处理/更新一次)
                String colNo = collegeNameToNo.get(data.getCollegeName());
                if (colNo == null) {
                    QueryWrapper<CeCollege> cQuery = new QueryWrapper<>();
                    cQuery.eq("college_name", data.getCollegeName());
                    List<CeCollege> dbCols = collegeRepo.list(cQuery);
                    
                    CeCollege college;
                    if (dbCols != null && !dbCols.isEmpty()) {
                        college = dbCols.get(0);
                        colNo = college.getCollegeNo();
                        if (updateSupport) {
                            college.setCity(data.getCity());
                            college.setRanking(data.getRanking());
                            college.setEducationLevel(data.getEducationLevel());
                            // 如果 Excel 中没这一列，data 里的值为 null，我们补 0
                            college.setPersonCount(data.getCollegePersonCount() == null ? 0 : data.getCollegePersonCount()); 
                            college.setUpdatedUser(operName);
                            collegeRepo.updateById(college);
                        }
                    } else {
                        college = EntityCopyUtil.copyEntity(CeCollege.class, data);
                        college.setPersonCount(data.getCollegePersonCount() == null ? 0 : data.getCollegePersonCount()); 
                        colNo = "C" + System.nanoTime(); 
                         college.setCollegeNo(colNo);
                        college.setCreatedUser(operName);
                        collegeRepo.save(college);
                    }
                    collegeNameToNo.put(data.getCollegeName(), colNo);
                }
                data.setCollegeNo(colNo);
                
                // 2. 处理专业表数据
                if (StrUtil.isNotBlank(data.getProfessionName())) {
                    String profKey = colNo + "_" + data.getProfessionName();
                    String profNo = professionNameToNo.get(profKey);
                    
                    if (profNo == null) {
                        QueryWrapper<CeProfession> pQuery = new QueryWrapper<>();
                        pQuery.eq("profession_name", data.getProfessionName())
                              .eq("college_no", colNo);
                        CeProfession profession = professionMapper.selectOne(pQuery);
                        
                        if (profession == null) {
                            profNo = "P" + System.nanoTime();
                            profession = new CeProfession();
                            profession.setCollegeNo(colNo);
                            profession.setProfessionNo(profNo);
                            profession.setProfessionName(data.getProfessionName());
                            profession.setStudyYear(data.getStudyYear());
                            // 处理 null 情况，补 0
                            profession.setPersonCount(data.getProfessionPersonCount() == null ? 0 : data.getProfessionPersonCount()); 
                            profession.setSubjectRequirement(StrUtil.isBlank(data.getSubjectRequirement()) ? "不提科目要求" : data.getSubjectRequirement());
                            profession.setCreatedUser(operName);
                            professionMapper.insert(profession);
                        } else {
                            profNo = profession.getProfessionNo();
                            if (updateSupport) {
                                profession.setStudyYear(data.getStudyYear());
                                // 处理 null 情况，补 0
                                profession.setPersonCount(data.getProfessionPersonCount() == null ? 0 : data.getProfessionPersonCount()); 
                                profession.setSubjectRequirement(StrUtil.isBlank(data.getSubjectRequirement()) ? "不提科目要求" : data.getSubjectRequirement());
                                profession.setUpdatedUser(operName);
                                professionMapper.updateById(profession);
                            }
                        }
                        professionNameToNo.put(profKey, profNo);
                    }
                    data.setProfessionNo(profNo);
                }
                successCount++;
            } catch (Exception e) {
                failureCount++;
                failureMsg.append("<br/>第 ").append(rowNum).append(" 行导入异常：").append(e.getMessage());
                log.error("导入单条数据失败", e);
            }
        }
        
        successMsg.append("恭喜您，数据处理完成！共成功导入/更新 ").append(successCount).append(" 条记录。");
        if (failureCount > 0) {
            successMsg.append("<br/><br/><span style='color:red; font-weight:bold;'>以下 ").append(failureCount).append(" 条记录处理失败：</span>").append(failureMsg);
        }
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
