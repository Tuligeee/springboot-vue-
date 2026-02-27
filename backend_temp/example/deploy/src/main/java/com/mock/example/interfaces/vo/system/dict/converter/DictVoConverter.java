package com.mock.example.interfaces.vo.system.dict.converter;

import com.mock.example.interfaces.vo.system.dict.DictDataVo;
import com.mock.example.interfaces.vo.system.dict.DictTypeVo;
import com.mock.example.modules.system.entity.model.SysDictData;
import com.mock.example.modules.system.entity.model.SysDictType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 字典对象装换器
 *
 * @author: Mock
 * @date: 2025-01-04 16:24:16
 */
@Mapper
public interface DictVoConverter {

    DictVoConverter INSTANT = Mappers.getMapper(DictVoConverter.class);

    /**
     * {@link SysDictData} -> {@link DictDataVo}
     */
    DictDataVo sysDictDataToDictDataVo(SysDictData sysDictData);
    List<DictDataVo> sysDictDataToDictDataVo(List<SysDictData> sysDictDatas);

    /**
     * {@link sysDictType} -> {@link DictTypeVo}
     */
    DictTypeVo sysDictTypeToDictDataVo(SysDictType sysDictType);
    List<DictTypeVo> sysDictTypeToDictDataVo(List<SysDictType> sysDictTypes);

}

  