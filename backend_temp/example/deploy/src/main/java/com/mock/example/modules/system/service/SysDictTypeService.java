package com.mock.example.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.google.common.collect.Lists;
import com.mock.example.common.entity.Response;
import com.mock.example.common.exception.BizException;
import com.mock.example.common.utils.DictUtil;
import com.mock.example.common.utils.EntityCopyUtil;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.interfaces.body.system.dict.DictTypeBody;
import com.mock.example.interfaces.vo.system.dict.DictTypeVo;
import com.mock.example.modules.system.entity.model.SysDictData;
import com.mock.example.modules.system.entity.model.SysDictType;
import com.mock.example.modules.system.repository.ISysDictDataRepo;
import com.mock.example.modules.system.repository.ISysDictTypeRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数字字典类型业务层
 *
 * @author: Mock
 * @date: 2025-01-05 08:20:48
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDictTypeService {

    private final ISysDictTypeRepo dictTypeRepository;

    private final ISysDictDataRepo dictDataRepository;

    /**
     * 获取字典列表
     *
     * @param dictTypeBody 字典类型请求对象
     * @return 字典列表
     */
    public List<SysDictType> selectDictTypeList(DictTypeBody dictTypeBody) {
        SysDictType sysDictType = EntityCopyUtil.copyEntity(SysDictType.class, dictTypeBody);
        return dictTypeRepository.selectDictTypeList(sysDictType);

    }

    /**
     * 根据字典id查询字典信息
     *
     * @param dictId 字典id
     * @return 字典类型
     */
    public DictTypeVo selectDictTypeById(Long dictId) {
        SysDictType sysDictType = dictTypeRepository.selectDictTypeById(dictId);
        return EntityCopyUtil.copyEntity(DictTypeVo.class, sysDictType);
    }

    /**
     * 新增字典类型
     *
     * @param dictTypeBody 字典类型请求对象
     * @return 结果
     */
    public Response addDictType(DictTypeBody dictTypeBody) {
        SysDictType sysDictType = EntityCopyUtil.copyEntity(SysDictType.class, dictTypeBody);
        if (!dictTypeRepository.checkDictTypeUnique(sysDictType)) {
            return new Response().failMsg("新增字典'" + dictTypeBody.getDictName() + "'失败，字典类型已存在");
        }
        sysDictType.setCreateBy(SecurityUtil.getUsername());
        sysDictType.setCreateTime(DateUtil.date());

        dictTypeRepository.save(sysDictType);
        DictUtil.setDictCache(sysDictType.getDictType(), Lists.newArrayList());

        return new Response<>(Boolean.TRUE);
    }

    /**
     * 修改字典类型
     *
     * @param dictTypeBody 字典类型请求对象
     * @return 结果
     */
    public Response editDictType(DictTypeBody dictTypeBody) {
        SysDictType sysDictType = EntityCopyUtil.copyEntity(SysDictType.class, dictTypeBody);
        if (!dictTypeRepository.checkDictTypeUnique(sysDictType)) {
            return new Response().failMsg("修改字典'" + dictTypeBody.getDictName() + "'失败，字典类型已存在");
        }
        sysDictType.setUpdateBy(SecurityUtil.getUsername());
        sysDictType.setUpdateTime(DateUtil.date());

        dictTypeRepository.updateById(sysDictType);
        DictUtil.setDictCache(sysDictType.getDictType(), Lists.newArrayList());

        return new Response<>(Boolean.TRUE);
    }

    /**
     * 删除字典类型
     *
     * @param dictIds 字典id
     * @return 结果
     */
    public Response deleteDictTypeByIds(Long[] dictIds) {
        for (Long dictId : dictIds) {
            SysDictType dictType = dictTypeRepository.getById(dictId);
            if (CollUtil.isNotEmpty(dictDataRepository.selectDictDataByType(dictType.getDictType()))) {
                throw new BizException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }

            dictTypeRepository.removeById(dictId);
            DictUtil.removeDictCache(dictType.getDictType());
        }

        return new Response().ok();
    }

    /**
     * 重置字典缓存
     */
    public void resetDictCache() {
        DictUtil.clearDictCache();
        List<SysDictType> dictTypeList = dictTypeRepository.list();
        for (SysDictType dictType : dictTypeList) {
            List<SysDictData> dictDatas = dictDataRepository.selectDictDataByType(dictType.getDictType());
            DictUtil.setDictCache(dictType.getDictType(), dictDatas);
        }
    }
}

  