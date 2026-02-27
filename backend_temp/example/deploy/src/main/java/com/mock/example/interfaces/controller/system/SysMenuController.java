package com.mock.example.interfaces.controller.system;

import com.mock.example.common.entity.Response;
import com.mock.example.common.entity.TreeSelect;
import com.mock.example.interfaces.body.system.menu.MenuBody;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.interfaces.vo.system.menu.MenuVo;
import com.mock.example.interfaces.vo.system.menu.RoleMenuTreeSelectVo;
import com.mock.example.modules.system.entity.model.SysMenu;
import com.mock.example.modules.system.service.SysMenuService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理
 *
 * @author: Mock
 * @date: 2025-01-05 18:55:31
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {

    private final SysMenuService menuService;

    /**
     * 获取菜单列表
     *
     * @param menuBody 菜单请求对象
     * @return 菜单列表
     */
    @ApiOperation(value = "获取菜单列表")
    @GetMapping("/list")
    public Response<List<SysMenu>> list(MenuBody menuBody) {
        return new Response<>(menuService.selectMenuList(menuBody));
    }

    /**
     * 根据菜单编号获取详细信息
     *
     * @param menuId 菜单id
     * @return 菜单信息
     */
    @ApiOperation(value = "根据菜单编号获取详细信息")
    @GetMapping(value = "/{menuId}")
    public Response<MenuVo> getInfo(@PathVariable Long menuId) {
        return new Response<>(menuService.selectMenuById(menuId));
    }

    /**
     * 新增菜单
     *
     * @param menuBody 菜单请求对象
     * @return 结果
     */
    @ApiOperation(value = "新增菜单")
    @PostMapping
    public Response add(@RequestBody MenuBody menuBody) {
        return menuService.addMenu(menuBody);
    }

    /**
     * 修改菜单
     *
     * @param menuBody 菜单请求对象
     * @return 结果
     */
    @ApiOperation(value = "修改菜单")
    @PutMapping
    public Response edit(@RequestBody MenuBody menuBody) {
        return menuService.editMenu(menuBody);
    }

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     * @return 结果
     */
    @ApiOperation(value = "删除菜单")
    @DeleteMapping("/{menuId}")
    public Response remove(@PathVariable("menuId") Long menuId) {
        return menuService.deleteMenu(menuId);
    }

    /**
     * 获取菜单下拉树列表
     *
     * @param menuBody 菜单请求对象
     * @return 菜单下拉树
     */
    @ApiOperation(value = "获取菜单下拉树列表")
    @GetMapping("/treeselect")
    public Response<TreeSelect> treeSelect(MenuBody menuBody) {
        return new Response(menuService.buildMenuTreeSelect(menuBody));
    }

    /**
     * 加载对应角色菜单列表树
     *
     * @param roleId 角色id
     * @return 角色菜单下拉树
     */
    @ApiOperation(value = "加载对应角色菜单列表树")
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public Response<RoleMenuTreeSelectVo> roleMenuTreeSelect(@PathVariable("roleId") Long roleId) {
        return new Response<>(menuService.buildRoleMenuTreeSelect(roleId));
    }

}

  