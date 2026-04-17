package com.mock.example.modules.system.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mock.example.common.utils.StringUtil;
import com.mock.example.modules.system.entity.model.SysConfig;
import com.mock.example.modules.system.mapper.SysConfigMapper;
import com.mock.example.modules.system.repository.ISysConfigRepo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 参数配置表 仓库实现类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2025-01-05
 */
@Repository
@RequiredArgsConstructor
public class SysConfigRepoImpl
        extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigRepo {

    private final SysConfigMapper mapper;

    @Override
    public SysConfig selectConfig(SysConfig config) {
        return mapper.selectConfig(config);
    }

    @Override
    public SysConfig selectConfigByKey(String configKey) {
        return this.getOne(
                Wrappers.<SysConfig>lambdaQuery().eq(SysConfig::getConfigKey, configKey)
                        .last("limit 1")
        );
    }

    @Override
    public List<SysConfig> selectConfigList(SysConfig config) {
        return mapper.selectConfigList(config);
    }

    @Override
    public Boolean checkConfigKeyUnique(SysConfig config) {
        Long configId = StringUtil.isNull(config.getConfigId()) ? -1L : config.getConfigId();
        SysConfig sysConfig = this.getOne(
                Wrappers.<SysConfig>lambdaQuery()
                        .eq(SysConfig::getConfigKey, configId)
        );
        return BooleanUtils.isTrue(
                sysConfig == null || sysConfig.getConfigId().longValue() == configId.longValue()
        );
    }

}
