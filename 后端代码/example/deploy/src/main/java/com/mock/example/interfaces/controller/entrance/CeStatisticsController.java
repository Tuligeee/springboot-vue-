package com.mock.example.interfaces.controller.entrance;

import com.mock.example.common.entity.Response;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.modules.entrance.service.CeStatisticsService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 仪表盘统计数据接口
 * 
 * @author mock
 * @date 2026-04-03
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/entrance/statistics")
public class CeStatisticsController extends BaseController {

    private final CeStatisticsService ceStatisticsService;

    @ApiOperation(value = "获取顶部数据概览卡片")
    @GetMapping("/overview")
    public Response<Map<String, Object>> getOverview() {
        return new Response<>(ceStatisticsService.getOverviewStats());
    }

    @ApiOperation(value = "获取图表统计数据")
    @GetMapping("/chart")
    public Response<Map<String, Object>> getChart() {
        return new Response<>(ceStatisticsService.getChartStats());
    }
}
