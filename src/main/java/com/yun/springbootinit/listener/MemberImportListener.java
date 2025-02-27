package com.yun.springbootinit.listener;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yun.springbootinit.common.ErrorCode;
import com.yun.springbootinit.constant.CommonConstant;
import com.yun.springbootinit.exception.BusinessException;
import com.yun.springbootinit.manage.ConfigManage;
import com.yun.springbootinit.model.dto.excel.ErrorInfo;
import com.yun.springbootinit.model.dto.member.MemberImportData;
import com.yun.springbootinit.model.entity.Club;
import com.yun.springbootinit.model.entity.Member;
import com.yun.springbootinit.model.entity.User;
import com.yun.springbootinit.model.enums.*;
import com.yun.springbootinit.service.IClubService;
import com.yun.springbootinit.service.IMemberService;
import com.yun.springbootinit.service.IUserService;
import com.yun.springbootinit.utils.ValidatorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Description: TODO
 * @Author: chenyun
 * @Date: 2025/2/26 22:36
 * @version: 1.0
 */
public class MemberImportListener extends AnalysisEventListener<MemberImportData> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberImportListener.class);

    private static final int BATCH_SIZE = 200;

    private final List<Member> cacheList = new ArrayList<>();

    private final List<ErrorInfo> errorInfoList = new ArrayList<>();

    private int processedNum = 0;

    private final String defaultPassword;

    private final IMemberService memberService;

    private final IClubService clubService;

    private final IUserService userService;

    public MemberImportListener(IMemberService memberService, IClubService clubService, IUserService userService, ConfigManage configManage) {
        this.memberService = memberService;
        this.clubService = clubService;
        this.userService = userService;
        this.defaultPassword = configManage.getDefaultPassword();
    }

    public String getDefaultPassword() {
        return defaultPassword;
    }

    @Override
    public void invoke(MemberImportData memberImportData, AnalysisContext analysisContext) {
        try {
            // 字段校验
            ValidatorUtils.validate(memberImportData);
            if (checkPhoneExisted(memberImportData)) {
                throw new Exception("手机号已存在");
            }
            Member member = importDataToEntity(memberImportData);
            this.cacheList.add(member);
            this.processedNum++;
            if (this.cacheList.size() >= BATCH_SIZE) {
                batchSave(this.cacheList);
                this.cacheList.clear();
            }

        } catch (Exception e) {
            this.errorInfoList.add(ErrorInfo.builder()
                    .rowNum(analysisContext.readRowHolder().getRowIndex() + 1)
                    .message(e.getMessage())
                    .build());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        try {
            if (CollectionUtil.isNotEmpty(this.cacheList)) {
                batchSave(this.cacheList);
            }
        } catch (Exception e) {
            this.errorInfoList.add(ErrorInfo.builder()
                    .rowNum(analysisContext.readRowHolder().getRowIndex() + 1)
                    .message(e.getMessage())
                    .build());
            LOGGER.error("message: {}", e.getMessage());
        } finally {
            this.cacheList.clear();
        }
    }

    public void batchSave(List<Member> cacheList) {
        memberService.saveBatch(cacheList);
    }

    private boolean checkPhoneExisted(MemberImportData memberImportData) {
        // 手机号唯一性校验
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", memberImportData.getPhone());
        return memberService.count(queryWrapper) > 0;
    }

    private void handleUserAndClub(MemberImportData memberImportData, Member member) throws Exception{
        if (memberImportData.getCurrentClubName() != null) {
            QueryWrapper<Club> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("club_name", memberImportData.getCurrentClubName());
            Club club = clubService.getOne(queryWrapper);
            if (club == null) {
                // 创建club
                club = new Club();
                club.setClubName(memberImportData.getCurrentClubName());
                boolean saveResult = clubService.save(club);
                if (!saveResult) {
                    throw new BusinessException(ErrorCode.SYSTEM_ERROR, "俱乐部插入失败，数据库错误");
                }
            }
            member.setCurrentClubId(club.getId());
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", memberImportData.getPhone());
        User user = userService.getOne(queryWrapper);
        Long userId;
        if (user == null) {
            userId = userService.userRegister(memberImportData.getPhone(), getDefaultPassword(), getDefaultPassword());
        } else {
            userId = user.getId();
        }
        member.setUserId(userId);
    }

    private void handleEnum(MemberImportData memberImportData, Member member) throws Exception {
        if (memberImportData.getIsCivilServant() != null) {
            if (CommonConstant.TRUE.equals(memberImportData.getIsCivilServant())) {
                member.setIsCivilServant(Boolean.TRUE);
            } else if (CommonConstant.FALSE.equals(memberImportData.getIsCivilServant())) {
                member.setIsCivilServant(Boolean.FALSE);
            } else {
                throw new Exception("是否公务员只能为是/否");
            }
        }
        if (memberImportData.getIsCadre() != null) {
            if (CommonConstant.TRUE.equals(memberImportData.getIsCadre())) {
                member.setIsCadre(Boolean.TRUE);
            } else if (CommonConstant.FALSE.equals(memberImportData.getIsCadre())) {
                member.setIsCadre(Boolean.FALSE);
            } else {
                throw new Exception("是否科局级及以上只能为是/否");
            }
        }
        if (memberImportData.getIsVeteran() != null) {
            if (CommonConstant.TRUE.equals(memberImportData.getIsVeteran())) {
                member.setIsVeteran(Boolean.TRUE);
            } else if (CommonConstant.FALSE.equals(memberImportData.getIsVeteran())) {
                member.setIsVeteran(Boolean.FALSE);
            } else {
                throw new Exception("是否退役军人只能为是/否");
            }
        }
        if (memberImportData.getGender().equals(CommonConstant.MALE)) {
            member.setGender(GenderEnum.MALE.getValue());
        } else if (memberImportData.getGender().equals(CommonConstant.FEMALE)) {
            member.setGender(GenderEnum.FEMALE.getValue());
        } else {
            throw new Exception("性别只能为男/女");
        }
        if (memberImportData.getAthleteLevel() != null) {
            if (MemberAthleteLevelEnum.getEnumByText(memberImportData.getAthleteLevel()) == null) {
                throw new Exception("专业运动员等级只能为国家级/省级/市级/县级");
            } else {
                member.setAthleteLevel(Objects.requireNonNull(MemberAthleteLevelEnum.getEnumByText(memberImportData.getAthleteLevel())).getValue());
            }
        }
        if (memberImportData.getRefereeLevel() != null) {
            if (MemberRefereeLevelEnum.getEnumByText(memberImportData.getRefereeLevel()) == null) {
                throw new Exception("裁判员等级只能为国家级/一级/二级/三级");
            } else {
                member.setRefereeLevel(Objects.requireNonNull(MemberRefereeLevelEnum.getEnumByText(memberImportData.getRefereeLevel())).getValue());
            }
        }
        if (memberImportData.getResidenceArea() != null) {
            if (MemberResidenceEnum.getEnumByText(memberImportData.getResidenceArea()) == null) {
                throw new Exception("人员归属地只能为龙港/苍南/平阳/温州市内/浙江省内/浙江省外");
            } else {
                member.setResidenceArea(Objects.requireNonNull(MemberResidenceEnum.getEnumByText(memberImportData.getResidenceArea())).getValue());
            }
        }
        if (memberImportData.getCurrentLevel() != null) {
            if (ClubLevelEnum.getEnumByText(memberImportData.getCurrentLevel()) == null) {
                throw new Exception("当前组别只能为甲组/乙组/丙组");
            } else {
                member.setCurrentLevel(Objects.requireNonNull(ClubLevelEnum.getEnumByText(memberImportData.getCurrentLevel())).getValue());
            }
        }
    }

    private Member importDataToEntity(MemberImportData memberImportData) throws Exception {
        Member member = new Member();
        BeanUtils.copyProperties(memberImportData, member);
        // 处理出生日期
        member.setBirthDate(LocalDate.parse(memberImportData.getBirthDate()));
        handleUserAndClub(memberImportData, member);
        handleEnum(memberImportData, member);
        return member;
    }

    public List<ErrorInfo> getErrorInfoList() {
        return this.errorInfoList;
    }

    public int getProcessedNum() {
        return this.processedNum;
    }
}
