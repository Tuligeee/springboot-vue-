package com.mock.example.interfaces.controller.system;

import com.mock.example.common.component.page.TableDataInfo;
import com.mock.example.common.entity.Response;
import com.mock.example.common.utils.ExcelUtil;
import com.mock.example.common.utils.StringUtil;
import com.mock.example.interfaces.body.system.user.UserBody;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.interfaces.vo.system.post.PostVo;
import com.mock.example.interfaces.vo.system.role.RoleVo;
import com.mock.example.interfaces.vo.system.user.UserDetailVo;
import com.mock.example.modules.system.entity.model.SysUser;
import com.mock.example.modules.system.service.SysConfigService;
import com.mock.example.modules.system.service.SysPostService;
import com.mock.example.modules.system.service.SysRoleService;
import com.mock.example.modules.system.service.SysUserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户管理
 *
 * @author: Mock
 * @date: 2023-02-26 19:30:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/user")
public class SysUserController extends BaseController {

    private final SysUserService userService;

    private final SysRoleService roleService;

    private final SysPostService postService;

    private final SysConfigService configService;

    /**
     * 获取用户列表
     *
     * @param userBody 用户请求对象
     * @return 用户列表表格对象
     */
    @ApiOperation(value = "获取用户列表")
    @GetMapping("/list")
    public TableDataInfo list(UserBody userBody) {
        startPage();
        TableDataInfo tableDataInfo = getDataTable(userService.listUser(userBody));
        return tableDataInfo;
    }

    /**
     * 根据用户编号获取详细信息
     *
     * @param userId 用户id
     * @return 用户信息
     */
    @ApiOperation(value = "根据用户编号获取详细信息")
    @GetMapping(value = {"/", "/{userId}"})
    public Response<UserDetailVo> getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        UserDetailVo userDetailVo = new UserDetailVo();

        List<RoleVo> roles = roleService.selectRoleAll();
        List<PostVo> posts = postService.selectPostAll();
        userDetailVo.setRoles(SysUser.isAdmin(userId) ? roles :
                roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        userDetailVo.setPosts(posts);

        if (StringUtil.isNotNull(userId)) {
            userDetailVo.setUser(userService.selectByUserId(userId));
            userDetailVo.setPostIds(postService.selectPostListByUserId(userId));
            userDetailVo.setRoleIds(roleService.selectRoleListByUserId(userId));
        }

        return new Response(userDetailVo);
    }

    /**
     * 添加用户
     *
     * @param userBody 用户请求对象
     * @return 添加是否成功
     */
    @ApiOperation(value = "添加用户")
    @PostMapping
    public Response<Boolean> add(@RequestBody UserBody userBody) {
        return userService.addUser(userBody);
    }

    /**
     * 修改用户
     *
     * @param userBody 用户请求对象
     * @return 修改是否成功
     */
    @ApiOperation(value = "修改用户")
    @PutMapping
    public Response<Boolean> edit(@RequestBody UserBody userBody) {
        return userService.updateUser(userBody);
    }

    /**
     * 删除用户
     *
     * @param userIds 用户id
     * @return 删除是否成功
     */
    @ApiOperation(value = "删除用户")
    @DeleteMapping("/{userIds}")
    public Response<Boolean> remove(@PathVariable Long[] userIds) {
        return userService.deleteUser(userIds);
    }

    /**
     * 用户状态修改
     *
     * @param userBody 用户请求对象
     * @return 结果
     */
    @ApiOperation(value = "用户状态修改")
    @PutMapping("/changeStatus")
    public Response<Boolean> changeStatus(@RequestBody UserBody userBody) {
        return new Response<>(userService.updateUserStatus(userBody));
    }

    /**
     * 重置密码
     *
     * @param userBody 用户请求体
     * @return 结果
     */
    @ApiOperation(value = "重置密码")
    @PutMapping("/resetPwd")
    public Response<Boolean> resetPwd(@RequestBody UserBody userBody) {
        return new Response<>(userService.resetPwd(userBody.getUserId(), userBody.getPassword()));
    }

    /**
     * 根据用户编号获取授权角色
     *
     * @param userId 用户id
     * @return 用户和角色信息
     */
    @ApiOperation(value = "根据用户编号获取授权角色")
    @GetMapping("/authRole/{userId}")
    public Response<UserDetailVo> authRole(@PathVariable("userId") Long userId) {
        return new Response<>(userService.authRole(userId));
    }

    /**
     * 用户授权角色
     *
     * @param userId  用户id
     * @param roleIds 角色id列表
     * @return 结果
     */
    @ApiOperation(value = "用户授权角色")
    @PutMapping("/authRole")
    public Response<Boolean> insertAuthRole(Long userId, Long[] roleIds) {
        return new Response<>(userService.insertAuthRole(userId, roleIds));
    }

    /**
     * 用户数据导出
     *
     * @param userBody 用户请求对象
     * @return 导出结果
     */
    @ApiOperation(value = "用户数据导出")
    @GetMapping("/export")
    public Response export(UserBody userBody) {
        List<SysUser> list = userService.listUser(userBody);
        ExcelUtil<SysUser> util = new ExcelUtil<>(SysUser.class);
        return util.exportExcel(list, "用户数据");
    }

    /**
     * 用户数据导入
     *
     * @param file          文件
     * @param updateSupport t 是否更新支持，如果已存在，则进行更新数据
     * @return 结果
     * @throws Exception
     */
    @ApiOperation(value = "用户数据导入")
    @PostMapping("/importData")
    public Response<String> importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String oriPassword = configService.selectConfigByKey("sys.user.initPassword");
        String message = userService.importUser(userList, updateSupport, operName, oriPassword);
        return new Response<>().okMsg(message);
    }

    /**
     * 用户导入模版下载
     *
     * @return 模版
     */
    @ApiOperation(value = "用户导入模版下载")
    @GetMapping("/importTemplate")
    public Response importTemplate() {
        ExcelUtil<SysUser> util = new ExcelUtil<>(SysUser.class);
        return util.importTemplateExcel("用户数据");
    }

}
