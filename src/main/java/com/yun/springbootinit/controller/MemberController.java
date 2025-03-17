package com.yun.springbootinit.controller;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yun.springbootinit.annotation.AuthCheck;
import com.yun.springbootinit.common.BaseResponse;
import com.yun.springbootinit.common.ErrorCode;
import com.yun.springbootinit.common.ResultUtils;
import com.yun.springbootinit.constant.CommonConstant;
import com.yun.springbootinit.exception.ThrowUtils;
import com.yun.springbootinit.manage.MemberImportManage;
import com.yun.springbootinit.model.dto.DeleteDTO;
import com.yun.springbootinit.model.dto.member.MemberImportData;
import com.yun.springbootinit.model.dto.member.MemberQueryRequest;
import com.yun.springbootinit.model.dto.member.MemberUpdateRequest;
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
import java.util.Map;
import java.util.Set;

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

    @GetMapping("/list")
    public BaseResponse<Page<MemberVO>> listMember(@RequestParam(required = false) Long id,
                                                   @RequestParam(required = false) String name,
                                                   @RequestParam(required = false) String gender,
                                                   @RequestParam(name = "start_age", required = false) Integer startAge,
                                                   @RequestParam(name = "end_age", required = false) Integer endAge,
                                                   @RequestParam(name = "is_civil_servant", required = false) Boolean isCivilServant,
                                                   @RequestParam(name = "is_cadre", required = false) Boolean isCadre,
                                                   @RequestParam(name = "is_veteran", required = false) Boolean isVeteran,
                                                   @RequestParam(name = "athlete_level", required = false) String athleteLevel,
                                                   @RequestParam(name = "referee_level", required = false) String refereeLevel,
                                                   @RequestParam(name = "residence_area", required = false) String residenceArea,
                                                   @RequestParam(name = "current_club_name", required = false) String currentClubNmae,
                                                   @RequestParam(name = "current_level", required = false) Integer currentLevel,
                                                   @RequestParam(name = "phone", required = false) String phone,
                                                   @RequestParam(defaultValue = "1") Long current,
                                                   @RequestParam(defaultValue = "10") Long pageSize,
                                                   @RequestParam(required = false) String sort
    ) {
        MemberQueryRequest memberQueryRequest = new MemberQueryRequest();
        memberQueryRequest.setId(id);
        memberQueryRequest.setName(name);
        memberQueryRequest.setGender(gender);
        memberQueryRequest.setPhone(phone);
        memberQueryRequest.setStartAge(startAge);
        memberQueryRequest.setEndAge(endAge);
        memberQueryRequest.setIsCivilServant(isCivilServant);
        memberQueryRequest.setIsCadre(isCadre);
        memberQueryRequest.setIsVeteran(isVeteran);
        memberQueryRequest.setAthleteLevel(athleteLevel);
        memberQueryRequest.setRefereeLevel(refereeLevel);
        memberQueryRequest.setResidenceArea(residenceArea);
        memberQueryRequest.setCurrentClubName(currentClubNmae);
        memberQueryRequest.setCurrentLevel(currentLevel);
        JSONObject sortJsonObj = JSONUtil.parseObj(sort);
        if (sortJsonObj.size() == 1) {
            Set<Map.Entry<String, Object>> entries = sortJsonObj.entrySet();
            entries.forEach(entry -> {
                memberQueryRequest.setSortField(entry.getKey());
                if (CommonConstant.AGE.equals(entry.getKey())) {
                    memberQueryRequest.setSortField(CommonConstant.BRITH_DATE);
                }
                memberQueryRequest.setSortOrder(entry.getValue() == null ? CommonConstant.SORT_ORDER_ASC : (String) entry.getValue());
            });
        }
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 50, ErrorCode.PARAMS_ERROR);
        Page<Member> memberPage = memberService.page(new Page<>(current, pageSize),
                memberService.getQueryWrapper(memberQueryRequest));
        Page<MemberVO> memberVOPage = new Page<>(current, pageSize, memberPage.getTotal());
        List<MemberVO> memberVOList = memberService.listMemberVO(memberPage.getRecords());
        memberVOPage.setRecords(memberVOList);
        return ResultUtils.success(memberVOPage);
    }

    @AuthCheck
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

    @AuthCheck
    @PostMapping("/export")
    public void exportMemberVOList(@RequestBody MemberQueryRequest memberQueryRequest, HttpServletResponse response) {
        String filename = String.format("member_list_%s%s", System.currentTimeMillis(), CommonConstant.EXCEL_FILE_SUFFIX);
        LOGGER.info("filename: {}", filename);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", String.format("attachment;filename=%s", filename));
        memberService.exportMemberVOList(memberQueryRequest, response);
    }

    @PostMapping("/add")
    public BaseResponse<Long> addMember(@RequestBody MemberImportData memberImportData) {
        return ResultUtils.success(memberService.addMember(memberImportData));
    }

    @DeleteMapping("/delete")
    public BaseResponse<Integer> batchDeleteMembers(@RequestBody DeleteDTO memberDeleteDTO) {
        return ResultUtils.success(memberService.deleteMember(memberDeleteDTO));
    }

    @PutMapping("/update/{id}")
    public BaseResponse<Long> updateMember(@PathVariable("id") Long id, @RequestBody MemberUpdateRequest memberUpdateRequest) {
        return ResultUtils.success(memberService.updateMember(id, memberUpdateRequest));
    }

}
