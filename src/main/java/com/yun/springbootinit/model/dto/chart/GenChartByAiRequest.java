package com.yun.springbootinit.model.dto.chart;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: cheny
 * @Description: 接收名称、文件数据、分析目标、类型
 * @Date: 2023/7/19 23:22
 */
@Data
public class GenChartByAiRequest implements Serializable {

    /**
     * 图表名称
     */
    private String name;

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表类型
     */
    private String chartType;

    private static final long serialVersionUID = 1L;
}