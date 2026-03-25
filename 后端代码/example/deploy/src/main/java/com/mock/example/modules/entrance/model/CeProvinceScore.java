package com.mock.example.modules.entrance.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 历年分数线对象 ce_province_score
 * 
 * @author mock
 * @date 2026-03-25
 */
@Data
@TableName("ce_province_score")
public class CeProvinceScore implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 年份 */
    private Integer year;

    /** 省份 */
    private String province;

    /** 科类 (文科/理科/综合) */
    private String category;

    /** 批次 (本科一批/本科二批/高职专科) */
    private String batch;

    /** 分数线 */
    private Integer score;

    /** 备注 */
    private String remark;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    private Date updateTime;
}
