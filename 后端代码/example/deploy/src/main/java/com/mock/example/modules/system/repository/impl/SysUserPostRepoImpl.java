package com.mock.example.modules.system.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mock.example.modules.system.entity.model.SysUserPost;
import com.mock.example.modules.system.mapper.SysUserPostMapper;
import com.mock.example.modules.system.repository.ISysUserPostRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户与岗位关联表 仓库实现类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
@Repository
@RequiredArgsConstructor
public class SysUserPostRepoImpl
        extends ServiceImpl<SysUserPostMapper, SysUserPost> implements ISysUserPostRepo {

    @Override
    public int countPostByPostId(Long postId) {
        return this.count(
                Wrappers.<SysUserPost>lambdaQuery()
                        .eq(SysUserPost::getPostId,postId)
        );
    }

    @Override
    public void deletePostByUserId(Long userId) {
        this.remove(
                Wrappers.<SysUserPost>lambdaQuery()
                        .eq(SysUserPost::getUserId, userId)
        );
    }
}
