package com.mock.example.modules.system.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mock.example.common.utils.StringUtil;
import com.mock.example.modules.system.entity.model.SysDictType;
import com.mock.example.modules.system.mapper.SysDictTypeMapper;
import com.mock.example.modules.system.repository.ISysDictTypeRepo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 字典类型表 仓库实现类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2025-01-05
 */
@Repository
@RequiredArgsConstructor
public class SysDictTypeRepoImpl
        extends ServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeRepo {

    private final SysDictTypeMapper mapper;

    @Override
    public List<SysDictType> selectDictTypeList(SysDictType dictType) {
        return mapper.selectDictTypeList(dictType);
    }

    @Override
    public SysDictType selectDictTypeById(Long dictId) {
        return this.getOne(
                Wrappers.<SysDictType>lambdaQuery().eq(SysDictType::getDictId, dictId)
                        .last(" limit 1")
        );
    }

    @Override
    public Boolean checkDictTypeUnique(SysDictType dictType) {
        Long dictId = StringUtil.isNull(dictType.getDictId()) ? -1L : dictType.getDictId();
        SysDictType sysDictType = this.getOne(
                Wrappers.<SysDictType>lambdaQuery()
                        .eq(SysDictType::getDictType, dictType.getDictType())
                        .last("limit 1")
        );

        return BooleanUtils.isTrue(
                sysDictType == null
                        || sysDictType.getDictId().longValue() == dictId.longValue()
        );
    }
}
