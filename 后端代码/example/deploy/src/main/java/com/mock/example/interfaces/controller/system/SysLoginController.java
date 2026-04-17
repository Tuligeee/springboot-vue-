package com.mock.example.interfaces.controller.system;

import com.mock.example.common.entity.Response;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.interfaces.body.system.user.LoginBody;
import com.mock.example.interfaces.body.system.user.RegisterBody;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.interfaces.vo.system.router.RouterVo;
import com.mock.example.interfaces.vo.system.user.LoginTokenVo;
import com.mock.example.interfaces.vo.system.user.UserInfoVo;
import com.mock.example.modules.system.entity.model.SysUser;
import com.mock.example.modules.system.service.*;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 用户登录
 *
 * @author: Mock
 * @date: 2023-02-26 19:30:36
 */
@RestController
@RequiredArgsConstructor
public class SysLoginController extends BaseController {

    private final SysLoginService loginService;

    private final SysPermissionService sysPermissionService;

    private final SysMenuService sysMenuService;

    private final SysConfigService configService;

    private final SysUserService userService;

    /**
     * 用户登录接口
     *
     * @param loginBody 登录请求体
     * @return 用户信息
     */
    @ApiOperation(value = "用户登录接口")
    @PostMapping("/login")
    public Response<LoginTokenVo> login(@RequestBody LoginBody loginBody) {
        return new Response(
                loginService.login(loginBody.getUsername(), loginBody.getPassword())
        );
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @ApiOperation(value = "获取用户信息")
    @GetMapping("getInfo")
    public Response<UserInfoVo> getInfo() {
        SysUser user = SecurityUtil.getLoginUser().getUser();
        // 角色集合
        Set<String> roles = sysPermissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = sysPermissionService.getMenuPermission(user);

        UserInfoVo userInfoVo = new UserInfoVo();
        userInfoVo.setUser(user);
        userInfoVo.setRoles(roles);
        userInfoVo.setPermissions(permissions);

        return new Response<>(userInfoVo);
    }

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @ApiOperation(value = "获取用户路由")
    @GetMapping("getRouters")
    public Response<List<RouterVo>> getRouters() {
        return new Response(sysMenuService.buildMenus());
    }

    /**
     * 注册用户
     *
     * @param registerBody 注册用户信息
     * @return 结果
     */
    @ApiOperation(value = "注册用户")
    @PostMapping("/register")
    public Response<String> register(@RequestBody RegisterBody registerBody) {
        if (!("true".equals(configService.selectConfigByKey("sys.account.registerUser")))) {
            return error("当前系统没有开启注册功能！");
        }
        String msg = userService.register(registerBody);
        return StringUtils.isEmpty(msg) ? success() : error(msg);
    }
}

