package com.yun.springbootinit.mapper;

import com.yun.springbootinit.model.entity.Chart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * @author cheny
 * @description 针对表【chart(图表信息表)】的数据库操作Mapper
 * @createDate 2023-07-18 22:39:07
 * @Entity com.yupi.springbootinit.model.entity.Chart
 */
public interface ChartMapper extends BaseMapper<Chart> {
    List<Map<String, Object>> queryChartData(String querySql);
}




