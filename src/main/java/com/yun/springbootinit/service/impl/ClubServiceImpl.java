package com.yun.springbootinit.service.impl;

import com.yun.springbootinit.model.entity.Club;
import com.yun.springbootinit.mapper.ClubMapper;
import com.yun.springbootinit.service.IClubService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 俱乐部信息 服务实现类
 * </p>
 *
 * @author chenyun
 * @since 2025-02-21
 */
@Service
public class ClubServiceImpl extends ServiceImpl<ClubMapper, Club> implements IClubService {

}
