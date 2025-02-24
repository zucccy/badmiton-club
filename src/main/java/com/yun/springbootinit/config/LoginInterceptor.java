package com.yun.springbootinit.config;

import com.yun.springbootinit.model.entity.User;
import com.yun.springbootinit.service.IUserService;
import com.yun.springbootinit.utils.UserContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Description: TODO
 * @Author: chenyun
 * @Date: 2025/2/24 23:07
 * @version: 1.0
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 从request中获取user
        User loginUser = userService.getLoginUser(request);
        UserContextHolder.setLoginUser(loginUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContextHolder.clear();
    }
}
