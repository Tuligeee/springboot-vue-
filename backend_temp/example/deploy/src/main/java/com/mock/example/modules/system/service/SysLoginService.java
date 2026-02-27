package com.mock.example.modules.system.service;

import com.mock.example.common.exception.BizException;
import com.mock.example.interfaces.vo.system.user.LoginTokenVo;
import com.mock.example.modules.system.types.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * 登录业务层
 *
 * @author: Mock
 * @date: 2023-02-26 20:09:19
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysLoginService {

    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    /**
     * 登录验证
     *
     * @param username 用户
     * @param password 密码
     * @return token
     */
    public LoginTokenVo login(String username, String password) {
        // 用户验证
        Authentication authentication;

        try {
            // Security 自定义用户密码认证
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                throw new BizException("用户密码不正确");
            } else {
                throw new BizException(e.getMessage());
            }
        }

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();

        // 生成token
        String token = tokenService.createToken(loginUser);
        LoginTokenVo tokenVo = new LoginTokenVo();
        tokenVo.setToken(token);

        return tokenVo;
    }

}

  