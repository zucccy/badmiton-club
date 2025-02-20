package com.yun.springbootinit.model;

import lombok.Data;

import java.util.List;

/**
 * @Author: cheny
 * @Description: TODO
 * @Date: 2023/7/20 23:49
 */
@Data
public class CreateChatResponse {

    private String id;
    private String object;
    private Long create;
    private List<Choices> choices;
    private Usage usage;

}
