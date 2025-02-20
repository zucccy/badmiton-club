package com.yun.springbootinit.model;

import lombok.Data;

/**
 * @Author: cheny
 * @Description: TODO
 * @Date: 2023/7/20 23:54
 */
@Data
public class Usage {

    private Integer promptTokens;
    private Integer completionTokens;
    private Integer totalTokens;
}
