package com.mock.example.interfaces.vo.system.dept;

import com.mock.example.common.entity.TreeSelect;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 角色部门树vo
 *
 * @author: Mock
 * @date: 2025-01-05 21:28:35
 */
@Data
@Accessors(chain = true)
public class RoleDeptTreeSelectVo {

    /**
     * 角色选择部门key
     */
    private List<Integer> checkedKeys;

    /**
     * 部门树
     */
    private List<TreeSelect> depts;
}

  