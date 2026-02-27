package com.mock.example.interfaces.vo.system.user.converter;

import com.github.pagehelper.Page;
import com.mock.example.interfaces.vo.system.user.UserVo;
import com.mock.example.modules.system.entity.model.SysUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 用户对象装换器
 *
 * @author: Mock
 * @date: 2025-01-04 16:24:16
 */
@Mapper
public interface UserVoConverter {

    UserVoConverter INSTANT = Mappers.getMapper(UserVoConverter.class);

    /**
     * {@link SysUser} -> {@link UserVo}
     */
    UserVo sysUserToUserVo(SysUser sysUser);
    Page<UserVo> sysUserToUserVo(List<SysUser> sysUser);

}

  