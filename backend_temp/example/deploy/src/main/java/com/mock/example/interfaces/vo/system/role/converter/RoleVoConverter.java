package com.mock.example.interfaces.vo.system.role.converter;

import com.mock.example.interfaces.vo.system.role.RoleVo;
import com.mock.example.modules.system.entity.model.SysRole;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 角色对象装换器
 *
 * @author: Mock
 * @date: 2025-01-04 16:24:16
 */
@Mapper
public interface RoleVoConverter {

    RoleVoConverter INSTANT = Mappers.getMapper(RoleVoConverter.class);

    /**
     * {@link SysRole} -> {@link RoleVo}
     */
    RoleVo sysRoleToRoleVo(SysRole sysRole);
    List<RoleVo> sysRoleToRoleVo(List<SysRole> sysRoles);

}

  