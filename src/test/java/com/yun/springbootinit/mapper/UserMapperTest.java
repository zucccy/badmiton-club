package com.yun.springbootinit.mapper;

import com.yun.springbootinit.model.entity.User;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class UserMapperTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserMapperTest.class);

    @Resource
    private UserMapper userMapper;

    @Test
    void queryUserById() {
        User user = userMapper.queryUserById(1L);
        LOGGER.info("user: {}", user.toString());
    }
}