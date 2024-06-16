package com.qiu.apifinal.config;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@Component
public class SaTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            // 尝试执行登录校验
            StpUtil.checkLogin();
            return true; // 校验通过，继续处理请求
        } catch (Exception e) {
            // 登录校验失败，返回401未授权状态码
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("401 Unauthorized: You need to log in first");
            return false; // 拦截请求
        }
    }
}
