package com.yun.springbootinit.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yun.springbootinit.common.BaseResponse;
import com.yun.springbootinit.common.ErrorCode;
import com.yun.springbootinit.common.ResultUtils;
import com.yun.springbootinit.constant.CommonConstant;
import com.yun.springbootinit.exception.BusinessException;
import com.yun.springbootinit.exception.ThrowUtils;
import com.yun.springbootinit.manage.MemberImportManage;
import com.yun.springbootinit.model.dto.member.MemberQueryRequest;
import com.yun.springbootinit.model.entity.Member;
import com.yun.springbootinit.model.vo.ImportResultVO;
import com.yun.springbootinit.model.vo.MemberVO;
import com.yun.springbootinit.service.IMemberService;
import com.yun.springbootinit.utils.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberController.class);

    @Resource
    private IMemberService memberService;

    @Resource
    private MemberImportManage memberImportManage;

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
        return ResultUtils.success(memberImportManage.importMember(file));
    }

    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) {
        String filename = "member_import_template.xlsx";
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", String.format("attachment;filename=%s", filename));
        memberService.downloadTemplate(response);
    }

    @PostMapping("/export")
    public void exportMemberVOList(@RequestBody MemberQueryRequest memberQueryRequest, HttpServletResponse response) {
        String filename = String.format("member_list_%s%s", System.currentTimeMillis(), CommonConstant.EXCEL_FILE_SUFFIX);
        LOGGER.info("filename: {}", filename);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", String.format("attachment;filename=%s", filename));
        memberService.exportMemberVOList(memberQueryRequest, response);
    }

}
