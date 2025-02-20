package com.yun.springbootinit.model.dto.chart;

import lombok.Data;

/**
 * @Author: cheny
 * @Description: BiResponse图表返回类
 * @Date: 2023/7/21 17:09
 */
@Data
public class BiResponse {

    /**
     * 生成的图表代码
     */
    private String genChart;

    /**
     * 生成的分析结论
     */
    private String genResult;

    /**
     * 生成的图表id，因为要存到数据库里
     */
    private Long chartId;
}
