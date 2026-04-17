package com.mock.example.interfaces.controller.system;

import com.mock.example.common.entity.Response;
import com.mock.example.common.entity.TreeSelect;
import com.mock.example.interfaces.body.system.dept.DeptBody;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.interfaces.vo.system.dept.DeptVo;
import com.mock.example.interfaces.vo.system.dept.RoleDeptTreeSelectVo;
import com.mock.example.modules.system.entity.model.SysDept;
import com.mock.example.modules.system.service.SysDeptService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户部门
 *
 * @author: Mock
 * @date: 2025-01-04 18:41:31
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {

    private final SysDeptService deptService;

    /**
     * 获取部门列表
     *
     * @param deptBody 部门请求对象
     * @return 部门列表
     */
    @ApiOperation(value = "获取部门列表")
    @GetMapping("/list")
    public Response<List<SysDept>> list(DeptBody deptBody) {
        return new Response<>(deptService.selectDeptList(deptBody));
    }

    /**
     * 查询部门列表（排除自己节点）
     *
     * @param deptId 部门id
     * @return 部门列表
     */
    @ApiOperation(value = "查询部门列表（排除自己节点）")
    @GetMapping("/list/exclude/{deptId}")
    public Response<List<DeptVo>> excludeChild(
            @PathVariable(value = "deptId", required = false) Long deptId
    ) {
        return new Response<>(deptService.excludeChild(deptId));
    }

    /**
     * 根据部门编号获取详细信息
     *
     * @param deptId 部门id
     * @return 部门信息
     */
    @ApiOperation(value = "根据部门编号获取详细信息")
    @GetMapping(value = "/{deptId}")
    public Response<DeptVo> getInfo(@PathVariable Long deptId) {
        return new Response<>(deptService.selectDeptById(deptId));
    }

    /**
     * 新增部门
     *
     * @param deptBody 部门请求对象
     * @return 结果
     */
    @ApiOperation(value = "新增部门")
    @PostMapping
    public Response<Boolean> add(@RequestBody DeptBody deptBody) {
        return deptService.addDept(deptBody);
    }

    /**
     * 修改部门
     *
     * @param deptBody 部门请求对象
     * @return 结果
     */
    @ApiOperation(value = "修改部门")
    @PutMapping
    public Response<Boolean> edit(@RequestBody DeptBody deptBody) {
        return deptService.editDept(deptBody);
    }

    /**
     * 删除部门
     *
     * @param deptId 部门id
     * @return 结果
     */
    @ApiOperation(value = "删除部门")
    @DeleteMapping("/{deptId}")
    public Response<Boolean> remove(@PathVariable Long deptId) {
        return deptService.deleteDept(deptId);
    }

    /**
     * 获取业务部门树
     *
     * @param deptBody 请求参数
     * @return 部门树
     */
    @ApiOperation(value = "获取业务部门树")
    @GetMapping("/treeselect")
    public Response<List<TreeSelect>> treeSelect(DeptBody deptBody) {
        return new Response<>(deptService.buildDeptTreeSelect(deptBody));
    }

    /**
     * 加载对应角色部门列表树
     *
     * @param roleId 角色id
     * @return 角色对应部门树
     */
    @ApiOperation(value = "加载对应角色部门列表树")
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    public Response<RoleDeptTreeSelectVo> roleDeptTreeSelect(@PathVariable("roleId") Long roleId) {
        return new Response<>(deptService.buildRoleDeptTreeSelect(roleId));
    }
}

  