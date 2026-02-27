package com.mock.example.interfaces.controller.system;

import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.common.entity.Response;
import com.mock.example.interfaces.body.system.role.RoleBody;
import com.mock.example.interfaces.body.system.user.UserBody;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.interfaces.vo.system.role.RoleVo;
import com.mock.example.modules.system.service.SysRoleService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 角色管理
 *
 * @author: Mock
 * @date: 2025-01-05 16:40:29
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {

    private final SysRoleService roleService;

    /**
     * 请求角色列表
     *
     * @param roleBody 角色请求体
     * @return 角色列表
     */
    @ApiOperation(value = "请求角色列表")
    @GetMapping("/list")
    public TableDataInfo list(RoleBody roleBody) {
        startPage();
        return getDataTable(roleService.selectRoleList(roleBody));
    }

    /**
     * 通过角色id查询角色
     *
     * @param roleId 角色id
     * @return 角色
     */
    @ApiOperation(value = "通过角色id查询角色")
    @GetMapping(value = "/{roleId}")
    public Response<RoleVo> getInfo(@PathVariable Long roleId) {
        return new Response(roleService.selectRoleById(roleId));
    }

    /**
     * 新增角色
     *
     * @param roleBody 角色请求对象
     * @return 结果
     */
    @ApiOperation(value = "新增角色")
    @PostMapping
    public Response<Boolean> add(@RequestBody RoleBody roleBody) {
        return roleService.addRole(roleBody);
    }

    /**
     * 修改角色
     *
     * @param roleBody 角色请求对象
     * @return 结果
     */
    @ApiOperation(value = "修改角色")
    @PutMapping
    public Response<Boolean> edit(@RequestBody RoleBody roleBody) {
        return roleService.editRole(roleBody);
    }

    /**
     * 更改角色状态
     *
     * @param roleBody 角色请求对象
     * @return 结果
     */
    @ApiOperation(value = "更改角色状态")
    @PutMapping("/changeStatus")
    public Response<Boolean> changeStatus(@RequestBody RoleBody roleBody) {
        return new Response<>(roleService.updateRoleStatus(roleBody));
    }

    /**
     * 删除角色
     *
     * @param roleIds 角色id列表
     * @return 结果
     */
    @ApiOperation(value = "删除角色")
    @DeleteMapping("/{roleIds}")
    public Response<Boolean> remove(@PathVariable Long[] roleIds) {
        return new Response<>(roleService.deleteRoleByIds(roleIds));
    }

    /**
     * 查询已分配用户角色列表
     *
     * @param userBody 用户请求对象
     * @return 表格结果
     */
    @ApiOperation(value = "查询已分配用户角色列表")
    @GetMapping("/authUser/allocatedList")
    public TableDataInfo allocatedList(UserBody userBody) {
        startPage();
        return getDataTable(roleService.allocatedList(userBody));
    }

    /**
     * 查询未分配用户角色列表
     *
     * @param userBody 用户请求对象
     * @return 表格结果
     */
    @ApiOperation(value = "查询未分配用户角色列表")
    @GetMapping("/authUser/unallocatedList")
    public TableDataInfo unallocatedList(UserBody userBody) {
        startPage();
        return getDataTable(roleService.unallocatedList(userBody));
    }

    /**
     * 批量选择用户授权
     *
     * @param roleId  角色id
     * @param userIds 需授权的用户id
     * @return 结果
     */
    @ApiOperation(value = "批量选择用户授权")
    @PutMapping("/authUser/selectAll")
    public Response<Boolean> selectAuthUserAll(Long roleId, Long[] userIds) {
        return new Response<>(roleService.insertAuthUsers(roleId, userIds));
    }

    /**
     * 批量选择用户取消授权
     *
     * @param roleId 角色id
     * @param userIds 需删除授权的用户id
     * @return 结果
     */
    @ApiOperation(value = "批量选择用户取消授权")
    @PutMapping("/authUser/cancelAll")
    public Response<Boolean> cancelAuthUserAll(Long roleId, Long[] userIds) {
        return new Response<>(roleService.deleteAuthUsers(roleId, userIds));
    }

}

  