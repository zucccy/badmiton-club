package com.yun.springbootinit.controller;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yun.springbootinit.common.BaseResponse;
import com.yun.springbootinit.common.ErrorCode;
import com.yun.springbootinit.common.ResultUtils;
import com.yun.springbootinit.constant.CommonConstant;
import com.yun.springbootinit.exception.ThrowUtils;
import com.yun.springbootinit.model.dto.DeleteDTO;
import com.yun.springbootinit.model.dto.club.ClubAddRequest;
import com.yun.springbootinit.model.dto.club.ClubDTO;
import com.yun.springbootinit.model.dto.club.FrontendObject;
import com.yun.springbootinit.model.entity.Club;
import com.yun.springbootinit.model.vo.ClubVO;
import com.yun.springbootinit.service.IClubService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * 俱乐部信息 前端控制器
 * </p>
 *
 * @author chenyun
 * @since 2025-02-21
 */
@RestController
@RequestMapping("/club")
public class ClubController {

    @Resource
    private IClubService clubService;

    @GetMapping("/get_all_club_name")
    public BaseResponse<List<FrontendObject>> getAllClubName() {
        return ResultUtils.success(clubService.getAllClubName());
    }

    @GetMapping("/list")
    public BaseResponse<Page<ClubVO>> listClub(@RequestParam(required = false) Long id,
                                                 @RequestParam(name = "club_name", required = false) String clubName,
                                                 @RequestParam(defaultValue = "1") Long current,
                                                 @RequestParam(defaultValue = "10") Long pageSize,
                                                 @RequestParam(required = false) String sort
    ) {
        ClubDTO clubDTO = new ClubDTO();
        clubDTO.setId(id);
        clubDTO.setClubName(clubName);
        JSONObject sortJsonObj = JSONUtil.parseObj(sort);
        if (sortJsonObj.size() == 1) {
            Set<Map.Entry<String, Object>> entries = sortJsonObj.entrySet();
            entries.forEach(entry -> {
                clubDTO.setSortField(entry.getKey());
                clubDTO.setSortOrder(entry.getValue() == null ? CommonConstant.SORT_ORDER_ASC : (String) entry.getValue());
            });
        }
        // 限制爬虫
        ThrowUtils.throwIf(pageSize > 50, ErrorCode.PARAMS_ERROR);
        Page<Club> clubPage = clubService.page(new Page<>(current, pageSize),
                clubService.getQueryWrapper(clubDTO));
        Page<ClubVO> clubVOPage = new Page<>(current, pageSize, clubPage.getTotal());
        List<ClubVO> clubVOList = clubService.listClubVO(clubPage.getRecords());
        clubVOPage.setRecords(clubVOList);
        return ResultUtils.success(clubVOPage);
    }

    @PostMapping("/add")
    public BaseResponse<Long> addClub(@RequestBody @Valid ClubAddRequest clubAddRequest) {
        return ResultUtils.success(clubService.addClub(clubAddRequest));
    }

    @DeleteMapping("/delete")
    public BaseResponse<Integer> batchDeleteClubs(@RequestBody DeleteDTO deleteDTO) {
        return ResultUtils.success(clubService.deleteClub(deleteDTO));
    }
}
