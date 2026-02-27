package com.mock.example.interfaces.body.system.config;

import lombok.Data;

import java.util.Map;

/**
 * 参数配置body
 *
 * @author: Mock
 * @date: 2025-01-05 08:50:26
 */
@Data
public class ConfigBody {

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
     * 备注
     */
    private String remark;

    /**
     * 请求参数
     */
    private Map<String, Object> params;

}

  