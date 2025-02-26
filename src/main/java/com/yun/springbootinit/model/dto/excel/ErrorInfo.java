package com.yun.springbootinit.model.dto.excel;

import lombok.Builder;
import lombok.Data;

/**
 * @Description: TODO
 * @Author: chenyun
 * @Date: 2025/2/26 22:39
 * @version: 1.0
 */
@Data
@Builder
public class ErrorInfo {
    private Integer rowNum;

    private String message;
}
