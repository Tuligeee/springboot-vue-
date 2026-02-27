package com.mock.example.modules.entrance.service;

import com.mock.example.common.utils.SecurityUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mock.example.common.exception.BizException;
import com.mock.example.modules.entrance.entity.model.CeForumComment;
import com.mock.example.modules.entrance.entity.model.CeForumPost;
import com.mock.example.modules.entrance.mapper.CeForumMapper;
import com.mock.example.modules.system.mapper.SysUserMapper; // 引入用户Mapper
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CeForumService {

    private final CeForumMapper forumMapper;
    // 还需要注入评论的Mapper，这里简单处理直接用 forumMapper 里的注解SQL，
    // 但正规做法应该有一个 CeForumCommentMapper。这里为了简便我们用 MyBatis-Plus 的 insert。
    // 如果你没有创建 CommentMapper，下面保存评论时需要单独处理。
    // 建议：在 CeForumMapper 同级目录下创建一个 CeForumCommentMapper 继承 BaseMapper<CeForumComment>
    private final com.mock.example.modules.entrance.mapper.CeForumCommentMapper commentMapper;

    // 1. 获取帖子列表
    public List<CeForumPost> getPostList() {
        return forumMapper.selectPostList();
    }

    // 2. 获取帖子详情
    public CeForumPost getPostDetail(Long id) {
        // 增加阅读量
        CeForumPost post = forumMapper.selectById(id);
        if(post != null) {
            post.setViewCount(post.getViewCount() == null ? 1 : post.getViewCount() + 1);
            forumMapper.updateById(post);
        }
        return post;
    }

    // 3. 发布帖子
    public void publishPost(CeForumPost post) {
        post.setUserId(SecurityUtil.getUserId());
        post.setCreateTime(new Date());
        post.setViewCount(0);
        post.setLikeCount(0);
        forumMapper.insert(post);
    }

    // 4. 获取评论列表
    public List<CeForumComment> getComments(Long postId) {
        return forumMapper.selectCommentsByPostId(postId);
    }

    // 5. 发布评论
    public void publishComment(CeForumComment comment) {
        comment.setUserId(SecurityUtil.getUserId());
        comment.setCreateTime(new Date());
        commentMapper.insert(comment);
    }

    public void deletePost(Long postId) {
        // 1. 查出帖子信息
        CeForumPost post = forumMapper.selectById(postId);
        if (post == null) {
            return;
        }

        // 2. 权限校验：只有 "当前登录用户是作者" 或者 "超级管理员(ID=1)" 才能删除
        Long currentUserId = SecurityUtil.getUserId();
        if (!currentUserId.equals(1L) && !currentUserId.equals(post.getUserId())) {
            throw new BizException("您无权删除此帖子！");
        }

        // 3. 删除关联的评论 (防止数据库留脏数据)
        commentMapper.delete(new QueryWrapper<CeForumComment>().eq("post_id", postId));

        // 4. 删除帖子本身
        forumMapper.deleteById(postId);
    }

    public void deleteComment(Long commentId) {
        CeForumComment comment = commentMapper.selectById(commentId); // 需确保你注入了 commentMapper
        if (comment == null) {
            return;
        }

        Long currentUserId = SecurityUtil.getUserId();

        // 权限校验逻辑：
        // 1. 超级管理员 (ID=1)
        // 2. 评论本人 (comment.userId)
        // 3. 帖子楼主 (需查出帖子信息)
        boolean canDelete = false;

        if (currentUserId.equals(1L) || currentUserId.equals(comment.getUserId())) {
            canDelete = true;
        } else {
            // 查询帖子，看是否是楼主删除
            CeForumPost post = forumMapper.selectById(comment.getPostId());
            if (post != null && currentUserId.equals(post.getUserId())) {
                canDelete = true;
            }
        }

        if (!canDelete) {
            throw new BizException("您无权删除这条评论");
        }

        commentMapper.deleteById(commentId);
    }
}
