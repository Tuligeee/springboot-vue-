package com.mock.example.interfaces.controller.entrance;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.common.entity.Response;
import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.modules.entrance.model.CeProvinceScore;
import com.mock.example.modules.entrance.service.ICeProvinceScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 历年分数线Controller
 * 
 * @author mock
 * @date 2026-03-25
 */
@RestController
@RequestMapping("/entrance/provinceScore")
public class CeProvinceScoreController extends BaseController {
    
    @Autowired
    private ICeProvinceScoreService ceProvinceScoreService;

    /**
     * 查询历年分数线列表
     */
    @GetMapping("/list")
    public TableDataInfo list(CeProvinceScore ceProvinceScore) {
        startPage();
        QueryWrapper<CeProvinceScore> queryWrapper = new QueryWrapper<>();
        if (ceProvinceScore != null) {
            if (ceProvinceScore.getYear() != null) {
                queryWrapper.eq("year", ceProvinceScore.getYear());
            }
            if (ceProvinceScore.getProvince() != null && !ceProvinceScore.getProvince().isEmpty()) {
                queryWrapper.like("province", ceProvinceScore.getProvince());
            }
            if (ceProvinceScore.getCategory() != null && !ceProvinceScore.getCategory().isEmpty()) {
                queryWrapper.eq("category", ceProvinceScore.getCategory());
            }
        }
        List<CeProvinceScore> list = ceProvinceScoreService.list(queryWrapper);
        return getDataTable(list);
    }

    /**
     * 获取历年分数线详细信息
     */
    @GetMapping(value = "/{id}")
    public Response getInfo(@PathVariable("id") Long id) {
        return new Response<>(ceProvinceScoreService.getById(id));
    }

    /**
     * 新增历年分数线
     */
    @PostMapping
    public Response add(@RequestBody CeProvinceScore ceProvinceScore) {
        return toAjax(ceProvinceScoreService.save(ceProvinceScore));
    }

    /**
     * 修改历年分数线
     */
    @PutMapping
    public Response edit(@RequestBody CeProvinceScore ceProvinceScore) {
        return toAjax(ceProvinceScoreService.updateById(ceProvinceScore));
    }

    /**
     * 删除历年分数线
     */
    @DeleteMapping("/{ids}")
    public Response remove(@PathVariable Long[] ids) {
        return toAjax(ceProvinceScoreService.removeByIds(java.util.Arrays.asList(ids)));
    }
}
