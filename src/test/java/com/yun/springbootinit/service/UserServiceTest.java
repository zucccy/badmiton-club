package com.yun.springbootinit.service;

import javax.annotation.Resource;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 用户服务测试
 *
 * @author chenyun
 * 
 */
@SpringBootTest
public class UserServiceTest {

    @Resource
    private IUserService userService;

    @Test
    void userRegister() {
        String account = "yun";
        String password = "";
        String checkPassword = "123456";
        try {
            long result = userService.userRegister(account, password, checkPassword);
            Assertions.assertEquals(-1, result);
            account = "chenyun";
            result = userService.userRegister(account, password, checkPassword);
            Assertions.assertEquals(-1, result);
        } catch (Exception e) {

        }
    }
}
