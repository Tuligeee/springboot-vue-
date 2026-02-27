package com.mock.example.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.mock.example.common.utils.DictUtil;
import com.mock.example.common.utils.EntityCopyUtil;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.interfaces.body.system.dict.DictDataBody;
import com.mock.example.interfaces.vo.system.dict.DictDataVo;
import com.mock.example.interfaces.vo.system.dict.converter.DictVoConverter;
import com.mock.example.modules.system.entity.model.SysDictData;
import com.mock.example.modules.system.repository.ISysDictDataRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数字字典信息业务层
 *
 * @author: Mock
 * @date: 2025-01-05 09:16:08
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDictDataService {

    private final ISysDictDataRepo dictDataRepository;

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictDataBody 字典数据对象请求体
     * @return 字典类别
     */
    public List<SysDictData> selectDictDataList(DictDataBody dictDataBody) {
        return dictDataRepository.selectDictDataList(EntityCopyUtil.copyEntity(SysDictData.class, dictDataBody));
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    public List<DictDataVo> selectDictDataByType(String dictType) {
        List<SysDictData> sysDictDataList = dictDataRepository.selectDictDataByType(dictType);
        if (CollUtil.isEmpty(sysDictDataList)) {
            return Lists.newArrayList();
        }
        return DictVoConverter.INSTANT.sysDictDataToDictDataVo(sysDictDataList);
    }

    /**
     * 查询字典数据详细
     *
     * @param dictCode 字典code
     * @return 字典详情
     */
    public DictDataVo selectDictDataById(Long dictCode) {
        SysDictData dictData = dictDataRepository.getById(dictCode);
        return EntityCopyUtil.copyEntity(DictDataVo.class, dictData);
    }

    /**
     * 新增字典类型
     *
     * @param dictDataBody 字典请求对象
     * @return 结果
     */
    public Boolean insertDictData(DictDataBody dictDataBody) {
        SysDictData sysDictData = EntityCopyUtil.copyEntity(SysDictData.class, dictDataBody);
        sysDictData.setCreateBy(SecurityUtil.getUsername());
        sysDictData.setCreateTime(DateUtil.date());

        dictDataRepository.save(sysDictData);

        // 更新缓存
        updateCache(sysDictData);

        return Boolean.TRUE;
    }

    /**
     * 修改保存字典类型
     *
     * @param dictDataBody 字典请求对象
     * @return 结果
     */
    public Boolean updateDictData(DictDataBody dictDataBody) {
        SysDictData sysDictData = EntityCopyUtil.copyEntity(SysDictData.class, dictDataBody);
        sysDictData.setUpdateBy(SecurityUtil.getUsername());
        sysDictData.setUpdateTime(DateUtil.date());

        dictDataRepository.updateById(sysDictData);

        // 更新缓存
        updateCache(sysDictData);

        return Boolean.TRUE;
    }

    /**
     * 删除字典
     *
     * @param dictCodes 字典code集合
     * @return 结果
     */
    public Boolean deleteDictDatas(Long[] dictCodes) {
        for (Long dictCode : dictCodes) {
            SysDictData data = dictDataRepository.getById(dictCode);
            dictDataRepository.removeById(dictCode);

            List<SysDictData> dictDatas = dictDataRepository.selectDictDataByType(data.getDictType());
            DictUtil.setDictCache(data.getDictType(), dictDatas);
        }

        return Boolean.TRUE;
    }

    /**
     * 更新缓存
     *
     * @param sysDictData 字典类型
     */
    private void updateCache(SysDictData sysDictData) {
        List<SysDictData> dictDatas = dictDataRepository.selectDictDataByType(sysDictData.getDictType());
        DictUtil.setDictCache(sysDictData.getDictType(), dictDatas);
    }

}

  