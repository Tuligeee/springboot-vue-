package com.mock.example.interfaces.vo.system.post;

import lombok.Data;

import java.util.Date;

/**
 * 岗位Vo
 *
 * @author: Mock
 * @date: 2025-01-04 20:06:56
 */
@Data
public class PostVo {

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

  