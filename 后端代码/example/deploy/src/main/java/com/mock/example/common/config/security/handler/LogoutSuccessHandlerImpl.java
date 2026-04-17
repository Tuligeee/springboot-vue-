package com.mock.example.common.config.security.handler;

import com.mock.example.common.consts.HttpStatus;
import com.mock.example.common.entity.Response;
import com.mock.example.common.utils.JsonUtil;
import com.mock.example.common.utils.ServletUtil;
import com.mock.example.modules.system.service.TokenService;
import com.mock.example.modules.system.types.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 自定义登录退出处理类
 *
 * @author: Mock
 * @date: 2023-02-26 21:51:46
 */
@Configuration
@RequiredArgsConstructor
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    private final TokenService tokenService;

    /**
     * 退出处理
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException, ServletException {

        LoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser != null) {
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getToken());
        }
        ServletUtil.renderString(response, JsonUtil.toString(new Response<>(HttpStatus.SUCCESS, "退出成功")));
    }


}

  