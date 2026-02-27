package com.mock.example.modules.system.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mock.example.modules.system.entity.model.SysRoleMenu;
import com.mock.example.modules.system.mapper.SysRoleMenuMapper;
import com.mock.example.modules.system.repository.ISysRoleMenuRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 角色和菜单关联表 仓库实现类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
@Repository
@RequiredArgsConstructor
public class SysRoleMenuRepoImpl
        extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements ISysRoleMenuRepo {

    @Override
    public List<SysRoleMenu> selectRoleMenuByMenuId(Long menuId) {
        return this.list(
                Wrappers.<SysRoleMenu>lambdaQuery()
                        .eq(SysRoleMenu::getMenuId, menuId)
        );
    }

    @Override
    public void deleteRoleMenuByRoleId(Long roleId) {
        this.remove(
                Wrappers.<SysRoleMenu>lambdaQuery()
                        .eq(SysRoleMenu::getRoleId, roleId)
        );
    }

}
