package com.mock.example.modules.system.service;

import cn.hutool.core.date.DateUtil;
import com.mock.example.common.entity.Response;
import com.mock.example.common.exception.BizException;
import com.mock.example.common.utils.EntityCopyUtil;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.common.utils.StringUtil;
import com.mock.example.interfaces.body.system.role.RoleBody;
import com.mock.example.interfaces.body.system.user.UserBody;
import com.mock.example.interfaces.vo.system.role.RoleVo;
import com.mock.example.interfaces.vo.system.role.converter.RoleVoConverter;
import com.mock.example.interfaces.vo.system.user.UserVo;
import com.mock.example.interfaces.vo.system.user.converter.UserVoConverter;
import com.mock.example.modules.system.entity.model.SysRole;
import com.mock.example.modules.system.entity.model.SysRoleMenu;
import com.mock.example.modules.system.entity.model.SysUser;
import com.mock.example.modules.system.entity.model.SysUserRole;
import com.mock.example.modules.system.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 角色业务层
 *
 * @author: Mock
 * @date: 2025-01-04 19:50:05
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysRoleService {

    private final ISysRoleRepo roleRepository;

    private final ISysRoleMenuRepo roleMenuRepository;

    private final ISysRoleDeptRepo roleDeptRepository;

    private final ISysUserRepo userRepository;

    private final ISysUserRoleRepo userRoleRepository;

    /**
     * 获取所有角色vo
     *
     * @return 角色vo列表
     */
    public List<RoleVo> selectRoleAll() {
        List<SysRole> roles = roleRepository.selectRoleList(new SysRole());
        return RoleVoConverter.INSTANT.sysRoleToRoleVo(roles);
    }

    /**
     * 请求角色列表
     *
     * @param roleBody 角色请求体
     * @return 角色vo列表
     */
    public List<SysRole> selectRoleList(RoleBody roleBody) {
        SysRole sysRole = EntityCopyUtil.copyEntity(SysRole.class, roleBody);
        return roleRepository.selectRoleList(sysRole);
    }

    /**
     * 获取用户角色id列表
     *
     * @param userId 用户id
     * @return 用户角色id列表
     */
    public List<Integer> selectRoleListByUserId(Long userId) {
        return roleRepository.selectRoleIdsByUserId(userId);
    }

    /**
     * 通过角色id查询角色
     *
     * @param roleId 角色id
     * @return 角色
     */
    public RoleVo selectRoleById(Long roleId) {
        SysRole sysRole = roleRepository.selectRoleById(roleId);
        return RoleVoConverter.INSTANT.sysRoleToRoleVo(sysRole);
    }

    /**
     * 新增角色
     *
     * @param roleBody 角色请求对象
     * @return 结果
     */
    @Transactional
    public Response<Boolean> addRole(RoleBody roleBody) {
        checkAdminRole(roleBody);

        SysRole sysRole = EntityCopyUtil.copyEntity(SysRole.class, roleBody);

        if (BooleanUtils.isFalse(roleRepository.checkRoleNameUnique(sysRole))) {
            return new Response<>().failMsg("新增角色'" + roleBody.getRoleName() + "'失败，角色名称已存在");
        } else if (BooleanUtils.isFalse(roleRepository.checkRoleKeyUnique(sysRole))) {
            return new Response<>().failMsg("新增角色'" + roleBody.getRoleName() + "'失败，角色权限已存在");
        }

        sysRole.setCreateBy(SecurityUtil.getLoginUser().getUsername());
        sysRole.setCreateTime(DateUtil.date());

        // 新增角色
        roleRepository.save(sysRole);
        // 新增角色菜单
        insertRoleMenu(sysRole);

        return new Response<>(Boolean.TRUE);

    }

    /**
     * 编辑角色
     *
     * @param roleBody 角色请求对象
     * @return 结果
     */
    @Transactional
    public Response<Boolean> editRole(RoleBody roleBody) {
        checkAdminRole(roleBody);

        SysRole sysRole = EntityCopyUtil.copyEntity(SysRole.class, roleBody);
        sysRole.setUpdateBy(SecurityUtil.getUsername());
        sysRole.setUpdateTime(DateUtil.date());

        if (BooleanUtils.isFalse(roleRepository.checkRoleNameUnique(sysRole))) {
            return new Response<>().failMsg("编辑角色'" + roleBody.getRoleName() + "'失败，角色名称已存在");
        } else if (BooleanUtils.isFalse(roleRepository.checkRoleKeyUnique(sysRole))) {
            return new Response<>().failMsg("编辑角色'" + roleBody.getRoleName() + "'失败，角色权限已存在");
        }

        // 修改角色信息
        roleRepository.updateById(sysRole);
        // 删除角色信息
        roleMenuRepository.deleteRoleMenuByRoleId(sysRole.getRoleId());
        // 插入角色菜单
        insertRoleMenu(sysRole);

        return new Response<>(Boolean.TRUE);
    }

    /**
     * 更改角色状态
     *
     * @param roleBody 角色请求对象
     * @return 结果
     */
    public Boolean updateRoleStatus(RoleBody roleBody) {
        checkAdminRole(roleBody);

        roleRepository.updateRoleStatus(roleBody.getRoleId(), roleBody.getStatus());

        return Boolean.TRUE;
    }

    /**
     * 删除角色
     *
     * @param roleIds 角色id列表
     * @return 结果
     */
    @Transactional
    public Boolean deleteRoleByIds(Long[] roleIds) {
        for (Long roleId : roleIds) {
            // 是否管理员检查
            RoleBody roleBody = new RoleBody();
            roleBody.setRoleId(roleId);
            checkAdminRole(roleBody);

            SysRole role = roleRepository.selectRoleById(roleId);

            if (userRoleRepository.countByRoleId(roleId) > 0) {
                throw new BizException(String.format("%1$s已分配,不能删除", role.getRoleName()));
            }
        }

        Arrays.stream(roleIds).forEach(
                roleId -> {
                    // 删除角色与菜单关联
                    roleMenuRepository.deleteRoleMenuByRoleId(roleId);
                    // 删除角色与部门关联
                    roleDeptRepository.deleteDeptByRoleId(roleId);
                    // 删除角色
                    roleRepository.removeById(roleId);
                }
        );

        return Boolean.TRUE;
    }

    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    public void insertRoleMenu(SysRole role) {
        // 新增用户与角色管理
        List<SysRoleMenu> list = new ArrayList<>();
        for (Long menuId : role.getMenuIds()) {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(role.getRoleId());
            rm.setMenuId(menuId);
            list.add(rm);
        }
        roleMenuRepository.saveBatch(list);
    }

    /**
     * 检查是否管理员角色
     *
     * @param roleBody 角色请求对象
     */
    private static void checkAdminRole(RoleBody roleBody) {
        if (StringUtil.isNotNull(roleBody.getRoleId()) && roleBody.isAdmin()) {
            throw new BizException("不允许操作超级管理员角色");
        }
    }

    /**
     * 查询已分配用户角色列表
     *
     * @param userBody 用户请求对象
     * @return 表格结果
     */
    public List<UserVo> allocatedList(UserBody userBody) {
        SysUser user = EntityCopyUtil.copyEntity(SysUser.class, userBody);
        return UserVoConverter.INSTANT.sysUserToUserVo(
                userRepository.selectAllocatedList(user)
        );
    }

    /**
     * 查询未分配用户角色列表
     *
     * @param userBody 用户请求对象
     * @return 表格结果
     */
    public List<UserVo> unallocatedList(UserBody userBody) {
        SysUser user = EntityCopyUtil.copyEntity(SysUser.class, userBody);
        return UserVoConverter.INSTANT.sysUserToUserVo(
                userRepository.selectUnallocatedList(user)
        );
    }

    /**
     * 批量选择授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 用户id
     * @return 结果
     */
    public Boolean insertAuthUsers(Long roleId, Long[] userIds) {
        // 新增用户与角色管理
        List<SysUserRole> list = new ArrayList<>();
        for (Long userId : userIds) {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            list.add(ur);
        }

        userRoleRepository.saveBatch(list);

        return Boolean.TRUE;
    }

    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要取消授权的用户数据ID
     * @return 结果
     */
    public Boolean deleteAuthUsers(Long roleId, Long[] userIds) {
        userRoleRepository.deleteUserRoleInfos(roleId, userIds);
        return Boolean.TRUE;
    }

}

  