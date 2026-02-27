package com.mock.example.interfaces.vo.system.menu.converter;

import com.mock.example.interfaces.vo.system.menu.MenuVo;
import com.mock.example.modules.system.entity.model.SysMenu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 菜单对象装换器
 *
 * @author: Mock
 * @date: 2025-01-04 16:24:16
 */
@Mapper(imports = {String.class})
public interface MenuVoConverter {

    MenuVoConverter INSTANT = Mappers.getMapper(MenuVoConverter.class);

    /**
     * {@link SysMenu} -> {@link MenuVo}
     */
    @Mapping(target = "isFrame", expression = "java(String.valueOf(sysMenu.getIsFrame()))")
    @Mapping(target = "isCache", expression = "java(String.valueOf(sysMenu.getIsCache()))")
    MenuVo sysMenuToMenuVo(SysMenu sysMenu);
    List<MenuVo> sysMenuToMenuVo(List<SysMenu> sysMenus);

}

  