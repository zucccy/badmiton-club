package com.yun.springbootinit.service.impl;

import com.yun.springbootinit.model.entity.ScoreRecord;
import com.yun.springbootinit.mapper.ScoreRecordMapper;
import com.yun.springbootinit.service.IScoreRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 积分记录 服务实现类
 * </p>
 *
 * @author chenyun
 * @since 2025-02-21
 */
@Service
public class ScoreRecordServiceImpl extends ServiceImpl<ScoreRecordMapper, ScoreRecord> implements IScoreRecordService {

}
