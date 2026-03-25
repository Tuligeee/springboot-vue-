package com.mock.example.modules.entrance.model.vo;

import com.mock.example.common.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * 院校数据导入导出对象
 */
@Data
public class CollegeImportVo implements Serializable {
    private static final long serialVersionUID = 1L;

    // --- 院校信息 ---
    @Excel(name = "院校代码")
    private String collegeNo;

    @Excel(name = "院校名称")
    private String collegeName;

    @Excel(name = "所在城市")
    private String city;

    @Excel(name = "院校排名")
    private Integer ranking;

    // --- 专业信息 ---
    @Excel(name = "专业代码")
    private String professionNo;

    @Excel(name = "专业名称")
    private String professionName;

    @Excel(name = "修业年限")
    private Integer studyYear;

    // --- 分数线信息 ---
    @Excel(name = "录取年份")
    private Integer year;

    @Excel(name = "录取分数")
    private Integer score;
}
