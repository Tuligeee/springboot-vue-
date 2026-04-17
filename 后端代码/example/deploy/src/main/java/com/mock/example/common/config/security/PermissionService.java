package com.mock.example.common.config.security;

import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.modules.system.types.LoginUser;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.Set;

/**
 * 自定义权限实现，ss取自PermissionService首字母
 */
@Service("ss")
public class PermissionService {
    /** 所有权限标识 */
    private static final String ALL_PERMISSION = "*:*:*";

    /**
     * 验证用户是否具备某权限
     *
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    public boolean hasPermi(String permission) {
        if (permission == null || permission.isEmpty()) {
            return true;
        }
        LoginUser loginUser = SecurityUtil.getLoginUser();
        if (loginUser == null || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        return hasPermissions(loginUser.getPermissions(), permission);
    }

    /**
     * 验证用户是否具有以下任意一个权限
     *
     * @param permissions 以解析符号分隔的权限列表
     * @return 用户是否具有以下任意一个权限
     */
    public boolean hasAnyPermi(String permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return true;
        }
        LoginUser loginUser = SecurityUtil.getLoginUser();
        if (loginUser == null || CollectionUtils.isEmpty(loginUser.getPermissions())) {
            return false;
        }
        Set<String> authorities = loginUser.getPermissions();
        for (String permission : permissions.split(",")) {
            if (permission != null && hasPermissions(authorities, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断用户是否拥有某个角色
     *
     * @param role 角色名称
     * @return 用户是否具备某角色
     */
    public boolean hasRole(String role) {
        if (role == null || role.isEmpty()) {
            return true;
        }
        return SecurityUtil.hasRole(role);
    }

    /**
     * 验证用户是否具有以下任意一个角色
     *
     * @param roles 角色列表
     * @return 用户是否具有以下任意一个角色
     */
    public boolean hasAnyRoles(String roles) {
        if (roles == null || roles.isEmpty()) {
            return true;
        }
        for (String role : roles.split(",")) {
            if (hasRole(role.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否包含权限
     *
     * @param permissions 权限列表
     * @param permission 权限字符串
     * @return 用户是否具备某权限
     */
    private boolean hasPermissions(Set<String> permissions, String permission) {
        return permissions.contains(ALL_PERMISSION) || permissions.contains(permission.trim());
    }
}
