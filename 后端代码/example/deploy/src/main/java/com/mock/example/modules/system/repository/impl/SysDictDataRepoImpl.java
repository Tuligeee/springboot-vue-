package com.mock.example.modules.system.repository.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mock.example.modules.system.entity.model.SysDictData;
import com.mock.example.modules.system.mapper.SysDictDataMapper;
import com.mock.example.modules.system.repository.ISysDictDataRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 字典数据表 仓库实现类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2025-01-05
 */
@Repository
@RequiredArgsConstructor
public class SysDictDataRepoImpl
        extends ServiceImpl<SysDictDataMapper, SysDictData> implements ISysDictDataRepo {

    private final SysDictDataMapper mapper;

    /**
     * 状态（0正常 1停用）
     */
    private final static int NORMAL_STATUS = 0;
    private final static int STOP_STATUS = 1;

    @Override
    public List<SysDictData> selectDictDataList(SysDictData dictData) {
        return mapper.selectDictDataList(dictData);
    }

    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        return this.list(
                Wrappers.<SysDictData>lambdaQuery()
                        .eq(SysDictData::getDictType, dictType)
                        .eq(SysDictData::getStatus, NORMAL_STATUS)
                        .orderByAsc(SysDictData::getDictSort)
        );

    }
}
