package com.mock.example.modules.entrance.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.mock.example.modules.entrance.entity.enums.TagEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 标签关联表
 * </p>
 *
 * @author Mybatis Auto
 * @since 2025-01-31
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class CeTagRel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 标签类型：STUDENT-学生 PROFESSION-专业
     */
    private TagEnum tagType;

    /**
     * 关联id。ce_student.id，ce_college.id，ce_profession.id 。
     */
    private Integer relId;

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
