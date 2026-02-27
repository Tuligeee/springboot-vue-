package com.mock.example.interfaces.controller.entrance;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.common.entity.Response;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.modules.entrance.entity.model.CeBanner;
import com.mock.example.modules.entrance.service.CeBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/entrance/banner")
public class CeBannerController extends BaseController {

    @Autowired
    private CeBannerService bannerService;

    @GetMapping("/list")
    public TableDataInfo list(CeBanner banner) {
        startPage();
        QueryWrapper<CeBanner> query = new QueryWrapper<>();
        if (banner.getTitle() != null && !banner.getTitle().isEmpty()) {
            query.like("title", banner.getTitle());
        }
        if (banner.getStatus() != null && !banner.getStatus().isEmpty()) {
            query.eq("status", banner.getStatus());
        }
        query.orderByAsc("sort"); // 轮播图按 sort 字段升序排列
        List<CeBanner> list = bannerService.list(query);
        return getDataTable(list);
    }

    @GetMapping(value = "/{id}")
    public Response<CeBanner> getInfo(@PathVariable("id") Integer id) {
        return new Response<>(bannerService.getById(id));
    }

    @PostMapping
    public Response<Boolean> add(@RequestBody CeBanner banner) {
        return new Response<>(bannerService.save(banner));
    }

    @PutMapping
    public Response<Boolean> edit(@RequestBody CeBanner banner) {
        return new Response<>(bannerService.updateById(banner));
    }

    @DeleteMapping("/{ids}")
    public Response<Boolean> remove(@PathVariable Integer[] ids) {
        return new Response<>(bannerService.removeByIds(Arrays.asList(ids)));
    }
}
