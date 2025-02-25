package com.yun.springbootinit.service.impl;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yun.springbootinit.common.ErrorCode;
import com.yun.springbootinit.constant.CommonConstant;
import com.yun.springbootinit.exception.BusinessException;
import com.yun.springbootinit.model.dto.member.MemberQueryRequest;
import com.yun.springbootinit.model.entity.Club;
import com.yun.springbootinit.model.entity.Member;
import com.yun.springbootinit.mapper.MemberMapper;
import com.yun.springbootinit.model.enums.*;
import com.yun.springbootinit.model.vo.MemberVO;
import com.yun.springbootinit.service.IClubService;
import com.yun.springbootinit.service.IMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yun.springbootinit.utils.SqlUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.stream.Collectors;

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

    @Resource
    private IClubService clubService;

    @Override
    public QueryWrapper<Member> getQueryWrapper(MemberQueryRequest memberQueryRequest) {
        if (memberQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = memberQueryRequest.getId();
        String name = memberQueryRequest.getName();
        Integer gender = null;
        if (GenderEnum.FEMALE.getText().equals(memberQueryRequest.getGender())) {
            gender = GenderEnum.FEMALE.getValue();
        } else if (GenderEnum.MALE.getText().equals(memberQueryRequest.getGender())) {
            gender = GenderEnum.MALE.getValue();
        }
        Integer startAge = memberQueryRequest.getStartAge();
        Integer endAge = memberQueryRequest.getEndAge();
        Date getDateBeforeStartAge = getDateBeforeAge(startAge, CommonConstant.START);
        Date getDateBeforeEndAge = getDateBeforeAge(endAge, CommonConstant.END);
        Boolean isCivilServant = memberQueryRequest.getIsCivilServant();
        Boolean isCadre = memberQueryRequest.getIsCadre();
        Boolean isVeteran = memberQueryRequest.getIsVeteran();
        String athleteLevel = memberQueryRequest.getAthleteLevel();
        String refereeLevel = memberQueryRequest.getRefereeLevel();
        String residenceArea = memberQueryRequest.getResidenceArea();
        Long currentClubId = memberQueryRequest.getCurrentClubId();
        Integer currentLevel = memberQueryRequest.getCurrentLevel();
        String sortField = memberQueryRequest.getSortField();
        String sortOrder = memberQueryRequest.getSortOrder();
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.like(StringUtils.isNotBlank(name), "name", name);
        queryWrapper.eq(gender != null, "gender", gender);
        // 如果要找到比startAge大的，则出生日期要小于getDateBeforeStartAge
        // 如果要找到比endAge小的，则出生日期要大于getDateBeforeEndAge
        queryWrapper.le(startAge != null && endAge != null, "birth_date", getDateBeforeStartAge);
        queryWrapper.ge(startAge != null && endAge != null, "birth_date", getDateBeforeEndAge);
        queryWrapper.eq(isCivilServant != null, "is_civil_servant", isCivilServant);
        queryWrapper.eq(isCadre != null, "is_veteran", isCadre);
        queryWrapper.eq(isVeteran != null, "is_cadre", isVeteran);
        queryWrapper.eq(athleteLevel != null, "athlete_level", athleteLevel);
        queryWrapper.eq(refereeLevel != null, "referee_level", refereeLevel);
        queryWrapper.eq(residenceArea != null, "residence_area", residenceArea);
        queryWrapper.eq(currentClubId != null, "current_club_id", currentClubId);
        queryWrapper.eq(currentLevel != null, "current_level", currentLevel);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    @Override
    public List<MemberVO> listMemberVO(List<Member> memberList) {
        if (CollectionUtil.isEmpty(memberList)) {
            return Collections.emptyList();
        }
        return memberList.stream().map(this::getMemberVO).collect(Collectors.toList());
    }

    @Override
    public MemberVO getMemberVO(Member member) {
        MemberVO memberVO = new MemberVO();
        BeanUtils.copyProperties(member, memberVO);
        memberVO.setGender(Objects.requireNonNull(GenderEnum.getEnumByValue(member.getGender())).getText());
        memberVO.setAge(getAgeFromBrithDate(member.getBirthDate()));
        if (member.getAthleteLevel() != null && MemberAthleteLevelEnum.getEnumByValue(member.getAthleteLevel()) != null) {
            memberVO.setAthleteLevel(Objects.requireNonNull(MemberAthleteLevelEnum.getEnumByValue(member.getAthleteLevel())).getText());
        }
        if (member.getRefereeLevel() != null && MemberRefereeLevelEnum.getEnumByValue(member.getRefereeLevel()) != null) {
            memberVO.setRefereeLevel(Objects.requireNonNull(MemberRefereeLevelEnum.getEnumByValue(member.getRefereeLevel())).getText());
        }
        if (member.getResidenceArea() != null && MemberResidenceEnum.getEnumByValue(member.getResidenceArea()) != null) {
            memberVO.setResidenceArea(Objects.requireNonNull(MemberResidenceEnum.getEnumByValue(member.getResidenceArea())).getText());
        }
        if (member.getCurrentClubId() != null) {
            Club club = clubService.getById(member.getCurrentClubId());
            if (club != null) {
                memberVO.setCurrentClubName(club.getClubName());
            }
        }
        if (member.getCurrentLevel() != null && ClubLevelEnum.getEnumByValue(member.getCurrentLevel()) != null) {
            memberVO.setCurrentLevel(Objects.requireNonNull(ClubLevelEnum.getEnumByValue(member.getCurrentLevel())).getText());
        }
        return memberVO;
    }

    /**
     * 获取age年前的日期
     *
     * @param age
     * @return
     */
    private static Date getDateBeforeAge(Integer age, String action) {
        if (age == null || action == null) {
            return null;
        }
        LocalDate currentYear = null;
        if (CommonConstant.END.equals(action)) {
            // 获取1月1日
            currentYear = LocalDate.now().withMonth(1).withDayOfMonth(1);
        } else if (CommonConstant.START.equals(action)) {
            // 获取12月31日
            currentYear = LocalDate.now().withMonth(12).withDayOfMonth(31);
        }
        if (currentYear != null) {
            // 计算 age 年前的日期
            LocalDate targetDate = currentYear.minusYears(age);
            // 将 LocalDate 转换为 java.util.Date
            return Date.from(targetDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }

    private static Integer getAgeFromBrithDate(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        // 计算当前日期与出生日期之间的差值
        Period period = Period.between(birthDate, currentDate);
        return period.getYears() + 1;
    }
}
