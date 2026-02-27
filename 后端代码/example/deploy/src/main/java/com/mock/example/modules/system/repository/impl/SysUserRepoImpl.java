package com.mock.example.modules.system.repository.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.modules.system.entity.model.SysUser;
import com.mock.example.modules.system.mapper.SysUserMapper;
import com.mock.example.modules.system.repository.ISysUserRepo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 用户信息表 仓库实现类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
@Repository
@RequiredArgsConstructor
public class SysUserRepoImpl
        extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserRepo {

    private final SysUserMapper mapper;

    @Override
    public SysUser selectByUserId(Long userId) {
        return this.getOne(
                Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUserId, userId)
                        .last(" limit 1")
        );
    }

    @Override
    public SysUser selectUserByUserName(String userName) {
        return mapper.selectUserByUserName(userName);
    }

    @Override
    public List<SysUser> selectUserByUserIds(List<Long> userIds) {
        if (CollUtil.isEmpty(userIds)) {
            return Lists.newArrayList();
        }
        return this.list(
                Wrappers.<SysUser>lambdaQuery()
                        .in(SysUser::getUserId, userIds)
                        .eq(SysUser::getStatus, 0)
        );
    }

    @Override
    public List<SysUser> selectUserList(SysUser user) {
        return mapper.selectUserList(user);
    }

    @Override
    public List<SysUser> selectAllocatedList(SysUser user) {
        return mapper.selectAllocatedList(user);
    }

    @Override
    public List<SysUser> selectUnallocatedList(SysUser user) {
        return mapper.selectUnallocatedList(user);
    }

    @Override
    public Boolean checkUserNameUnique(String userName) {
        return BooleanUtils.isFalse(
                this.count(
                        Wrappers.<SysUser>lambdaQuery()
                                .eq(SysUser::getUserName, userName)
                                .last(" limit 1")) > 0
        );
    }

    @Override
    public Boolean checkDeptExistUser(Long deptId) {
        return BooleanUtils.isTrue(
                this.count(
                        Wrappers.<SysUser>lambdaQuery()
                                .eq(SysUser::getDeptId, deptId)
                                .last(" limit 1")) > 0
        );
    }

    @Override
    public void updateUserStatus(Long userId, String status) {
        this.update(
                Wrappers.<SysUser>lambdaUpdate()
                        .set(SysUser::getStatus, status)
                        .set(SysUser::getUpdateBy, SecurityUtil.getLoginUser().getUsername())
                        .set(SysUser::getUpdateTime, DateUtil.date())
                        .eq(SysUser::getUserId, userId)
        );
    }

    @Override
    public void updatePassword(Long userId, String password) {
        this.update(
                Wrappers.<SysUser>lambdaUpdate()
                        .set(SysUser::getPassword, password)
                        .set(SysUser::getUpdateBy, SecurityUtil.getLoginUser().getUsername())
                        .set(SysUser::getUpdateTime, DateUtil.date())
                        .eq(SysUser::getUserId, userId)
        );
    }

    @Override
    public void updateAvatar(Long userId, String avatar) {
        this.update(
                Wrappers.<SysUser>lambdaUpdate()
                        .set(SysUser::getAvatar, avatar)
                        .set(SysUser::getUpdateBy, SecurityUtil.getLoginUser().getUsername())
                        .set(SysUser::getUpdateTime, DateUtil.date())
                        .eq(SysUser::getUserId, userId)
        );
    }

}
