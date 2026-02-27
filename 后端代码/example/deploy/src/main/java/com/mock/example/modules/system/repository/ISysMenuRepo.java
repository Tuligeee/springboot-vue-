package com.mock.example.modules.system.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mock.example.modules.system.entity.model.SysMenu;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 菜单权限表 仓库服务类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
public interface ISysMenuRepo extends IService<SysMenu> {

    /***
     * 根据用户id查询权限
     *
     * @param userId 用户id
     * @return 权限列表
     */
    Set<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据用户ID查询菜单树信息
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * 根据用户查询菜单列表
     *
     * @param menu   菜单信息
     * @param userId 用户id
     * @return 菜单列表
     */
    List<SysMenu> selectMenuList(SysMenu menu, Long userId);

    /**
     * 根据角色id查询菜单id列表
     *
     * @param roleId 角色id
     * @return 菜单id列表
     */
    List<Integer> selectMenuListByRoleId(Long roleId);

    /**
     * 查询菜单的子菜单
     *
     * @param menuId 菜单id
     * @return 子菜单
     */
    List<SysMenu> selectMenuChildList(Long menuId);

    /**
     * 检查菜单名称是否重复
     *
     * @param menu 菜单
     * @return true 无重复
     */
    Boolean checkMenuNameUnique(SysMenu menu);

}
