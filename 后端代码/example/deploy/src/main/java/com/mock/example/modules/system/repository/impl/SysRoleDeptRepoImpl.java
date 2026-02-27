package com.mock.example.modules.system.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mock.example.modules.system.entity.model.SysRoleDept;
import com.mock.example.modules.system.mapper.SysRoleDeptMapper;
import com.mock.example.modules.system.repository.ISysRoleDeptRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 角色和部门关联表 仓库实现类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
@Repository
@RequiredArgsConstructor
public class SysRoleDeptRepoImpl
        extends ServiceImpl<SysRoleDeptMapper, SysRoleDept> implements ISysRoleDeptRepo {

    @Override
    public void deleteDeptByRoleId(Long roleId) {
        this.remove(
                Wrappers.<SysRoleDept>lambdaQuery()
                        .eq(SysRoleDept::getRoleId, roleId)
        );
    }
}
