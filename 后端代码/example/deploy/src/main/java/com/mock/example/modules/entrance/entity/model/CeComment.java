package com.mock.example.modules.entrance.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

/**
 * 通用评论表 ce_comment
 */
@Data
@TableName("ce_comment")
public class CeComment {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 目标ID (文章ID/学校ID) */
    private Long targetId;

    /** 类型 (1=资讯, 2=学校) */
    private String type;

    /** 内容 */
    private String content;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 冗余字段：用户昵称 */
    private transient String nickName;
    /** 冗余字段：用户头像 */
    private transient String avatar;
}
