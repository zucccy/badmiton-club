package com.yun.springbootinit.model.vo;

import com.yun.springbootinit.model.dto.excel.ErrorInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Description: TODO
 * @Author: chenyun
 * @Date: 2025/2/26 22:41
 * @version: 1.0
 */
@Data
@AllArgsConstructor
public class ImportResultVO {
    private Integer processNum;

    private List<ErrorInfo> errorInfoList;
}
