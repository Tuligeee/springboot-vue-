package com.mock.example.modules.entrance.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 院校表
 * </p>
 *
 * @author Mybatis Auto
 * @since 2025-01-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CeCollege implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 院校代码
     */
    private String collegeNo;

    /**
     * 院校名称
     */
    private String collegeName;

    /**
     * 城市
     */
    private String city;

    /**
     * 排名
     */
    private Integer ranking;

    /**
     * 人数
     */
    private Integer personCount;

    /**
     * 详细信息
     */
    private String detailInfo;

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
     * 学校管理员ID (新增)
     */
    private Long managerId;

}
