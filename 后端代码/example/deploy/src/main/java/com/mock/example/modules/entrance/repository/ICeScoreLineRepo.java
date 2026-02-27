package com.mock.example.modules.entrance.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mock.example.modules.entrance.entity.model.CeScoreLine;

import java.util.List;

/**
 * <p>
 * 分数线表 仓库类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2025-01-31
 */
public interface ICeScoreLineRepo extends IService<CeScoreLine> {

    /**
     * 通过院校代码查询分数线
     *
     * @param collegeNos 院校代码列表
     * @return 分数线列表
     */
    List<CeScoreLine> selectScoreLineByCollegeNos(List<String> collegeNos);

    /**
     * 通过院校代码和专业代码查询分数线
     *
     * @param collegeNo    院校代码
     * @param professionNo 专业代码
     * @return 结果
     */
    List<CeScoreLine> selectScoreLineByKey(String collegeNo, String professionNo);

    /**
     * 通过年度查询小于成绩的分数线
     *
     * @param year 年度
     * @param score 分数
     * @return 分数线
     */
    List<CeScoreLine> selectScoreLineLeScore(Integer year, Integer score);

    /**
     * 获取最近一年分数线时间
     *
     * @return 最近一年
     */
    Integer getLastYear();

    /**
     * 清除分数线
     *
     * @param professionNo 专业代码
     * @param collegeNo    院校代码
     */
    void unBindScoreLines(String professionNo, String collegeNo);

}
