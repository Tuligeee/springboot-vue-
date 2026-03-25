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
        return ceScoreLineMapper.insert(ceScoreLine) > 0 ? new Response<>().ok() : new Response<>().fail();
    }

    /**
     * 修改
     */
    @PutMapping
    public Response edit(@RequestBody CeScoreLine ceScoreLine) {
        return ceScoreLineMapper.updateById(ceScoreLine) > 0 ? new Response<>().ok() : new Response<>().fail();
    }

    /**
     * 删除
     */
    @DeleteMapping("/{ids}")
    public Response remove(@PathVariable Integer[] ids) {
        int count = 0;
        for (Integer id : ids) {
            count += ceScoreLineMapper.deleteById(id);
        }
        return count > 0 ? new Response<>().ok() : new Response<>().fail();
    }
}
