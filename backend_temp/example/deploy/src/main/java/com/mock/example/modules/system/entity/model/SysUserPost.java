package com.mock.example.modules.system.entity.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 用户与岗位关联表
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysUserPost implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 岗位ID
     */
    private Long postId;


}
