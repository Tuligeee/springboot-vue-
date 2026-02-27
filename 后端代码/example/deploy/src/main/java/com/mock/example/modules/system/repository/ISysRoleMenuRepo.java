package com.mock.example.modules.system.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mock.example.modules.system.entity.model.SysRoleMenu;

import java.util.List;

/**
 * <p>
 * 角色和菜单关联表 仓库服务类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
public interface ISysRoleMenuRepo extends IService<SysRoleMenu> {

    /**
     * 通过菜单id查询
     *
     * @param menuId 菜单id
     * @return 角色菜单关系列表
     */
    List<SysRoleMenu> selectRoleMenuByMenuId(Long menuId);

    /**
     * 通过角色id删除关联
     *
     * @param roleId 角色id
     */
    void deleteRoleMenuByRoleId(Long roleId);

}
