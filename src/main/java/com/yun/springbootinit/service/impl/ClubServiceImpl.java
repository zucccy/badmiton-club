package com.yun.springbootinit.service.impl;
import java.time.LocalDateTime;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yun.springbootinit.common.ErrorCode;
import com.yun.springbootinit.constant.CommonConstant;
import com.yun.springbootinit.exception.BusinessException;
import com.yun.springbootinit.model.dto.DeleteDTO;
import com.yun.springbootinit.model.dto.club.ClubAddRequest;
import com.yun.springbootinit.model.dto.club.ClubDTO;
import com.yun.springbootinit.model.dto.club.FrontendObject;
import com.yun.springbootinit.model.entity.Club;
import com.yun.springbootinit.mapper.ClubMapper;
import com.yun.springbootinit.model.entity.Member;
import com.yun.springbootinit.model.vo.ClubVO;
import com.yun.springbootinit.service.IClubService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yun.springbootinit.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public List<FrontendObject> getAllClubName() {
        List<Club> clubList = this.list();
        if (CollectionUtil.isEmpty(clubList)) {
            return Collections.emptyList();
        }
        return clubList.stream()
                .map(club -> {
                    FrontendObject frontendObject = new FrontendObject();
                    frontendObject.setLabel(club.getClubName());
                    frontendObject.setValue(club.getClubName());
                    return frontendObject;
                }).collect(Collectors.toList());
    }
    @Override
    public QueryWrapper<Club> getQueryWrapper(ClubDTO clubDTO) {
        if (clubDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        String sortField = clubDTO.getSortField();
        String sortOrder = clubDTO.getSortOrder();
        QueryWrapper<Club> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(clubDTO.getId() != null, "id", clubDTO.getId());
        queryWrapper.like(StringUtils.isNotBlank(clubDTO.getClubName()), "club_name", clubDTO.getClubName());
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    @Override
    public List<ClubVO> listClubVO(List<Club> clubList) {
        if (CollectionUtil.isEmpty(clubList)) {
            return Collections.emptyList();
        }
        return clubList.stream().map(this::getClubVO).collect(Collectors.toList());
    }

    @Override
    public ClubVO getClubVO(Club club) {
        ClubVO clubVO = new ClubVO();
        BeanUtils.copyProperties(club, clubVO);
        return clubVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long addClub(ClubAddRequest clubAddRequest) {
        if (clubAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        QueryWrapper<Club> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("club_name", clubAddRequest.getClubName());
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "俱乐部已存在");
        }
        Club club = new Club();
        club.setClubName(clubAddRequest.getClubName());
        this.save(club);
        return club.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer deleteClub(DeleteDTO deleteDTO) {
        if (CollectionUtil.isEmpty(deleteDTO.getIdList())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数错误");
        }
        List<Club> clubList = this.listByIds(deleteDTO.getIdList());
        if (clubList.size() != deleteDTO.getIdList().size()) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "内部服务错误");
        }
        this.removeBatchByIds(deleteDTO.getIdList());
        return deleteDTO.getIdList().size();
    }
}
