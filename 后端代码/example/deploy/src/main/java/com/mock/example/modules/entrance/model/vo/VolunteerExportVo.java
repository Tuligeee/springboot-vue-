package com.mock.example.modules.entrance.model.vo;

import com.mock.example.common.annotation.Excel;
import lombok.Data;
import java.util.Date;

/**
 * 志愿表导出对象
 */
@Data
public class VolunteerExportVo {
    @Excel(name = "序号")
    private Integer sort;

    @Excel(name = "院校代码")
    private String collegeNo;

    @Excel(name = "院校名称")
    private String collegeName;

    @Excel(name = "专业代码")
    private String professionNo;

    @Excel(name = "专业名称")
    private String professionName;

    @Excel(name = "填报分数")
    private Integer score;

    @Excel(name = "填报时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
