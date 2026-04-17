package com.mock.example.modules.entrance.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 志愿明细
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-04-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CeAspirationDetail implements Serializable {

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
     * 院校代码
     */
    private String collegeNo;

    /**
     * 院校名称
     */
    private String collegeName;

    /**
     * 专业代码
     */
    private String professionNo;

    /**
     * 专业名称
     */
    private String professionName;

    /**
     * 志愿批次
     */
    private Integer aspirationBatch;

    /**
     * 志愿专业顺序
     */
    private Integer professionSort;

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
