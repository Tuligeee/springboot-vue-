package com.mock.example.modules.system.repository.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mock.example.modules.system.entity.model.SysPost;
import com.mock.example.modules.system.mapper.SysPostMapper;
import com.mock.example.modules.system.repository.ISysPostRepo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 岗位信息表 仓库实现类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
@Repository
@RequiredArgsConstructor
public class SysPostRepoImpl
        extends ServiceImpl<SysPostMapper, SysPost> implements ISysPostRepo {

    private final SysPostMapper mapper;

    @Override
    public List<SysPost> selectPostList(SysPost post) {
        return mapper.selectPostList(post);
    }

    @Override
    public List<Integer> selectPostListByUserId(Long userId) {
        return mapper.selectPostListByUserId(userId);
    }

    @Override
    public String selectUserPostGroup(String userName) {
        List<SysPost> list = mapper.selectPostsByUserName(userName);
        StringBuffer idsStr = new StringBuffer();
        for (SysPost post : list) {
            idsStr.append(post.getPostName()).append(",");
        }
        if (StringUtils.isNotEmpty(idsStr.toString())) {
            return idsStr.substring(0, idsStr.length() - 1);
        }
        return idsStr.toString();
    }

    @Override
    public Boolean checkPostNameUnique(SysPost sysPost) {
        List<SysPost> sysPosts = this.list(
                Wrappers.<SysPost>lambdaQuery()
                        .eq(SysPost::getPostName, sysPost.getPostName())
        );

        // 过滤掉自己的postId,判断是否还有重复
        return CollUtil.isEmpty(
                sysPosts.stream()
                        .filter(post -> !post.getPostId().equals(post.getPostId()))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Boolean checkPostCodeUnique(SysPost sysPost) {
        List<SysPost> sysPosts = this.list(
                Wrappers.<SysPost>lambdaQuery()
                        .eq(SysPost::getPostCode, sysPost.getPostCode())
        );

        // 过滤掉自己的postId,判断是否还有重复
        return CollUtil.isEmpty(
                sysPosts.stream()
                        .filter(post -> !post.getPostCode().equals(post.getPostCode()))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void deletePostByIds(Long[] postIds) {
        this.remove(
                Wrappers.<SysPost>lambdaQuery()
                        .in(SysPost::getPostId, postIds)
        );
    }
}
