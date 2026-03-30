package com.mock.example.interfaces.controller.entrance;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.common.entity.Response;
import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.modules.entrance.entity.model.CeScoreLine;
import com.mock.example.modules.entrance.mapper.CeScoreLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.modules.system.types.LoginUser;
import com.mock.example.modules.entrance.entity.model.CeCollege;
/**
 * 专业录取分数线Controller
 * 
 * @author mock
 * @date 2026-03-25
 */
@RestController
@RequestMapping("/entrance/scoreLine")
public class CeScoreLineController extends BaseController {
    
    @Autowired
    private CeScoreLineMapper ceScoreLineMapper;
    
    @Autowired
    private com.mock.example.modules.entrance.service.CeCollegeService collegeService;

    /**
     * 查询各校专业分数线列表
     */
    @GetMapping("/list")
    public TableDataInfo list(CeScoreLine ceScoreLine) {
        startPage();
        QueryWrapper<CeScoreLine> queryWrapper = new QueryWrapper<>();
        if (ceScoreLine != null) {
            if (ceScoreLine.getCollegeNo() != null && !ceScoreLine.getCollegeNo().isEmpty()) {
                queryWrapper.eq("college_no", ceScoreLine.getCollegeNo());
            }
            if (ceScoreLine.getProfessionNo() != null && !ceScoreLine.getProfessionNo().isEmpty()) {
                queryWrapper.eq("profession_no", ceScoreLine.getProfessionNo());
            }
            if (ceScoreLine.getYear() != null) {
                queryWrapper.eq("year", ceScoreLine.getYear());
            }
        }
        
        LoginUser loginUser = SecurityUtil.getLoginUser();
        if (loginUser != null && loginUser.getUser() != null) {
            boolean isSchoolAdmin = loginUser.getUser().getRoles().stream()
                    .anyMatch(r -> "school_admin".equals(r.getRoleKey()));
            if (isSchoolAdmin) {
                Long myCollegeId = loginUser.getUser().getCollegeId();
                if (myCollegeId != null) {
                    queryWrapper.inSql("college_no", "SELECT college_no FROM ce_college WHERE id = " + myCollegeId);
                } else {
                    queryWrapper.eq("college_no", "NO_COLLEGE_ASSIGNED");
                }
            }
        }
        queryWrapper.orderByDesc("year", "score");
        List<CeScoreLine> list = ceScoreLineMapper.selectList(queryWrapper);
        return getDataTable(list);
    }

    /**
     * 获取详情
     */
    @GetMapping(value = "/{id}")
    public Response getInfo(@PathVariable("id") Integer id) {
        return new Response<>(ceScoreLineMapper.selectById(id));
    }

    /**
     * 新增
     */
    @PostMapping
    public Response add(@RequestBody CeScoreLine ceScoreLine) {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        if (loginUser != null && loginUser.getUser() != null) {
            boolean isSchoolAdmin = loginUser.getUser().getRoles().stream()
                    .anyMatch(r -> "school_admin".equals(r.getRoleKey()));
            if (isSchoolAdmin) {
                Long myCollegeId = loginUser.getUser().getCollegeId();
                if (myCollegeId == null) return new Response<>().failMsg("缺少所属院校管理权限");
                CeCollege myCollege = collegeService.getById(myCollegeId.intValue());
                if (myCollege != null) {
                    ceScoreLine.setCollegeNo(myCollege.getCollegeNo());
                }
            }
        }
        ceScoreLine.setCreatedUser(SecurityUtil.getUsername());
        return ceScoreLineMapper.insert(ceScoreLine) > 0 ? new Response<>().ok() : new Response<>().fail();
    }

    /**
     * 修改
     */
    @PutMapping
    public Response edit(@RequestBody CeScoreLine ceScoreLine) {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        if (loginUser != null && loginUser.getUser() != null) {
            boolean isSchoolAdmin = loginUser.getUser().getRoles().stream()
                    .anyMatch(r -> "school_admin".equals(r.getRoleKey()));
            if (isSchoolAdmin) {
                Long myCollegeId = loginUser.getUser().getCollegeId();
                if (myCollegeId == null) return new Response<>().failMsg("缺少所属院校管理权限");
                CeCollege myCollege = collegeService.getById(myCollegeId.intValue());
                if (myCollege == null || !myCollege.getCollegeNo().equals(ceScoreLine.getCollegeNo())) {
                    return new Response<>().failMsg("禁止越权修改其他院校录取分数");
                }
            }
        }
        ceScoreLine.setUpdatedUser(SecurityUtil.getUsername());
        return ceScoreLineMapper.updateById(ceScoreLine) > 0 ? new Response<>().ok() : new Response<>().fail();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{ids}")
    public Response remove(@PathVariable Integer[] ids) {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        boolean isSchoolAdmin = false;
        String myCollegeNo = null;
        if (loginUser != null && loginUser.getUser() != null) {
            isSchoolAdmin = loginUser.getUser().getRoles().stream()
                    .anyMatch(r -> "school_admin".equals(r.getRoleKey()));
            if (isSchoolAdmin) {
                Long myCollegeId = loginUser.getUser().getCollegeId();
                if (myCollegeId == null) return new Response<>().failMsg("缺少所属院校管理权限");
                CeCollege myCollege = collegeService.getById(myCollegeId.intValue());
                if (myCollege != null) myCollegeNo = myCollege.getCollegeNo();
            }
        }
        
        int count = 0;
        for (Integer id : ids) {
            if (isSchoolAdmin && myCollegeNo != null) {
                CeScoreLine item = ceScoreLineMapper.selectById(id);
                if (item == null || !myCollegeNo.equals(item.getCollegeNo())) {
                    continue; // 跳过不属于本校的数据
                }
            }
            count += ceScoreLineMapper.deleteById(id);
        }
        return count > 0 ? new Response<>().ok() : new Response<>().fail();
    }
}
