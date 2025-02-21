package com.yun.springbootinit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yun.springbootinit.mapper.ChartMapper;
import com.yun.springbootinit.model.entity.Chart;
import com.yun.springbootinit.service.IChartService;
import org.springframework.stereotype.Service;

/**
 * @author cheny
 * @description 针对表【chart(图表信息表)】的数据库操作Service实现
 * @createDate 2023-07-18 22:39:07
 */
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart>
        implements IChartService {

}




