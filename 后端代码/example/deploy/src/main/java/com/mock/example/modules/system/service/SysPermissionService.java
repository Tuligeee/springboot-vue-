package com.mock.example.modules.system.service;

import com.mock.example.modules.system.entity.model.SysRole;
import com.mock.example.modules.system.entity.model.SysUser;
import com.mock.example.modules.system.repository.ISysMenuRepo;
import com.mock.example.modules.system.repository.ISysRoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 系统用户权限处理业务层
 *
 * @author: Mock
 * @date: 2023-02-26 20:55:29
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysPermissionService {

    private final ISysRoleRepo roleRepository;

    private final ISysMenuRepo menuRepository;

    /**
     * 获取角色数据权限
     *
     * @param user 用户信息
     * @return 角色权限信息
     */
    public Set<String> getRolePermission(SysUser user) {
        Set<String> roles = new HashSet<String>();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            roles.add("admin");
        } else {
            List<SysRole> perms = roleRepository.selectRolePermissionByUserId(user.getUserId());
            Set<String> permsSet = new HashSet<>();
            for (SysRole perm : perms) {
                if (perm != null) {
                    permsSet.addAll(Arrays.asList(perm.getRoleKey().trim().split(",")));
                }
            }
            roles.addAll(permsSet);
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param user 用户信息
     * @return 菜单权限信息
     */
    public Set<String> getMenuPermission(SysUser user) {
        Set<String> perms = new HashSet<>();
        // 管理员拥有所有权限
        if (user.isAdmin()) {
            perms.add("*:*:*");
        } else {
            perms.addAll(menuRepository.selectMenuPermsByUserId(user.getUserId()));
        }
        return perms;
    }
}

  