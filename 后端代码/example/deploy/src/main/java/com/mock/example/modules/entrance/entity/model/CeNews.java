package com.mock.example.modules.entrance.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
@TableName("ce_news")
public class CeNews {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String title;
    private String coverImg;
    private String summary;
    private String content;
    private String type; // 1-政策 2-指南 3-动态
    private Integer viewCount;
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
