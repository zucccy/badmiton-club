package com.yun.springbootinit.model.dto.user;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * 用户更新个人信息请求
 *
 * @author chenyun
 * 
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserUpdateMyRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String username;

    private static final long serialVersionUID = 1L;
}