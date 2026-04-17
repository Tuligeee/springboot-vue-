package com.mock.example.modules.system.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mock.example.modules.system.entity.model.SysDictType;

import java.util.List;

/**
 * <p>
 * 字典类型表 仓库服务类
 * </p>
 *
 * @author Mybatis Auto
 * @since 2025-01-05
 */
public interface ISysDictTypeRepo extends IService<SysDictType> {

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    List<SysDictType> selectDictTypeList(SysDictType dictType);

    /**
     * 根据字典id查询字典信息
     *
     * @param dictId 字典id
     * @return 字典类型
     */
    SysDictType selectDictTypeById(Long dictId);

    /**
     * 检查字典类型是否唯一
     *
     * @param dictType 字典类型
     * @return true 是唯一
     */
    Boolean checkDictTypeUnique(SysDictType dictType);
}
