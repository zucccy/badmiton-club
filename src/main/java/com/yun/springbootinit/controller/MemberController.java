package com.yun.springbootinit.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yun.springbootinit.common.BaseResponse;
import com.yun.springbootinit.common.ErrorCode;
import com.yun.springbootinit.common.ResultUtils;
import com.yun.springbootinit.exception.BusinessException;
import com.yun.springbootinit.exception.ThrowUtils;
import com.yun.springbootinit.model.dto.member.MemberQueryRequest;
import com.yun.springbootinit.model.entity.Member;
import com.yun.springbootinit.model.entity.User;
import com.yun.springbootinit.model.vo.MemberVO;
import com.yun.springbootinit.model.vo.UserVO;
import com.yun.springbootinit.service.IMemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 会员主表 前端控制器
 * </p>
 *
 * @author chenyun
 * @since 2025-02-21
 */
@RestController
@RequestMapping("/member")
public class MemberController {
    @Resource
    private IMemberService memberService;

    @PostMapping("/list")
    public BaseResponse<Page<MemberVO>> listMember(@RequestBody MemberQueryRequest memberQueryRequest) {
        if (memberQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long pageSize = memberQueryRequest.getPageSize();
        long current = memberQueryRequest.getCurrent();
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 50, ErrorCode.PARAMS_ERROR);
        Page<Member> memberPage = memberService.page(new Page<>(current, pageSize),
                memberService.getQueryWrapper(memberQueryRequest));
        Page<MemberVO> memberVOPage = new Page<>(current, pageSize, memberPage.getTotal());
        List<MemberVO> memberVOList = memberService.listMemberVO(memberPage.getRecords());
        memberVOPage.setRecords(memberVOList);
        return ResultUtils.success(memberVOPage);
    }
}
