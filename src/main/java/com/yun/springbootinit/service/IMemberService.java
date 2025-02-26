package com.yun.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yun.springbootinit.model.dto.member.MemberQueryRequest;
import com.yun.springbootinit.model.dto.user.UserQueryRequest;
import com.yun.springbootinit.model.entity.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yun.springbootinit.model.entity.User;
import com.yun.springbootinit.model.vo.ImportResultVO;
import com.yun.springbootinit.model.vo.MemberVO;
import org.springframework.web.multipart.MultipartFile;

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
     * 导入会员excel数据到数据表
     * @param file
     * @return
     */
    ImportResultVO importMember(MultipartFile file);
}
