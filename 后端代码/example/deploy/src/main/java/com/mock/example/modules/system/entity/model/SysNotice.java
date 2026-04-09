package com.mock.example.modules.system.entity.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("sys_notice")
public class SysNotice implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "notice_id", type = IdType.AUTO)
    private Long noticeId;

    private String noticeTitle;
    private String noticeType;
    private String noticeContent;
    private String status;
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String remark;
}
