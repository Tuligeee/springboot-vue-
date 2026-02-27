package com.mock.example.modules.system.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mock.example.modules.system.entity.model.SysRoleDept;

/**
 * <p>
 * 角色和部门关联表 仓库服务类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
public interface ISysRoleDeptRepo extends IService<SysRoleDept> {

    /**
     * 删除角色和部门管理
     *
     * @param roleId 角色id
     */
    void deleteDeptByRoleId(Long roleId);
}
