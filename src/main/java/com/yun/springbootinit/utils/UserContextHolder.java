package com.yun.springbootinit.utils;

import com.yun.springbootinit.model.entity.User;

/**
 * @Description: 全局存储登录用户，使用ThreadLocal在整个请求过程中访问
 * @Author: chenyun
 * @Date: 2025/2/24 23:03
 * @version: 1.0
 */
public class UserContextHolder {
    public static final ThreadLocal<User> CONTEXT = new ThreadLocal<>();

    public static void setLoginUser(User user) {
        CONTEXT.set(user);
    }

    public static User getLoginUser() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
