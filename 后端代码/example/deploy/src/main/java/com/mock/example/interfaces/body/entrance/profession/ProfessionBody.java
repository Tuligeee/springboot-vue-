package com.mock.example.interfaces.body.entrance.profession;

import lombok.Data;

import java.util.List;

/**
 * 专业请求对象body
 *
 * @author: Mock
 * @date: 2023-04-01 20:57:31
 */
@Data
public class ProfessionBody {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 专业代码
     */
    private String professionNo;

    /**
     * 专业名称
     */
    private String professionName;

    /**
     * 所属院校代码
     */
    private String collegeNo;

    /**
     * 修业年限
     */
    private Integer studyYear;

    /**
     * 详情
     */
    private String detailInfo;

    /**
     * 分数线
     */
    private List<ScoreLineWrapper> scoreLines;

    /**
     * 分数线对象
     */
    @Data
    public static class ScoreLineWrapper{

        /**
         * 年份
         */
        private Integer year;

        /**
         * 分数
         */
        private Integer score;
    }

}

  