package com.mock.example.interfaces.vo.entrance.profession;

import lombok.Data;

/**
 * 专业列表行视图对象
 */
@Data
public class ProfessionListRowVo {

    private Integer id;
    private String collegeName;
    private String professionName;
    private Integer studyYear;

    /**
     * 选科要求
     */
    private String subjectRequirement;

    /**
     * 招生人数
     */
    private Integer personCount;

    /**
     * 专业代码
     */
    private String professionNo;
}
