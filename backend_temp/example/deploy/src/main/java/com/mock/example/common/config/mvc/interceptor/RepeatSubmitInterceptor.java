package com.mock.example.common.config.mvc.interceptor;

import com.mock.example.common.annotation.RepeatSubmit;
import com.mock.example.common.consts.HttpStatus;
import com.mock.example.common.entity.Response;
import com.mock.example.common.utils.JsonUtil;
import com.mock.example.common.utils.ServletUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 防止重复提交拦截器
 *
 * @author: Mock
 * @date: 2023-02-26 22:07:11
 */
@Component
public abstract class RepeatSubmitInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            RepeatSubmit annotation = method.getAnnotation(RepeatSubmit.class);
            if (annotation != null) {
                if (this.isRepeatSubmit(request)) {
                    ServletUtil.renderString(response, JsonUtil.toString(new Response<>(HttpStatus.SUCCESS,
                            "不允许重复提交，请稍后再试")));
                    return false;
                }
            }
            return true;
        } else {
            return super.preHandle(request, response, handler);
        }
    }

    /**
     * 验证是否重复提交由子类实现具体的防重复提交的规则
     */
    public abstract boolean isRepeatSubmit(HttpServletRequest request);
}
