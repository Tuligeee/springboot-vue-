package com.mock.example.modules.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mock.example.modules.system.entity.model.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 菜单权限表 Mapper 接口
 * </p>
 *
 * @author Mybatis Auto
 * @since 2023-02-26
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
     List<SysMenu> selectMenuList(SysMenu menu);

    /**
     * 根据用户查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    public List<SysMenu> selectMenuListByUserId(SysMenu menu);

    /**
     * 根据用户id查询权限列表
     *
     * @param userId 用户id
     * @return 权限列表
     */
    List<String> selectMenuPermsByUserId(Long userId);

    /**
     * 根据用户ID查询菜单
     *
     * @return 菜单列表
     */
    List<SysMenu> selectMenuTreeAll();

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<SysMenu> selectMenuTreeByUserId(Long userId);

    /**
     * 根据角色id查询菜单id列表
     *
     * @param roleId 角色id
     * @return 菜单id列表
     */
    List<Integer> selectMenuListByRoleId(Long roleId);

}
