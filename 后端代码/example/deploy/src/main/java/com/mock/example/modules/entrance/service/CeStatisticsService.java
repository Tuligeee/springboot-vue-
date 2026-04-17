package com.mock.example.modules.entrance.service;

import java.util.Map;

/**
 * 数据大屏统计接口
 * 
 * @author mock
 * @date 2026-04-03
 */
public interface CeStatisticsService {
    
    /**
     * 获取全站数据概览
     */
    Map<String, Object> getOverviewStats();

    /**
     * 获取分类统计图表数据
     */
    Map<String, Object> getChartStats();
}
