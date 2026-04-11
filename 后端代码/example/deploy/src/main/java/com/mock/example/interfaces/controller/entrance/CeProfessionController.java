package com.mock.example.interfaces.controller.entrance;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.common.entity.Response;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.interfaces.body.entrance.college.CollegeBody;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.interfaces.vo.entrance.profession.ProfessionListRowVo;
import com.mock.example.modules.entrance.entity.model.CeCollege;
import com.mock.example.modules.entrance.entity.model.CeProfession;
import com.mock.example.modules.entrance.repository.ICeCollegeRepo;
import com.mock.example.modules.entrance.service.CeCollegeService;
import com.mock.example.modules.entrance.service.CeProfessionService;
import com.mock.example.modules.system.types.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/college_entrance/profession")
public class CeProfessionController extends BaseController {

    @Autowired
    private CeProfessionService professionService;

    @Autowired
    private CeCollegeService collegeService;

    @Autowired
    private ICeCollegeRepo collegeRepo;

    /**
     * 查询专业列表（按院校名称筛选，不返回院校代码、专业代码）
     */
    @GetMapping("/list")
    public TableDataInfo list(CeProfession profession) {
        String collegeNameFilter = profession.getCollegeName();
        profession.setCollegeName(null);

        List<String> collegeNosByName = null;
        if (StrUtil.isNotBlank(collegeNameFilter)) {
            CollegeBody cb = new CollegeBody();
            cb.setCollegeName(collegeNameFilter.trim());
            List<CeCollege> matched = collegeService.selectCollegeList(cb);
            collegeNosByName = matched.stream().map(CeCollege::getCollegeNo).collect(Collectors.toList());
            if (collegeNosByName.isEmpty()) {
                collegeNosByName = Collections.singletonList("__NO_MATCH__");
            }
        }

        startPage();
        QueryWrapper<CeProfession> query = new QueryWrapper<>();

        if (profession.getProfessionName() != null && !profession.getProfessionName().isEmpty()) {
            query.like("profession_name", profession.getProfessionName());
        }

        LoginUser loginUser = SecurityUtil.getLoginUser();
        if (loginUser != null && loginUser.getUser() != null) {
            boolean isSchoolAdmin = loginUser.getUser().getRoles().stream()
                    .anyMatch(r -> "school_admin".equals(r.getRoleKey()));
            if (isSchoolAdmin) {
                Long myCollegeId = loginUser.getUser().getCollegeId();
                if (myCollegeId != null) {
                    CeCollege myCollege = collegeRepo.getById(myCollegeId.intValue());
                    if (myCollege != null) {
                        if (collegeNosByName != null) {
                            if (collegeNosByName.contains(myCollege.getCollegeNo())) {
                                query.eq("college_no", myCollege.getCollegeNo());
                            } else {
                                query.eq("college_no", "__NO_MATCH__");
                            }
                        } else {
                            query.eq("college_no", myCollege.getCollegeNo());
                        }
                    } else {
                        query.eq("college_no", "NO_COLLEGE_ASSIGNED");
                    }
                } else {
                    query.eq("college_no", "NO_COLLEGE_ASSIGNED");
                }
            } else if (collegeNosByName != null) {
                query.in("college_no", collegeNosByName);
            }
        } else if (collegeNosByName != null) {
            query.in("college_no", collegeNosByName);
        }

        List<CeProfession> list = professionService.list(query);

        Set<String> nos = list.stream().map(CeProfession::getCollegeNo).filter(Objects::nonNull).collect(Collectors.toSet());
        Map<String, String> nameByNo = new HashMap<>();
        if (!nos.isEmpty()) {
            List<CeCollege> colleges = collegeRepo.list(Wrappers.<CeCollege>lambdaQuery().in(CeCollege::getCollegeNo, nos));
            for (CeCollege c : colleges) {
                nameByNo.put(c.getCollegeNo(), c.getCollegeName());
            }
        }

        List<ProfessionListRowVo> rows = list.stream().map(p -> {
            ProfessionListRowVo vo = new ProfessionListRowVo();
            vo.setId(p.getId());
            vo.setCollegeName(nameByNo.getOrDefault(p.getCollegeNo(), ""));
            vo.setProfessionName(p.getProfessionName());
            vo.setStudyYear(p.getStudyYear());
            vo.setProfessionNo(p.getProfessionNo());
            return vo;
        }).collect(Collectors.toList());

        return getDataTable(rows);
    }

    /**
     * 获取专业详情
     */
    @GetMapping(value = "/{id}")
    public Response<CeProfession> getInfo(@PathVariable("id") Integer id) {
        CeProfession p = professionService.getById(id);
        if (p != null && p.getCollegeNo() != null) {
            CeCollege c = collegeRepo.selectCollegeByNo(p.getCollegeNo());
            if (c != null) {
                p.setCollegeId(c.getId());
            }
        }
        return new Response<>(p);
    }

