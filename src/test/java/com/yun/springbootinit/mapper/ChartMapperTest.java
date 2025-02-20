package com.yun.springbootinit.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

@SpringBootTest
class ChartMapperTest {

    @Resource
    private ChartMapper chartMapper;

    @Test
    void queryChartData() {
        Long chartId = 1681315808774565890L;
        String querySql = String.format("SELECT * FROM chart_%s", chartId);
//        String querySql = "SELECT * FROM chart_1681315808774565890";
        List<Map<String, Object>> result = chartMapper.queryChartData(querySql);
        System.out.println(result);
    }
}