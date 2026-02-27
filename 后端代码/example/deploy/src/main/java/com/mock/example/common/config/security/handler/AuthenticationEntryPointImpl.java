package com.mock.example.common.config.security.handler;

import cn.hutool.core.util.StrUtil;
import com.mock.example.common.consts.HttpStatus;
import com.mock.example.common.entity.Response;
import com.mock.example.common.utils.JsonUtil;
import com.mock.example.common.utils.ServletUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * 认证失败处理类 返回未授权
 *
 * @author: Mock
 * @date: 2023-02-26 21:17:48
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = 1;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException e) throws IOException, ServletException {

        int code = HttpStatus.UNAUTHORIZED;
        String msg = StrUtil.format("请求访问：{}，认证失败，无法访问系统资源", request.getRequestURI());
        //渲染下请求结果
        ServletUtil.renderString(
                response,
                JsonUtil.toJSONString(new Response(code, msg))
        );

    }
}

  