package com.mock.example.interfaces.vo.system.config;

import lombok.Data;

import java.util.Date;

/**
 * 参数配置vo
 *
 * @author: Mock
 * @date: 2025-01-05 08:50:26
 */
@Data
public class ConfigVo {

    /**
     * 参数主键
     */
    private Integer configId;

    /**
     * 参数名称
     */
    private String configName;

    /**
     * 参数键名
     */
    private String configKey;

    /**
     * 参数键值
     */
    private String configValue;

    /**
     * 系统内置（Y是 N否）
     */
    private String configType;

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

  