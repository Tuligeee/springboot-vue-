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

@RestController
@RequestMapping("/entrance/profession")
public class CeProfessionController extends BaseController {

    @Autowired
    private CeProfessionService professionService;

    /**
     * 查询专业列表
     */
    @GetMapping("/list")
    public TableDataInfo list(CeProfession profession) {
        startPage();
        QueryWrapper<CeProfession> query = new QueryWrapper<>();

        // 【关键】使用 collegeNo (院校代码) 进行查询
        if (profession.getCollegeNo() != null && !profession.getCollegeNo().isEmpty()) {
            query.eq("college_no", profession.getCollegeNo());
        }

        // 也可以加上按名称模糊搜索
        if (profession.getProfessionName() != null && !profession.getProfessionName().isEmpty()) {
            query.like("profession_name", profession.getProfessionName());
        }

        return getDataTable(professionService.list(query));
    }
}
