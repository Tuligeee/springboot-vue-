package com.mock.example.modules.system.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.mock.example.common.consts.UserConstants;
import com.mock.example.common.entity.Response;
import com.mock.example.common.exception.BizException;
import com.mock.example.common.utils.EntityCopyUtil;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.common.utils.StringUtil;
import com.mock.example.interfaces.body.system.config.ConfigBody;
import com.mock.example.interfaces.vo.system.config.ConfigVo;
import com.mock.example.interfaces.vo.system.config.converter.ConfigVoConverter;
import com.mock.example.modules.system.entity.model.SysConfig;
import com.mock.example.modules.system.repository.ISysConfigRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 参数配置业务层
 *
 * @author: Mock
 * @date: 2025-01-05 08:44:56
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysConfigService {

    private final ISysConfigRepo configRepository;

    /**
     * 根据键名查询参数信息
     *
     * @param configKey 键key
     * @return 键值
     */
    public String selectConfigByKey(String configKey) {
        SysConfig config = configRepository.selectConfigByKey(configKey);
        if (config == null) {
            log.warn("获取不到键key {} 的配置", configKey);
            return StrUtil.EMPTY;
        }

        return config.getConfigValue();
    }

    /**
     * 获取参数配置列表
     *
     * @param configBody 配置信息
     * @return 配置列表
     */
    public List<SysConfig> selectConfigList(ConfigBody configBody) {
        SysConfig sysConfig = EntityCopyUtil.copyEntity(SysConfig.class, configBody);
        return configRepository.selectConfigList(sysConfig);
    }

    /**
     * 新增参数配置
     *
     * @param configId 配置id
     * @return 配置信息
     */
    public ConfigVo selectConfigById(Integer configId) {
        SysConfig sysConfig = new SysConfig();
        sysConfig.setConfigId(configId);

        return ConfigVoConverter.INSTANT.sysConfigToConfigVo(
                configRepository.selectConfig(sysConfig)
        );
    }

    /**
     * 新增参数配置
     *
     * @param configBody 配置信息
     * @return 结果
     */
    public Response addConfig(ConfigBody configBody) {
        SysConfig sysConfig = EntityCopyUtil.copyEntity(SysConfig.class, configBody);

        if (!configRepository.checkConfigKeyUnique(sysConfig)) {
            return new Response().failMsg("新增参数'" + sysConfig.getConfigName() + "'失败，参数键名已存在");
        }
        sysConfig.setCreateBy(SecurityUtil.getUsername());
        sysConfig.setCreateTime(DateUtil.date());
        configRepository.save(sysConfig);
        //todo 更新缓存

        return new Response().ok();
    }

    /**
     * 修改参数配置
     *
     * @param configBody 配置信息
     * @return 结果
     */
    public Response editConfig(ConfigBody configBody) {
        SysConfig sysConfig = EntityCopyUtil.copyEntity(SysConfig.class, configBody);

        if (!configRepository.checkConfigKeyUnique(sysConfig)) {
            return new Response().failMsg("修改参数'" + sysConfig.getConfigName() + "'失败，参数键名已存在");
        }
        sysConfig.setUpdateBy(SecurityUtil.getUsername());
        sysConfig.setUpdateTime(DateUtil.date());
        configRepository.updateById(sysConfig);
        //todo 更新缓存

        return new Response().ok();
    }

    /**
     * 删除参数配置
     *
     * @param configIds 配置ID列表
     * @return 结果
     */
    public Response deleteConfigs(Long[] configIds) {
        for (Long configId : configIds) {
            SysConfig config = configRepository.getById(configId);
            if (StringUtil.equals(UserConstants.YES, config.getConfigType())) {
                throw new BizException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
            configRepository.removeById(configId);
            //todo 更新缓存redisCache.deleteObject(getCacheKey(config.getConfigKey()));
        }
        return new Response().ok();
    }
}

  