package com.mock.example.modules.entrance.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.interfaces.body.entrance.aspiration.AspirationBody;
import com.mock.example.interfaces.body.entrance.aspiration.AspirationFormBody;
import com.mock.example.interfaces.vo.entrance.aspiration.AspirationFormVo;
import com.mock.example.interfaces.vo.entrance.aspiration.AspirationSelectVo;
import com.mock.example.modules.entrance.entity.model.*;
import com.mock.example.modules.entrance.repository.*;
import com.mock.example.modules.entrance.mapper.CeStudentMapper; // 直接使用 Mapper
import com.mock.example.modules.entrance.model.vo.VolunteerExportVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CeAspirationService {

    private final ICeCollegeRepo collegeRepo;
    private final ICeProfessionRepo professionRepo;
    private final ICeAspirationRepo aspirationRepo;
    private final ICeAspirationDetailRepo aspirationDetailRepo;
    private final CeStudentMapper studentMapper; // 改用 Mapper 规避 Service 循环依赖

    /**
     * 查询导出专用的志愿表数据
     */
    public List<VolunteerExportVo> selectVolunteerExportList(Integer sheetNo) {
        Long userId = SecurityUtil.getUserId();
        if (userId == null) return new ArrayList<>();
        String userTag = userId + "_" + sheetNo;
        
        List<CeAspirationDetail> details = aspirationDetailRepo.selectAspirationDetailList(userTag);
        if (details == null || details.isEmpty()) return new ArrayList<>();

        // 查询学生成绩
        Integer score = null;
        try {
            QueryWrapper<CeStudent> query = new QueryWrapper<>();
            query.eq("user_id", userId).last("limit 1");
            CeStudent student = studentMapper.selectOne(query);
            if (student != null) {
                score = student.getAchievement();
            }
        } catch (Exception e) {
            log.error("查询学生成绩异常: {}", e.getMessage());
        }

        List<VolunteerExportVo> exportList = new ArrayList<>();
        for (CeAspirationDetail d : details) {
            VolunteerExportVo vo = new VolunteerExportVo();
            vo.setSort(d.getProfessionSort());
            vo.setCollegeNo(d.getCollegeNo());
            vo.setCollegeName(d.getCollegeName());
            vo.setProfessionNo(d.getProfessionNo());
            vo.setProfessionName(d.getProfessionName());
            vo.setScore(score);
            vo.setCreateTime(d.getCreatedTime());
            exportList.add(vo);
        }
        return exportList;
    }

    /**
     * 保存志愿表
     */
    @Transactional
    public Boolean addFrom(AspirationFormBody body) {
        Long userId = SecurityUtil.getUserId();
        int sheetNo = body.getSheetNo() != null ? body.getSheetNo() : 1;
        String userTag = userId + "_" + sheetNo;

        aspirationRepo.deleteByStudentNo(userTag);
        aspirationDetailRepo.deleteByStudentNo(userTag);

        CeAspiration aspiration = new CeAspiration();
        aspiration.setStudentNo(userTag); 
        aspiration.setEntranceYear(sheetNo);
        aspiration.setCreatedUser(SecurityUtil.getUsername());
        aspirationRepo.save(aspiration);
        
        if (body.getCollegeGroups() == null || body.getCollegeGroups().isEmpty()) return true;

        List<CeAspirationDetail> details = new ArrayList<>();
        int globalSort = 1;
        for (AspirationFormBody.CollegeGroup group : body.getCollegeGroups()) {
            if (StrUtil.isBlank(group.getCollegeNo())) continue;
            CeCollege college = collegeRepo.selectCollegeByNo(group.getCollegeNo());
            if (college == null) continue;

            for (String pNo : group.getProfessionNos()) {
                if (StrUtil.isBlank(pNo)) continue;
                CeProfession prof = professionRepo.selectByProfessionNo(pNo);
                if (prof == null) continue;

                CeAspirationDetail d = new CeAspirationDetail();
                d.setStudentNo(userTag);
                d.setCollegeNo(college.getCollegeNo());
                d.setCollegeName(college.getCollegeName());
                d.setProfessionNo(prof.getProfessionNo());
                d.setProfessionName(prof.getProfessionName());
                d.setProfessionSort(globalSort++);
                d.setCreatedUser(SecurityUtil.getUsername());
                d.setAspirationBatch(1);
                details.add(d);
            }
        }

        if (!details.isEmpty()) aspirationDetailRepo.saveBatch(details);
        return Boolean.TRUE;
    }

    public List<Map<String, Object>> listAllSheets() {
        Long userId = SecurityUtil.getUserId();
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("sheetNo", i);
            String tag = userId + "_" + i;
            List<CeAspirationDetail> details = aspirationDetailRepo.selectAspirationDetailList(tag);
            if (details != null && !details.isEmpty()) {
                map.put("hasData", true);
                Map<String, List<CeAspirationDetail>> grouped = details.stream().collect(Collectors.groupingBy(CeAspirationDetail::getCollegeNo, LinkedHashMap::new, Collectors.toList()));
                List<Map<String, Object>> gList = new ArrayList<>();
                grouped.forEach((cNo, dList) -> {
                    Map<String, Object> gMap = new HashMap<>();
                    gMap.put("collegeName", dList.get(0).getCollegeName());
                    gMap.put("professions", dList.stream().map(CeAspirationDetail::getProfessionName).collect(Collectors.toList()));
                    gList.add(gMap);
                });
                map.put("details", gList);
            } else {
                map.put("hasData", false);
            }
            list.add(map);
        }
        return list;
    }

    public Map<String, Object> selectItemNew(Integer sheetNo) {
        int idx = sheetNo != null ? sheetNo : 1;
        List<CeCollege> colls = collegeRepo.list();
        List<CeProfession> profs = professionRepo.list();
        Map<String, List<CeProfession>> profMap = profs.stream().collect(Collectors.groupingBy(CeProfession::getCollegeNo));
        List<AspirationSelectVo> items = colls.stream().map(c -> {
            AspirationSelectVo vo = new AspirationSelectVo();
            vo.setLabel(c.getCollegeName()); vo.setValue(c.getCollegeNo());
            List<CeProfession> pList = profMap.getOrDefault(c.getCollegeNo(), new ArrayList<>());
            vo.setChildren(pList.stream().map(p -> {
                AspirationSelectVo child = new AspirationSelectVo();
                String extra = (p.getStudyYear() != null ? p.getStudyYear() + "年制" : "");
                child.setLabel(p.getProfessionName() + (extra.isEmpty() ? "" : " [" + extra + "]"));
                child.setValue(p.getProfessionNo());
                return child;
            }).collect(Collectors.toList()));
            return vo;
        }).collect(Collectors.toList());
        List<CeAspirationDetail> details = aspirationDetailRepo.selectAspirationDetailList(SecurityUtil.getUserId() + "_" + idx);
        Map<String, List<CeAspirationDetail>> grouped = details.stream().collect(Collectors.groupingBy(CeAspirationDetail::getCollegeNo, LinkedHashMap::new, Collectors.toList()));
        List<Map<String, Object>> groups = new ArrayList<>();
        grouped.forEach((cNo, dList) -> {
            Map<String, Object> g = new HashMap<>();
            g.put("collegeNo", cNo);
            g.put("professionNos", dList.stream().map(CeAspirationDetail::getProfessionNo).collect(Collectors.toList()));
            groups.add(g);
        });
        Map<String, Object> result = new HashMap<>();
        result.put("items", items); result.put("groups", groups);
        return result;
    }

    public AspirationFormVo selectItem(Integer sheetNo) { return new AspirationFormVo(); }

    @Transactional
    public Boolean deleteSheet(Integer sheetNo) {
        if (sheetNo == null) return false;
        String userTag = SecurityUtil.getUserId() + "_" + sheetNo;
        aspirationRepo.deleteByStudentNo(userTag);
        aspirationDetailRepo.deleteByStudentNo(userTag);
        return true;
    }

    public String aspirationDetail(String sNo) { return ""; }
    public List<CeAspiration> selectAspirationList(AspirationBody b) { return new ArrayList<>(); }
}
