package com.mock.example.modules.system.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mock.example.modules.system.entity.model.SysUserRole;
import com.mock.example.modules.system.mapper.SysUserRoleMapper;
import com.mock.example.modules.system.repository.ISysUserRoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户和角色关联表 仓库实现类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
@Repository
@RequiredArgsConstructor
public class SysUserRoleRepoImpl
        extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleRepo {

    @Override
    public void deleteRoleByUserId(Long userId) {
        this.remove(
                Wrappers.<SysUserRole>lambdaQuery()
                        .eq(SysUserRole::getUserId, userId)
        );
    }

    @Override
    public void deleteUserRoleInfos(Long roleId, Long[] userIds) {
        this.remove(
                Wrappers.<SysUserRole>lambdaQuery()
                        .in(SysUserRole::getUserId, userIds)
                        .eq(SysUserRole::getRoleId, roleId)
        );
    }

    @Override
    public Integer countByRoleId(Long roleId) {
        return this.count(
                Wrappers.<SysUserRole>lambdaQuery()
                        .eq(SysUserRole::getRoleId, roleId)
        );
    }
}
