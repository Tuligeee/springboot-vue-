package com.mock.example.interfaces.vo.system.dept.converter;

import com.mock.example.interfaces.vo.system.dept.DeptVo;
import com.mock.example.modules.system.entity.model.SysDept;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 部门对象装换器
 *
 * @author: Mock
 * @date: 2025-01-04 16:24:16
 */
@Mapper
public interface DeptVoConverter {

    DeptVoConverter INSTANT = Mappers.getMapper(DeptVoConverter.class);

    /**
     * {@link SysDept} -> {@link DeptVo}
     */
    DeptVo sysDeptToDeptVo(SysDept sysDept);
    List<DeptVo> sysDeptToDeptVo(List<SysDept> sysDepts);

}

  