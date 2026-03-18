package com.mock.example.interfaces.controller.entrance;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.modules.entrance.entity.model.CeProfession;
import com.mock.example.modules.entrance.service.CeProfessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mock.example.common.entity.Response;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.modules.system.types.LoginUser;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;

@RestController
@RequestMapping("/college_entrance/profession")
public class CeProfessionController extends BaseController {

    @Autowired
    private CeProfessionService professionService;

    @Autowired
    private com.mock.example.modules.entrance.service.CeCollegeService collegeService;

    /**
     * 查询专业列表
     */
    @GetMapping("/list")
    public TableDataInfo list(CeProfession profession) {
        startPage();
        QueryWrapper<CeProfession> query = new QueryWrapper<>();

        if (profession.getCollegeNo() != null && !profession.getCollegeNo().isEmpty()) {
            query.eq("college_no", profession.getCollegeNo());
        }
        if (profession.getProfessionName() != null && !profession.getProfessionName().isEmpty()) {
            query.like("profession_name", profession.getProfessionName());
        }

        // 数据隔离
        LoginUser loginUser = SecurityUtil.getLoginUser();
        if (loginUser != null && loginUser.getUser() != null) {
            boolean isSchoolAdmin = loginUser.getUser().getRoles().stream()
                    .anyMatch(r -> "school_admin".equals(r.getRoleKey()));
            if (isSchoolAdmin) {
                Long myCollegeId = loginUser.getUser().getCollegeId();
                if (myCollegeId != null) {
                    query.inSql("college_no", "SELECT college_no FROM ce_college WHERE id = " + myCollegeId);
                } else {
                    query.eq("college_no", "NO_COLLEGE_ASSIGNED");
                }
            }
        }
        return getDataTable(professionService.list(query));
    }

    /**
     * 获取专业详情
     */
    @GetMapping(value = "/{id}")
    public Response<CeProfession> getInfo(@PathVariable("id") Integer id) {
        return new Response<>(professionService.getById(id));
    }

    /**
     * 新增专业
     */
    @PostMapping
    public Response<Boolean> add(@RequestBody CeProfession profession) {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        boolean isSchoolAdmin = loginUser.getUser().getRoles().stream()
                .anyMatch(r -> "school_admin".equals(r.getRoleKey()));

        // 学校管理员新增时，强制绑定到自己学校的代码
        if (isSchoolAdmin) {
            Long myCollegeId = loginUser.getUser().getCollegeId();
            if (myCollegeId != null) {
                com.mock.example.modules.entrance.entity.model.CeCollege college = collegeService.getById(myCollegeId.intValue());
                if (college != null) {
                    profession.setCollegeNo(college.getCollegeNo());
                    profession.setCollegeName(college.getCollegeName());
                }
            }
        }
        profession.setCreatedUser(SecurityUtil.getUsername());
        return new Response<>(professionService.save(profession));
    }

    /**
     * 修改专业
     */
    @PutMapping
    public Response<Boolean> edit(@RequestBody CeProfession profession) {
        profession.setUpdatedUser(SecurityUtil.getUsername());
        return new Response<>(professionService.updateById(profession));
    }

    /**
     * 删除专业
     */
    @DeleteMapping("/{ids}")
    public Response<Boolean> remove(@PathVariable Integer[] ids) {
        return new Response<>(professionService.removeByIds(Arrays.asList(ids)));
    }
}
