package com.mock.example.modules.system.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mock.example.modules.system.entity.model.SysRole;

import java.util.List;

/**
 * <p>
 * 角色信息表 仓库服务类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
public interface ISysRoleRepo extends IService<SysRole> {


    /**
     * 根据用户ID查询角色权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<SysRole> selectRolePermissionByUserId(Long userId);

    /**
     * 查询角色列表
     *
     * @param role 角色
     * @return 角色列表
     */
    List<SysRole> selectRoleList(SysRole role);

    /**
     * 获取用户角色id列表
     *
     * @param userId 用户id
     * @return 用户角色id列表
     */
    List<Integer> selectRoleIdsByUserId(Long userId);

    /**
     * 通过角色id查询角色
     *
     * @param roleId 角色id
     * @return 角色
     */
    SysRole selectRoleById(Long roleId);

    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 用户角色组
     */
    String selectUserRoleGroup(String userName);

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    Boolean checkRoleNameUnique(SysRole role);

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    Boolean checkRoleKeyUnique(SysRole role);

    /**
     * 更改角色状态
     *
     * @param roleId 角色id
     * @param status 角色状态（0正常 1停用）
     */
    void updateRoleStatus(Long roleId, String status);

}
