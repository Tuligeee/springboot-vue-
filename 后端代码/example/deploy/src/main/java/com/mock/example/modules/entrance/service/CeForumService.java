package com.mock.example.modules.entrance.service;

import com.mock.example.common.utils.SecurityUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mock.example.common.exception.BizException;
import com.mock.example.modules.entrance.entity.model.CeForumComment;
import com.mock.example.modules.entrance.entity.model.CeForumPost;
import com.mock.example.modules.entrance.mapper.CeForumMapper;
import com.mock.example.modules.system.mapper.SysUserMapper; // 引入用户Mapper
import com.mock.example.common.config.security.PermissionService;
import com.mock.example.common.utils.SensitiveWordUtil;
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
    private final PermissionService permissionService;

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
        checkSensitive(post.getTitle(), "标题");
        checkSensitive(post.getContent(), "内容");

        post.setUserId(SecurityUtil.getUserId());
        post.setCreateTime(new Date());
        post.setViewCount(0);
        post.setLikeCount(0);
        forumMapper.insert(post);
    }

    // 更新帖子
    public void updatePost(CeForumPost post) {
        checkSensitive(post.getTitle(), "标题");
        checkSensitive(post.getContent(), "内容");

        CeForumPost oldPost = forumMapper.selectById(post.getId());
        if (oldPost == null) {
            return;
        }
        Long currentUserId = SecurityUtil.getUserId();
        if (!currentUserId.equals(1L) && !currentUserId.equals(oldPost.getUserId())) {
            throw new BizException("您无权修改此帖子！");
        }
        oldPost.setTitle(post.getTitle());
        oldPost.setContent(post.getContent());
        forumMapper.updateById(oldPost);
    }

    // 4. 获取评论列表
    public List<CeForumComment> getComments(Long postId) {
        return forumMapper.selectCommentsByPostId(postId);
    }

    // 5. 发布评论
    public void publishComment(CeForumComment comment) {
        checkSensitive(comment.getContent(), "评论内容");

        comment.setUserId(SecurityUtil.getUserId());
        comment.setCreateTime(new Date());
        commentMapper.insert(comment);
    }

    /** 统一封装的敏感词核验器 */
    private void checkSensitive(String text, String fieldType) {
        String hitWord = SensitiveWordUtil.getFirstMatchedWord(text);
        if (hitWord != null) {
            throw new BizException("发布失败！提交的" + fieldType + "中包含违规词汇，请修改后重试。");
        }
    }

    public void deletePost(Long postId) {
        // 1. 查出帖子信息
        CeForumPost post = forumMapper.selectById(postId);
        if (post == null) {
            return;
        }

        // 2. 权限校验：只有 "当前登录用户是作者" 或者 "具有entrance:forum:remove权限" 才能删除
        Long currentUserId = SecurityUtil.getUserId();
        boolean hasAdminPerm = permissionService.hasPermi("entrance:forum:remove");
        if (!hasAdminPerm && !currentUserId.equals(post.getUserId())) {
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
        // 1. 后台具有论坛管理权限的管理员
        // 2. 评论本人 (comment.userId)
        // 3. 帖子楼主 (帖子拥有者)
        boolean canDelete = false;

        boolean hasAdminPerm = permissionService.hasPermi("entrance:forum:remove");
        if (hasAdminPerm || currentUserId.equals(comment.getUserId())) {
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
