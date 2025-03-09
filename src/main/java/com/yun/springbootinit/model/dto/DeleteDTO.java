package com.yun.springbootinit.model.dto;

import lombok.Data;

import java.util.List;

/**
 * @Description: TODO
 * @Author: chenyun
 * @Date: 2025/3/7 22:52
 * @version: 1.0
 */
@Data
public class DeleteDTO {
    private List<Long> idList;
}
