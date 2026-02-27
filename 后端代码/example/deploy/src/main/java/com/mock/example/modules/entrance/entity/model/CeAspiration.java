package com.mock.example.modules.entrance.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;


/**
 * <p>
 * 志愿表单表
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CeAspiration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学生学号
     */
    private String studentNo;

    /**
     * 填报年份
     */
    private Integer entranceYear;

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

    /**
     * 学生姓名
     */
    @TableField(exist = false)
    private String studentName;

}
