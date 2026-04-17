package com.mock.example.interfaces.controller.entrance;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.common.entity.Response;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.modules.entrance.entity.model.CeProvinceLine;
import com.mock.example.modules.entrance.service.CeProvinceLineService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 档线信息管理
 */
@RestController
@RequestMapping("/entrance/provinceLine")
@RequiredArgsConstructor
public class CeProvinceLineController extends BaseController {

    private final CeProvinceLineService provinceLineService;

    /**
     * 查询列表
     */
    @ApiOperation(value = "查询档线列表")
    @GetMapping("/list")
    public TableDataInfo list(CeProvinceLine provinceLine) {
        startPage();
        QueryWrapper<CeProvinceLine> query = new QueryWrapper<>();
        // 年份查询
        if (provinceLine.getYear() != null) {
            query.eq("year", provinceLine.getYear());
        }
        // 省份模糊查询
        if (provinceLine.getProvince() != null && !provinceLine.getProvince().isEmpty()) {
            query.like("province", provinceLine.getProvince());
        }
        // 批次查询
        if (provinceLine.getBatchName() != null && !provinceLine.getBatchName().isEmpty()) {
            query.like("batch_name", provinceLine.getBatchName());
        }

        // 数据隔离约束：省控线为全国通用基础数据，不应限制学校管理员只看本市数据。
        // 已删除原本按 city 匹配 province 的错误过滤逻辑。

        query.orderByDesc("year");

        List<CeProvinceLine> list = provinceLineService.list(query);
        return getDataTable(list);
    }

    /**
     * 获取详情
     */
    @ApiOperation(value = "获取档线详情")
    @GetMapping(value = "/{id}")
    public Response<CeProvinceLine> getInfo(@PathVariable("id") Integer id) {
        return new Response<>(provinceLineService.getById(id));
    }

    /**
     * 新增
     */
    @ApiOperation(value = "新增档线")
    @PostMapping
    public Response<Boolean> add(@RequestBody CeProvinceLine provinceLine) {
        if (getLoginUser() != null && !getLoginUser().getUserId().equals(1L)) {
            boolean isAdmin = getLoginUser().getUser().getRoles().stream()
                    .anyMatch(r -> "admin".equals(r.getRoleKey()));
            if (!isAdmin) return new Response<Boolean>().failMsg("越权操作：仅超级管理员可新增省控线");
        }
        return new Response<>(provinceLineService.save(provinceLine));
    }

    /**
     * 修改
     */
    @ApiOperation(value = "修改档线")
    @PutMapping
    public Response<Boolean> edit(@RequestBody CeProvinceLine provinceLine) {
        if (getLoginUser() != null && !getLoginUser().getUserId().equals(1L)) {
            boolean isAdmin = getLoginUser().getUser().getRoles().stream()
                    .anyMatch(r -> "admin".equals(r.getRoleKey()));
            if (!isAdmin) return new Response<Boolean>().failMsg("越权操作：仅超级管理员可修改省控线");
        }
        return new Response<>(provinceLineService.updateById(provinceLine));
    }

    /**
     * 删除
     */
    @ApiOperation(value = "删除档线")
    @DeleteMapping("/{ids}")
    public Response<Boolean> remove(@PathVariable Integer[] ids) {
        if (getLoginUser() != null && !getLoginUser().getUserId().equals(1L)) {
            boolean isAdmin = getLoginUser().getUser().getRoles().stream()
                    .anyMatch(r -> "admin".equals(r.getRoleKey()));
            if (!isAdmin) return new Response<Boolean>().failMsg("越权操作：仅超级管理员可删除省控线");
        }
        return new Response<>(provinceLineService.removeByIds(Arrays.asList(ids)));
    }
}
