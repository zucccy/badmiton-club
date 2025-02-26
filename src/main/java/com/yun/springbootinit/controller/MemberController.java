package com.yun.springbootinit.controller;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yun.springbootinit.common.BaseResponse;
import com.yun.springbootinit.common.ErrorCode;
import com.yun.springbootinit.common.ResultUtils;
import com.yun.springbootinit.exception.BusinessException;
import com.yun.springbootinit.exception.ThrowUtils;
import com.yun.springbootinit.model.dto.member.MemberImportData;
import com.yun.springbootinit.model.dto.member.MemberQueryRequest;
import com.yun.springbootinit.model.entity.Member;
import com.yun.springbootinit.model.entity.User;
import com.yun.springbootinit.model.vo.ImportResultVO;
import com.yun.springbootinit.model.vo.MemberVO;
import com.yun.springbootinit.model.vo.UserVO;
import com.yun.springbootinit.service.IMemberService;
import com.yun.springbootinit.utils.FileUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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

    @PostMapping("/import")
    public BaseResponse<ImportResultVO> importMember(@RequestPart("file") MultipartFile file) {
        FileUtils.validFile(file);
        return ResultUtils.success(memberService.importMember(file));
    }

    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=member_template.xlsx");
        try {
            EasyExcel.write(response.getOutputStream(), MemberImportData.class)
                    .sheet("会员导入模板文件")
                    .doWrite(Collections.emptyList());
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_OPERATE_ERROR, "文件写入失败");
        }
    }
}
