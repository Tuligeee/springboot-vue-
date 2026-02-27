package com.mock.example.interfaces.controller.system;

import com.mock.example.common.config.ProjectConfig;
import com.mock.example.common.entity.Response;
import com.mock.example.common.utils.SecurityUtil;
import com.mock.example.common.utils.file.FileUploadUtil;
import com.mock.example.interfaces.body.system.user.UserBody;
import com.mock.example.interfaces.controller.BaseController;
import com.mock.example.interfaces.vo.system.user.UserProfileDetailVo;
import com.mock.example.interfaces.vo.system.user.UserUploadAvatarVo;
import com.mock.example.modules.system.entity.model.SysUser;
import com.mock.example.modules.system.service.SysUserService;
import com.mock.example.modules.system.service.TokenService;
import com.mock.example.modules.system.types.LoginUser;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 用户个人中心
 *
 * @author: Mock
 * @date: 2025-01-18 06:51:10
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/system/user/profile")
public class SysProfileController extends BaseController {

    private final SysUserService userService;

    private final TokenService tokenService;

    /**
     * 个人中心信息
     *
     * @return 用户信息
     */
    @ApiOperation(value = "个人中心信息")
    @GetMapping
    public Response<UserProfileDetailVo> profile() {
        return new Response<>(userService.profile());
    }

    /**
     * 修改用户中心
     *
     * @param userBody 用户信息
     * @return 结果
     */
    @ApiOperation(value = "修改用户中心")
    @PutMapping
    public Response<Boolean> updateProfile(@RequestBody UserBody userBody) {
        //更新数据库基本信息
        userService.updateProfile(userBody);
        //更新缓存
        LoginUser loginUser = SecurityUtil.getLoginUser();
        SysUser sysUser = loginUser.getUser();
        sysUser.setNickName(userBody.getNickName());
        sysUser.setMobile(userBody.getMobile());
        sysUser.setEmail(userBody.getEmail());
        sysUser.setSex(userBody.getSex());
        tokenService.setLoginUser(loginUser);
        return new Response<>();
    }

    /**
     * 重置密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 结果
     */
    @ApiOperation(value = "重置密码")
    @PutMapping("/updatePwd")
    public Response<Boolean> updatePwd(String oldPassword, String newPassword) {
        LoginUser loginUser = getLoginUser();
        Long userId = loginUser.getUserId();
        String password = loginUser.getPassword();

        if (!SecurityUtil.matchesPassword(oldPassword, password)) {
            return new Response().failMsg("修改密码失败，旧密码错误");
        }
        if (SecurityUtil.matchesPassword(newPassword, password)) {
            return new Response().failMsg("新密码不能与旧密码相同");
        }
        //更新数据库密码
        userService.resetPwd(userId, newPassword);
        // 更新缓存用户密码
        loginUser.getUser().setPassword(SecurityUtil.encryptPassword(newPassword));
        tokenService.setLoginUser(loginUser);

        return new Response<>().okMsg("修改密码成功");
    }

    /**
     * 头像上传
     *
     * @param file 文件
     * @return 结果
     * @throws IOException
     */
    @ApiOperation(value = "头像上传")
    @PostMapping("/avatar")
    public Response<UserUploadAvatarVo> avatar(@RequestParam("avatarfile") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            //上传文件
            LoginUser loginUser = getLoginUser();
            String avatar = FileUploadUtil.upload(ProjectConfig.getAvatarPath(), file);
            userService.updateAvatar(loginUser.getUserId(), avatar);

            // 更新缓存用户头像
            loginUser.getUser().setAvatar(avatar);
            tokenService.setLoginUser(loginUser);
            UserUploadAvatarVo userUploadAvatarVo = new UserUploadAvatarVo();
            userUploadAvatarVo.setImgUrl(avatar);

            return new Response<>(userUploadAvatarVo);

        }
        return new Response<>().failMsg("上传图片异常，请联系管理员");
    }

}

  