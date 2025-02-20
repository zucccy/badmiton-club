package com.yun.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yun.springbootinit.model.entity.User;

/**
 * @author cheny
 * @description 针对表【user(用户)】的数据库操作Mapper
 * @createDate 2023-07-18 22:39:07
 * @Entity com.yupi.springbootinit.model.entity.User
 */
public interface UserMapper extends BaseMapper<User> {
    User queryUserById(Integer id);
}




