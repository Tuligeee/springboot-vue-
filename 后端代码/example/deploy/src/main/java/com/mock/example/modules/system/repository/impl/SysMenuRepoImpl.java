package com.mock.example.modules.system.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.modules.system.entity.model.SysMenu;
import com.mock.example.modules.system.entity.model.SysUser;
import com.mock.example.modules.system.mapper.SysMenuMapper;
import com.mock.example.modules.system.repository.ISysMenuRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单权限表 仓库实现类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
@Repository
@RequiredArgsConstructor
public class SysMenuRepoImpl
        extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuRepo {

    private final SysMenuMapper mapper;

    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        List<String> perms = mapper.selectMenuPermsByUserId(userId);

        Set<String> permsSet = Sets.newHashSet();
        for (String perm : perms) {
            if (StrUtil.isNotBlank(perm)) {
                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
            }
        }

        return permsSet;
    }

    @Override
    public List<SysMenu> selectMenuTreeByUserId(Long userId) {
        List<SysMenu> menus;
        if (SecurityUtil.isAdmin(userId)) {
            menus = mapper.selectMenuTreeAll();
        } else {
            menus = mapper.selectMenuTreeByUserId(userId);
        }
        return getChildPerms(menus, 0);
    }

    @Override
    public List<SysMenu> selectMenuList(SysMenu menu, Long userId) {
        List<SysMenu> menuList;
        // 管理员显示所有菜单信息
        if (SysUser.isAdmin(userId)) {
            menuList = mapper.selectMenuList(menu);
        } else {
            menu.getParams().put("userId", userId);
            menuList = mapper.selectMenuListByUserId(menu);
        }
        return menuList;
    }

    @Override
    public List<Integer> selectMenuListByRoleId(Long roleId) {
        return mapper.selectMenuListByRoleId(roleId);
    }

    @Override
    public List<SysMenu> selectMenuChildList(Long menuId) {
        return this.list(
                Wrappers.<SysMenu>lambdaQuery()
                        .eq(SysMenu::getParentId, menuId)
        );
    }

    @Override
    public Boolean checkMenuNameUnique(SysMenu menu) {
        List<SysMenu> sysMenus = this.list(
                Wrappers.<SysMenu>lambdaQuery()
                        .eq(SysMenu::getMenuName, menu.getMenuName())
        );

        // 过滤掉自己的menuId,判断是否还有重复
        return CollUtil.isEmpty(
                sysMenus.stream()
                        .filter(sysMenu -> !sysMenu.getMenuId().equals(menu.getMenuId()))
                        .collect(Collectors.toList())
        );
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, int parentId) {
        List<SysMenu> returnList = new ArrayList<>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext(); ) {
            SysMenu t = iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId) {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t) {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t) {
        List<SysMenu> tlist = new ArrayList<>();
        Iterator<SysMenu> it = list.iterator();
        while (it.hasNext()) {
            SysMenu n = (SysMenu) it.next();
            if (n.getParentId().longValue() == t.getMenuId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }
}
