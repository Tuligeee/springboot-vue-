package com.mock.example.modules.entrance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mock.example.modules.entrance.entity.model.CeForumComment;
import com.mock.example.modules.entrance.entity.model.CeForumPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface CeForumMapper extends BaseMapper<CeForumPost> {

    // 查询帖子列表（关联查出用户信息）
    @Select("SELECT p.*, u.nick_name, u.avatar FROM ce_forum_post p " +
            "LEFT JOIN sys_user u ON p.user_id = u.user_id " +
            "WHERE p.del_flag = '0' ORDER BY p.create_time DESC")
    List<CeForumPost> selectPostList();

    // 查询某个帖子的所有评论
    @Select("SELECT c.*, u.nick_name, u.avatar FROM ce_forum_comment c " +
            "LEFT JOIN sys_user u ON c.user_id = u.user_id " +
            "WHERE c.post_id = #{postId} ORDER BY c.create_time ASC")
    List<CeForumComment> selectCommentsByPostId(Long postId);
}
