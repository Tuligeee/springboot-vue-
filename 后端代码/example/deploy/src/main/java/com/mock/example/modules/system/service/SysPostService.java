package com.mock.example.modules.system.service;

import cn.hutool.core.date.DateUtil;
import com.mock.example.common.entity.Response;
import com.mock.example.common.exception.BizException;
import com.mock.example.common.utils.EntityCopyUtil;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.interfaces.body.system.post.PostBody;
import com.mock.example.interfaces.vo.system.post.PostVo;
import com.mock.example.interfaces.vo.system.post.converter.PostVoConverter;
import com.mock.example.modules.system.entity.model.SysPost;
import com.mock.example.modules.system.repository.ISysPostRepo;
import com.mock.example.modules.system.repository.ISysUserPostRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 岗位业务层
 *
 * @author: Mock
 * @date: 2025-01-04 20:04:16
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysPostService {

    private final ISysPostRepo postRepository;

    private final ISysUserPostRepo userPostRepository;

    /**
     * 获取岗位列表
     *
     * @param postBody 岗位请求对象
     * @return 岗位列表
     */
    public List<SysPost> selectPostList(PostBody postBody) {
        return postRepository.selectPostList(EntityCopyUtil.copyEntity(SysPost.class, postBody));
    }

    /**
     * 根据岗位编号获取详细信息
     *
     * @param postId 岗位id
     * @return 岗位信息
     */
    public PostVo selectPostById(Long postId) {
        return PostVoConverter.INSTANT.sysPostToPostVo(postRepository.getById(postId));
    }

    /**
     * 新增岗位
     *
     * @param postBody 岗位信息
     * @return 结果
     */
    public Response addPost(PostBody postBody) {
        SysPost sysPost = EntityCopyUtil.copyEntity(SysPost.class, postBody);
        if (!postRepository.checkPostNameUnique(sysPost)) {

            return new Response<>().failMsg("新增岗位'" + postBody.getPostName() + "'失败，岗位名称已存在");
        } else if (!postRepository.checkPostCodeUnique(sysPost)) {

            return new Response<>().failMsg("新增岗位'" + postBody.getPostName() + "'失败，岗位编码已存在");
        }
        sysPost.setCreateBy(SecurityUtil.getUsername());
        sysPost.setCreateTime(DateUtil.date());

        postRepository.save(sysPost);

        return new Response<>(Boolean.TRUE);
    }

    /**
     * 编辑岗位
     *
     * @param postBody 岗位信息
     * @return 结果
     */
    public Response editPost(PostBody postBody) {
        SysPost sysPost = EntityCopyUtil.copyEntity(SysPost.class, postBody);
        if (!postRepository.checkPostNameUnique(sysPost)) {

            return new Response<>().failMsg("修改岗位'" + postBody.getPostName() + "'失败，岗位名称已存在");
        } else if (!postRepository.checkPostCodeUnique(sysPost)) {

            return new Response<>().failMsg("修改岗位'" + postBody.getPostName() + "'失败，岗位编码已存在");
        }
        sysPost.setUpdateBy(SecurityUtil.getUsername());
        sysPost.setUpdateTime(DateUtil.date());

        postRepository.updateById(sysPost);

        return new Response<>(Boolean.TRUE);
    }

    /**
     * 删除岗位
     *
     * @param postIds 岗位ID列表
     * @return 结果
     */
    public Response deleteByPostIds(Long[] postIds) {
        for (Long postId : postIds) {
            SysPost post = postRepository.getById(postId);
            if (userPostRepository.countPostByPostId(postId) > 0) {
                throw new BizException(String.format("%1$s已分配,不能删除", post.getPostName()));
            }
        }
        postRepository.deletePostByIds(postIds);

        return new Response<>(Boolean.TRUE);
    }

    /**
     * 获取所有岗位
     *
     * @return 岗位列表
     */
    public List<PostVo> selectPostAll() {
        return PostVoConverter.INSTANT.sysPostToPostVo(
                postRepository.selectPostList(new SysPost())
        );
    }

    /**
     * 查询用户岗位ID列表
     *
     * @param userId 用户id
     * @return 岗位id列表
     */
    public List<Integer> selectPostListByUserId(Long userId) {
        return postRepository.selectPostListByUserId(userId);
    }

}

  