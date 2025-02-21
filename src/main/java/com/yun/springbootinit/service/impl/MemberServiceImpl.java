package com.yun.springbootinit.service.impl;

import com.yun.springbootinit.model.entity.Member;
import com.yun.springbootinit.mapper.MemberMapper;
import com.yun.springbootinit.service.IMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员主表 服务实现类
 * </p>
 *
 * @author chenyun
 * @since 2025-02-21
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService {

}
