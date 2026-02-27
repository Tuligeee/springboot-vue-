package com.mock.example.modules.entrance.entity.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
@TableName("ce_forum_comment")
public class CeForumComment {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long postId;
    private Long userId;
    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField(exist = false)
    private String nickName; // 评论人昵称

    @TableField(exist = false)
    private String avatar;   // 评论人头像
}
