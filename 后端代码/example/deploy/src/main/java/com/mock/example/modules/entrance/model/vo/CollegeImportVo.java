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
    // @Excel(name = "院校代码") - 已废弃
    private String collegeNo;

    @Excel(name = "学校")
    private String collegeName;

    @Excel(name = "所在城市")
    private String city;

    @Excel(name = "排名")
    private Integer ranking;

    @Excel(name = "水平层次", readConverterExp = "本科=本科,专科=专科")
    private String educationLevel;

    private Integer collegePersonCount;

    // --- 专业信息 ---
    // @Excel(name = "专业代码") - 已废弃
    private String professionNo;

    @Excel(name = "专业名称")
    private String professionName;

    @Excel(name = "修业年限")
    private Integer studyYear;

    @Excel(name = "选考科目要求", prompt = "如:物理,化学,生物 或 不限")
    private String subjectRequirement;

    private Integer professionPersonCount;

    // --- 分数线信息 (已弃用，不再通过此模板导入) ---
    private Integer year;

    private Integer score;

    private Integer admissionCount;
}
