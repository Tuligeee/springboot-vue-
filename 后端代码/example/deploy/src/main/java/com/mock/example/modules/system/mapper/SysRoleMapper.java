package com.mock.example.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mock.example.modules.system.entity.model.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 角色信息表 Mapper 接口
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
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
     * 查询用户角色组
     *
     * @param userName 用户名
     * @return 用户角色组
     */
    List<SysRole> selectRolesByUserName(String userName);
}
