package com.mock.example.interfaces.vo.entrance.profession;

import lombok.Data;

/**
 * ???б????????????????????У??????????
 */
@Data
public class ProfessionListRowVo {

    private Integer id;
    private String collegeName;
    private String professionName;
    private Integer studyYear;

    /**
     * 不面向「专业查询」列表展示，供其它页面（如院校详情查分数线）使用
     */
    private String professionNo;
}
