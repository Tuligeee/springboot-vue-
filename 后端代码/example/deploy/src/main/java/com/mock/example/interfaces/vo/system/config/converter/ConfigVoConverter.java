package com.mock.example.interfaces.vo.system.config.converter;

import com.mock.example.interfaces.vo.system.config.ConfigVo;
import com.mock.example.modules.system.entity.model.SysConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 配置对象装换器
 *
 * @author: Mock
 * @date: 2025-01-04 16:24:16
 */
@Mapper
public interface ConfigVoConverter {

    ConfigVoConverter INSTANT = Mappers.getMapper(ConfigVoConverter.class);

    /**
     * {@link SysConfig} -> {@link ConfigVo}
     */
    ConfigVo sysConfigToConfigVo(SysConfig sysConfig);
    List<ConfigVo> sysConfigToConfigVo(List<SysConfig> sysConfigs);

}

  