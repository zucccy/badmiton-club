package com.yun.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yun.springbootinit.model.entity.User;

/**
 * @author cheny
 */
public interface UserMapper extends BaseMapper<User> {
    User queryUserById(Long id);
}




