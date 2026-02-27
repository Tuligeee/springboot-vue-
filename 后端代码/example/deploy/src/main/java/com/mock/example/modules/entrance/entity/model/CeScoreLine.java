package com.mock.example.modules.entrance.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 分数线表
 * </p>
 *
 * @author Mybatis Auto
 * @since 2025-01-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CeScoreLine implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 专业代码
     */
    private String professionNo;

    /**
     * 院校代码
     */
    private String collegeNo;

    /**
     * 分数线
     */
    private Integer score;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 创建人
     */
    private String createdUser;

    /**
     * 修改时间
     */
    private Date updatedTime;

    /**
     * 修改人
     */
    private String updatedUser;


}
