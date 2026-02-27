package com.mock.example.interfaces.vo.system.dict;

import lombok.Data;

import java.util.Date;

/**
 * 数字字典类型vo
 *
 * @author: Mock
 * @date: 2025-01-05 08:23:50
 */
@Data
public class DictTypeVo {

    /**
     * 字典主键
     */
    private Long dictId;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典类型
     */
    private String dictType;

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

  