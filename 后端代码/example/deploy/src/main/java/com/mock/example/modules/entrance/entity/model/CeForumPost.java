package com.mock.example.modules.entrance.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
@TableName("ce_forum_post")
public class CeForumPost {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String title;
    private String content;
    private Long userId;
    private Integer viewCount;
    private Integer likeCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    // 非数据库字段，用于前端显示发帖人昵称
    @TableField(exist = false)
    private String nickName;

    // 非数据库字段，用于前端显示发帖人头像
    @TableField(exist = false)
    private String avatar;
}
