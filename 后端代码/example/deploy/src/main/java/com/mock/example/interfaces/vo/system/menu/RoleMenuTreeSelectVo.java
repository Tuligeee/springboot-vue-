package com.mock.example.interfaces.vo.system.menu;

import com.mock.example.common.entity.TreeSelect;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 角色菜单下拉树vo
 *
 * @author: Mock
 * @date: 2025-01-05 20:28:39
 */
@Data
@Accessors(chain = true)
public class RoleMenuTreeSelectVo {

    /**
     * 角色选择菜单key
     */
    private List<Integer> checkedKeys;

    /**
     * 菜单树
     */
    private List<TreeSelect> menus;
}

  