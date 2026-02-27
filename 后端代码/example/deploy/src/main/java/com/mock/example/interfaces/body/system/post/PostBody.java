package com.mock.example.interfaces.body.system.post;

import lombok.Data;

import java.util.Date;

/**
 * 岗位请求对象
 *
 * @author: Mock
 * @date: 2025-01-11 13:38:01
 */
@Data
public class PostBody {

    /**
     * 岗位ID
     */
    private Long postId;

    /**
     * 岗位编码
     */
    private String postCode;

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 显示顺序
     */
    private Integer postSort;

    /**
     * 状态（0正常 1停用）
     */
    private String status;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 备注
     */
    private String remark;
}

  