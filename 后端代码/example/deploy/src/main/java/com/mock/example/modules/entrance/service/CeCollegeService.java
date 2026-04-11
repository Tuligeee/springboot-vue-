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
                    QueryWrapper<CeScoreLine> sQuery = new QueryWrapper<>();
                    sQuery.eq("college_no", college.getCollegeNo())
                          .eq("profession_no", profession.getProfessionNo());
                    List<CeScoreLine> scores = scoreLineMapper.selectList(sQuery);
                    
                    if (scores.isEmpty()) {
                        CollegeImportVo vo = EntityCopyUtil.copyEntity(CollegeImportVo.class, college);
                        vo.setProfessionNo(profession.getProfessionNo());
                        vo.setProfessionName(profession.getProfessionName());
                        vo.setStudyYear(profession.getStudyYear());
                        exportList.add(vo);
                    } else {
                        for (CeScoreLine score : scores) {
                            CollegeImportVo vo = EntityCopyUtil.copyEntity(CollegeImportVo.class, college);
                            vo.setProfessionNo(profession.getProfessionNo());
                            vo.setProfessionName(profession.getProfessionName());
                            vo.setStudyYear(profession.getStudyYear());
                            vo.setYear(score.getYear());
                            vo.setScore(score.getScore());
                            exportList.add(vo);
                        }
                    }
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
        String operName = SecurityUtil.getUsername();
        
        java.util.Set<String> processedColleges = new java.util.HashSet<>();
        java.util.Set<String> processedProfessions = new java.util.HashSet<>();
        
        for (CollegeImportVo data : collegeList) {
            try {
                // 1. 处理院校表数据 (相同 batch 只处理/更新一次)
                if (!processedColleges.contains(data.getCollegeNo())) {
                    CeCollege college = collegeRepo.selectCollegeByNo(data.getCollegeNo());
                    if (college == null) {
                        college = EntityCopyUtil.copyEntity(CeCollege.class, data);
                        college.setCreatedUser(operName);
                        collegeRepo.save(college);
                    } else if (updateSupport) {
                        college.setCollegeName(data.getCollegeName());
                        college.setCity(data.getCity());
                        college.setRanking(data.getRanking());
                        college.setUpdatedUser(operName);
                        collegeRepo.updateById(college);
                    }
                    processedColleges.add(data.getCollegeNo());
                }
                
                // 2. 处理专业表数据 (相同 batch、相同专业只处理/更新一次)
                if (data.getProfessionNo() != null && !data.getProfessionNo().isEmpty()) {
                    String profKey = data.getCollegeNo() + "_" + data.getProfessionNo();
                    if (!processedProfessions.contains(profKey)) {
                        QueryWrapper<CeProfession> pQuery = new QueryWrapper<>();
                        pQuery.eq("profession_no", data.getProfessionNo())
                              .eq("college_no", data.getCollegeNo());
                        CeProfession profession = professionMapper.selectOne(pQuery);
                        
                        if (profession == null) {
                            profession = new CeProfession();
                            profession.setCollegeNo(data.getCollegeNo());
                            profession.setProfessionNo(data.getProfessionNo());
                            profession.setProfessionName(data.getProfessionName());
                            profession.setStudyYear(data.getStudyYear());
                            profession.setCreatedUser(operName);
                            professionMapper.insert(profession);
                        } else if (updateSupport) {
                            profession.setProfessionName(data.getProfessionName());
                            profession.setStudyYear(data.getStudyYear());
                            profession.setUpdatedUser(operName);
                            professionMapper.updateById(profession);
                        }
                        processedProfessions.add(profKey);
                    }
                    
                    // 3. 处理录取分数线记录
                    if (data.getYear() != null && data.getScore() != null) {
                        QueryWrapper<CeScoreLine> sQuery = new QueryWrapper<>();
                        sQuery.eq("college_no", data.getCollegeNo())
                              .eq("profession_no", data.getProfessionNo())
                              .eq("year", data.getYear());
                        CeScoreLine scoreLine = scoreLineMapper.selectOne(sQuery);
                        
                        if (scoreLine == null) {
                            scoreLine = new CeScoreLine();
                            scoreLine.setCollegeNo(data.getCollegeNo());
                            scoreLine.setProfessionNo(data.getProfessionNo());
                            scoreLine.setYear(data.getYear());
                            scoreLine.setScore(data.getScore());
                            scoreLine.setCreatedUser(operName);
                            scoreLineMapper.insert(scoreLine);
                        } else if (updateSupport) {
                            scoreLine.setScore(data.getScore());
                            scoreLine.setUpdatedUser(operName);
                            scoreLineMapper.updateById(scoreLine);
                        }
                    }
                }
                successCount++;
            } catch (Exception e) {
                log.error("导入单条数据失败", e);
            }
        }
        return "恭喜您，数据已全部处理完成！共涉及 " + successCount + " 条记录。";
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
