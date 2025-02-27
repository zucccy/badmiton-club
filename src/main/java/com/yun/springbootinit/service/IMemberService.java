package com.yun.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yun.springbootinit.model.dto.member.MemberQueryRequest;
import com.yun.springbootinit.model.entity.Member;
import com.yun.springbootinit.model.vo.MemberVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 会员主表 服务类
 * </p>
 *
 * @author chenyun
 * @since 2025-02-21
 */
public interface IMemberService extends IService<Member> {

    /**
     * 获取查询条件
     * @param memberQueryRequest
     * @return
     */
    QueryWrapper<Member> getQueryWrapper(MemberQueryRequest memberQueryRequest);

    /**
     * 获取脱敏member列表
     * @param memberList
     * @return
     */
    List<MemberVO> listMemberVO(List<Member> memberList);

    /**
     * 获取脱敏member
     * @param member
     * @return
     */
    MemberVO getMemberVO(Member member);

    /**
     * 根据筛选条件导出会员excel表
     * @param memberQueryRequest
     * @param response
     */
    void exportMemberVOList(MemberQueryRequest memberQueryRequest, HttpServletResponse response);

    /**
     * 下载会员导入模板excel表
     * @param response
     */
    void downloadTemplate(HttpServletResponse response);
}
