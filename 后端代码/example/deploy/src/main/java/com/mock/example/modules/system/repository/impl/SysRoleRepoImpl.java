package com.mock.example.modules.system.repository.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mock.example.modules.system.entity.model.SysRole;
import com.mock.example.modules.system.mapper.SysRoleMapper;
import com.mock.example.modules.system.repository.ISysRoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 角色信息表 仓库实现类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
@Repository
@RequiredArgsConstructor
public class SysRoleRepoImpl
        extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleRepo {

    private final SysRoleMapper mapper;

    @Override
    public List<SysRole> selectRolePermissionByUserId(Long userId) {
        return mapper.selectRolePermissionByUserId(userId);
    }

    @Override
    public List<SysRole> selectRoleList(SysRole role) {
        return mapper.selectRoleList(role);
    }

    @Override
    public List<Integer> selectRoleIdsByUserId(Long userId) {
        return mapper.selectRoleIdsByUserId(userId);
    }

    @Override
    public SysRole selectRoleById(Long roleId) {
        return mapper.selectRoleById(roleId);
    }

    @Override
    public String selectUserRoleGroup(String userName) {
        List<SysRole> list = mapper.selectRolesByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysRole role : list) {
            idsStr.append(role.getRoleName()).append(",");
        }
        if (StrUtil.isNotBlank(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    @Override
    public Boolean checkRoleNameUnique(SysRole role) {
        SysRole sysRole = this.getOne(
                Wrappers.<SysRole>lambdaQuery()
                        .eq(SysRole::getRoleName, role.getRoleName())
                        .last("limit 1")
        );
        if (sysRole != null && !sysRole.getRoleId().equals(role.getRoleId())) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public Boolean checkRoleKeyUnique(SysRole role) {
        SysRole sysRole = this.getOne(
                Wrappers.<SysRole>lambdaQuery()
                        .eq(SysRole::getRoleKey, role.getRoleKey())
                        .last("limit 1")
        );
        if (sysRole != null && !sysRole.getRoleId().equals(role.getRoleId())) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    @Override
    public void updateRoleStatus(Long roleId, String status) {
        this.update(
                Wrappers.<SysRole>lambdaUpdate()
                        .set(SysRole::getStatus, status)
                        .eq(SysRole::getRoleId, roleId)
        );
    }

}
