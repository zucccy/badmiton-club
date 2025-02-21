package com.yun.springbootinit.service.impl;

import com.yun.springbootinit.model.entity.Game;
import com.yun.springbootinit.mapper.GameMapper;
import com.yun.springbootinit.service.IGameService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 赛事信息 服务实现类
 * </p>
 *
 * @author chenyun
 * @since 2025-02-21
 */
@Service
public class GameServiceImpl extends ServiceImpl<GameMapper, Game> implements IGameService {

}