    /**
     * 新增专业
     */
    @PostMapping
    public Response<Boolean> add(@RequestBody CeProfession profession) {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            return new Response<>().failMsg("未登录");
        }
        boolean isSchoolAdmin = loginUser.getUser().getRoles().stream()
                .anyMatch(r -> "school_admin".equals(r.getRoleKey()));

        if (profession.getCollegeId() != null) {
            CeCollege c = collegeRepo.getById(profession.getCollegeId());
            if (c != null) {
                profession.setCollegeNo(c.getCollegeNo());
            }
        }

        if (isSchoolAdmin) {
            Long myCollegeId = loginUser.getUser().getCollegeId();
            if (myCollegeId != null) {
                CeCollege college = collegeService.getById(myCollegeId.intValue());
                if (college != null) {
                    profession.setCollegeNo(college.getCollegeNo());
                }
            }
        }
        if (StrUtil.isBlank(profession.getCollegeNo())) {
            return new Response<>().failMsg("请选择所属院校");
        }
        if (StrUtil.isBlank(profession.getProfessionNo())) {
            profession.setProfessionNo(generateUniqueProfessionNo());
        }
        profession.setCollegeId(null);
        profession.setCreatedUser(SecurityUtil.getUsername());
        return new Response<>(professionService.save(profession));
    }

    /**
     * 修改专业
     */
    @PutMapping
    public Response<Boolean> edit(@RequestBody CeProfession profession) {
        if (profession.getCollegeId() != null) {
            CeCollege c = collegeRepo.getById(profession.getCollegeId());
            if (c != null) {
                profession.setCollegeNo(c.getCollegeNo());
            }
        } else if (profession.getId() != null) {
            CeProfession old = professionService.getById(profession.getId());
            if (old != null) {
                profession.setCollegeNo(old.getCollegeNo());
            }
        }
        if (profession.getId() != null && StrUtil.isBlank(profession.getProfessionNo())) {
            CeProfession old = professionService.getById(profession.getId());
            if (old != null) {
                profession.setProfessionNo(old.getProfessionNo());
            }
        }
        profession.setCollegeId(null);

        LoginUser loginUser = SecurityUtil.getLoginUser();
        if (loginUser != null && loginUser.getUser() != null) {
            boolean isSchoolAdmin = loginUser.getUser().getRoles().stream()
                    .anyMatch(r -> "school_admin".equals(r.getRoleKey()));
            if (isSchoolAdmin) {
                Long myCollegeId = loginUser.getUser().getCollegeId();
                if (myCollegeId == null) {
                    return new Response<>().failMsg("缺少所属院校管理权限");
                }
                CeCollege myCollege = collegeService.getById(myCollegeId.intValue());
                if (myCollege == null || !myCollege.getCollegeNo().equals(profession.getCollegeNo())) {
                    return new Response<>().failMsg("禁止越权修改其他院校的专业");
                }
            }
        }
        profession.setUpdatedUser(SecurityUtil.getUsername());
        return new Response<>(professionService.updateById(profession));
    }

    /**
     * 删除专业
     */
    @DeleteMapping("/{ids}")
    public Response<Boolean> remove(@PathVariable Integer[] ids) {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        boolean isSchoolAdmin = false;
        String myCollegeNo = null;
        if (loginUser != null && loginUser.getUser() != null) {
            isSchoolAdmin = loginUser.getUser().getRoles().stream()
                    .anyMatch(r -> "school_admin".equals(r.getRoleKey()));
            if (isSchoolAdmin) {
                Long myCollegeId = loginUser.getUser().getCollegeId();
                if (myCollegeId == null) {
                    return new Response<>().failMsg("缺少所属院校管理权限");
                }
                CeCollege myCollege = collegeService.getById(myCollegeId.intValue());
                if (myCollege != null) {
                    myCollegeNo = myCollege.getCollegeNo();
                }
            }
        }

        List<Integer> validIds = new ArrayList<>();
        for (Integer id : ids) {
            if (isSchoolAdmin && myCollegeNo != null) {
                CeProfession item = professionService.getById(id);
                if (item == null || !myCollegeNo.equals(item.getCollegeNo())) {
                    continue;
                }
            }
            validIds.add(id);
        }
        if (!validIds.isEmpty()) {
            professionService.removeByIds(validIds);
        }
        return new Response<>(Boolean.TRUE);
    }

    private String generateUniqueProfessionNo() {
        String candidate;
        do {
            candidate = "P" + System.nanoTime();
        } while (professionService.count(Wrappers.<CeProfession>lambdaQuery().eq(CeProfession::getProfessionNo, candidate)) > 0);
        return candidate;
    }
}
