package com.mock.example.modules.system.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mock.example.modules.system.entity.model.SysConfig;

import java.util.List;

/**
 * <p>
 * 参数配置表 仓库类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2025-01-05
 */
public interface ISysConfigRepo extends IService<SysConfig> {

    /**
     * 查询参数配置信息
     *
     * @param config 参数配置信息
     * @return 参数配置信息
     */
    SysConfig selectConfig(SysConfig config);

    /**
     * 根据键名查询参数信息
     *
     * @param configKey 键值
     * @return 配置
     */
    SysConfig selectConfigByKey(String configKey);

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    List<SysConfig> selectConfigList(SysConfig config);

    /**
     * 检查key是否唯一
     *
     * @param config 配置
     * @return 结果 true 是唯一
     */
    Boolean checkConfigKeyUnique(SysConfig config);

}
