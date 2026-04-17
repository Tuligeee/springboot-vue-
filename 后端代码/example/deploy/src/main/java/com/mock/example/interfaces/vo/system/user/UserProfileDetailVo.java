package com.mock.example.interfaces.vo.system.user;

import com.mock.example.modules.system.entity.model.SysUser;
import lombok.Data;

/**
 * 用户个人中心vo
 *
 * @author: Mock
 * @date: 2025-01-18 06:54:21
 */
@Data
public class UserProfileDetailVo {

    /**
     * 角色组
     */
    private String roleGroup;

    /**
     * 岗位组
     */
    private String postGroup;

    /**
     * 用户信息
     */
    private SysUser user;
}

  