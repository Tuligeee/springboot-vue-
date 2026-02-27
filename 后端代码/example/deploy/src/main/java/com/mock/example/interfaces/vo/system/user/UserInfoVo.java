package com.mock.example.interfaces.vo.system.user;

import com.mock.example.modules.system.entity.model.SysUser;
import lombok.Data;

import java.util.Set;

/**
 * 用户登录后相关信息
 *
 * @author: Mock
 * @date: 2025-01-03 21:43:11
 */
@Data
public class UserInfoVo {

    /**
     * 基本信息
     */
    private SysUser user;

    /**
     * 角色集合
     */
    private Set<String> roles;


    // 权限集合
    Set<String> permissions;
}

  