package com.yun.springbootinit.model;

import lombok.Data;

/**
 * @Author: cheny
 * @Description: TODO
 * @Date: 2023/7/20 23:52
 */
@Data
public class Choices {

    private String index;
    private Message message;
    private String finishReason;
}
