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
import com.mock.example.modules.entrance.mapper.CeStudentMapper;
import com.mock.example.modules.system.mapper.SysUserMapper;
import com.mock.example.modules.system.entity.model.SysUser;
import com.mock.example.modules.system.types.LoginUser;
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
    private final CeStudentMapper studentMapper;
    private final SysUserMapper userMapper;

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
     * 【核心助手】获取归一化后的学生选考科目
     */
    private Set<String> getNormalizedSubjects(Long userId) {
        Set<String> subjects = new HashSet<>();
        try {
            QueryWrapper<CeStudent> query = new QueryWrapper<>();
            query.eq("user_id", userId).last("limit 1");
            CeStudent student = studentMapper.selectOne(query);
            if (student != null) {
                // 兼容多分隔符：逗号、分号、空格、顿号
                String first = student.getSubjectFirst() == null ? "" : student.getSubjectFirst();
                String second = student.getSubjectSecond() == null ? "" : student.getSubjectSecond();
                String raw = (first + "," + second).replace("政治", "思想政治");
                
                // 移除所有引号
                raw = raw.replace("\"", "").replace("'", "").replace("“", "").replace("”", "");
                
                String[] parts = raw.split("[,，;；\\s、]+");
                for (String s : parts) {
                    if (StrUtil.isNotBlank(s)) subjects.add(s.trim());
                }
            }
        } catch (Exception e) {
            log.error("归一化学生选科异常: {}", e.getMessage());
        }
        return subjects;
    }

    /**
     * 【核心助手】判定专业要求与考生选科是否匹配
     */
    private boolean isSubjectMatch(String req, Set<String> studentSubjects) {
        if (StrUtil.isBlank(req)) return true;
        // 彻底清洗任何形式的引号和空格干扰
        String cleanReq = req.replace("\"", "").replace("'", "").replace("“", "").replace("”", "").trim();
        if (cleanReq.isEmpty() || "不提科目要求".equals(cleanReq) || "不限".equals(cleanReq)) return true;

        // 归一化专业要求（处理多种分隔符，统一政治名称）
        String[] reqs = cleanReq.replace("政治", "思想政治").split("[,，;；\\s、]+");
        for (String r : reqs) {
            if (StrUtil.isNotBlank(r)) {
                if (!studentSubjects.contains(r.trim())) return false;
            }
        }
        return true;
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
        aspiration.setEntranceYear(java.time.Year.now().getValue());
        aspiration.setCreatedUser(SecurityUtil.getUsername());
        aspirationRepo.save(aspiration);
        
        if (body.getCollegeGroups() == null || body.getCollegeGroups().isEmpty()) return true;

        // 获取归一化的学生选科并缓存字符串形式用于报错展示
        Set<String> normalizedStudentSubjects = getNormalizedSubjects(userId);
        String studentSubjectStr = normalizedStudentSubjects.isEmpty() ? "[未设置]" : normalizedStudentSubjects.toString();

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

                // 校验：如果不匹配，抛出极详细的提示，包括导致错误的具体院校和专业
                String req = prof.getSubjectRequirement();
                if (!isSubjectMatch(req, normalizedStudentSubjects)) {
                    String errorDetail = String.format("【%s - %s】（要求: %s）", college.getCollegeName(), prof.getProfessionName(), req);
                    throw new RuntimeException("填报保存失败！所选专业 " + errorDetail + " 与您的选考科目 " + studentSubjectStr + " 不符，请移除或更换该专业。");
                }

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

    private List<CeCollege> cachedColleges = null;
    private List<CeProfession> cachedProfessions = null;
    private long cacheTime = 0;

    public Map<String, Object> selectItemNew(Integer sheetNo) {
        int idx = sheetNo != null ? sheetNo : 1;
        
        // 1. 获取归一化的当前学生选科（动态，不缓存）
        Set<String> normalizedStudentSubjects = getNormalizedSubjects(SecurityUtil.getUserId());

        // 2. 获取原始数据列表（使用缓存加速）
        List<CeCollege> colls;
        List<CeProfession> profs;
        if (cachedColleges != null && cachedProfessions != null && (System.currentTimeMillis() - cacheTime < 3600000)) {
            colls = cachedColleges;
            profs = cachedProfessions;
        } else {
            colls = collegeRepo.list();
            profs = professionRepo.list();
            cachedColleges = colls;
            cachedProfessions = profs;
            cacheTime = System.currentTimeMillis();
        }

        // 3. 动态构建树状结构并追加考生专属的资格提示
        Map<String, List<CeProfession>> profMap = profs.stream().collect(Collectors.groupingBy(CeProfession::getCollegeNo));
        List<AspirationSelectVo> items = colls.stream().map(c -> {
            AspirationSelectVo vo = new AspirationSelectVo();
            vo.setLabel(c.getCollegeName()); vo.setValue(c.getCollegeNo());
            List<CeProfession> pList = profMap.getOrDefault(c.getCollegeNo(), new ArrayList<>());
            vo.setChildren(pList.stream().map(p -> {
                AspirationSelectVo child = new AspirationSelectVo();
                String extra = (p.getStudyYear() != null ? p.getStudyYear() + "年制" : "");
                
                // 核心：基于当前学生选科动态判定资格并标记状态位
                boolean isMatch = isSubjectMatch(p.getSubjectRequirement(), normalizedStudentSubjects);
                if (!isMatch) {
                    child.setInfo("incompatible");
                }
                
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

    public List<CeAspiration> selectAspirationList(AspirationBody b) {
        QueryWrapper<CeAspiration> qw = new QueryWrapper<>();
        
        // --- 核心修复：数据隔离 ---
        if (SecurityUtil.isRestrictedSchoolAdmin()) {
            LoginUser loginUser = SecurityUtil.getLoginUser();
            Long myCollegeId = loginUser.getUser().getCollegeId();
            if (myCollegeId != null) {
                // 找到我校代码
                CeCollege myCollege = collegeRepo.getById(myCollegeId.intValue());
                if (myCollege != null) {
                    // 仅允许查询在该校填报了至少一个专业的 student_no
                    qw.inSql("student_no", "SELECT student_no FROM ce_aspiration_detail WHERE college_no = '" + myCollege.getCollegeNo() + "'");
                } else {
                     return new ArrayList<>(); // 逻辑错误，学校不存在
                }
            } else {
                return new ArrayList<>(); // 没绑定学校
            }
        }

        if (StrUtil.isNotBlank(b.getStudentNo())) {
            qw.like("student_no", b.getStudentNo());
        }
        if (b.getEntranceYear() != null) {
            qw.eq("entrance_year", b.getEntranceYear());
        }
        qw.orderByDesc("created_time");
        
        List<CeAspiration> list = aspirationRepo.list(qw);
        for (CeAspiration asp : list) {
            try {
                // student_no 格式为 userId_sheetNo
                String[] parts = asp.getStudentNo().split("_");
                Long userId = Long.parseLong(parts[0]);
                QueryWrapper<CeStudent> sQw = new QueryWrapper<>();
                sQw.eq("user_id", userId).last("limit 1");
                CeStudent student = studentMapper.selectOne(sQw);
                if (student != null) {
                    asp.setStudentName(student.getStudentName());
                    asp.setDisplayStudentNo(student.getStudentNo());
                } else {
                    // 如果没找到学生档案，尝试查询系统用户账号名作为昵称
                    SysUser user = userMapper.selectById(userId);
                    if (user != null) {
                        asp.setStudentName(user.getUserName());
                        asp.setDisplayStudentNo("SYS_USER");
                    } else {
                        asp.setStudentName("未知用户");
                        asp.setDisplayStudentNo("ID:" + userId);
                    }
                }
            } catch (Exception e) {
                log.warn("解析学号姓名异常: {}", e.getMessage());
            }
        }
        return list;
    }

    public String aspirationDetail(String sNo) {
        if (StrUtil.isBlank(sNo)) return "暂无内容";
        List<CeAspirationDetail> details = aspirationDetailRepo.selectAspirationDetailList(sNo);
        if (details == null || details.isEmpty()) return "该志愿表目前为空";
        
        // --- 数据脱敏/隔离：学校管理员仅能看到报了自己学校的部分 ---
        String myCollegeNo = null;
        if (SecurityUtil.isRestrictedSchoolAdmin()) {
            LoginUser loginUser = SecurityUtil.getLoginUser();
            CeCollege myCollege = collegeRepo.getById(loginUser.getUser().getCollegeId().intValue());
            if (myCollege != null) myCollegeNo = myCollege.getCollegeNo();
        }

        StringBuilder sb = new StringBuilder("填报内容摘要：\n");
        Map<String, List<CeAspirationDetail>> grouped = details.stream()
                .collect(Collectors.groupingBy(CeAspirationDetail::getCollegeName, LinkedHashMap::new, Collectors.toList()));
        
        final int[] idx = {1};
        final String finalMyCollegeNo = myCollegeNo;
        grouped.forEach((collegeName, dList) -> {
            boolean isMySchool = finalMyCollegeNo == null || dList.get(0).getCollegeNo().equals(finalMyCollegeNo);
            if (isMySchool) {
                sb.append(idx[0]++).append(". ").append(collegeName).append(" (")
                  .append(dList.stream().map(CeAspirationDetail::getProfessionName).collect(Collectors.joining(", ")))
                  .append(")\n");
            } else {
                sb.append(idx[0]++).append(". ").append("[其他院校] (已填报但院校管理员不可见)\n");
            }
        });
        return sb.toString();
    }
    @Transactional
    public Boolean deleteById(Integer id) {
        CeAspiration aspiration = aspirationRepo.getById(id);
        if (aspiration != null) {
            aspirationDetailRepo.deleteByStudentNo(aspiration.getStudentNo());
            aspirationRepo.removeById(id);
        }
        return true;
    }
}
