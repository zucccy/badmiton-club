package com.yun.springbootinit.listener;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.yun.springbootinit.common.ErrorCode;
import com.yun.springbootinit.exception.BusinessException;
import com.yun.springbootinit.model.dto.excel.ErrorInfo;
import com.yun.springbootinit.model.dto.member.MemberImportData;
import com.yun.springbootinit.model.entity.Member;
import com.yun.springbootinit.service.impl.MemberServiceImpl;
import com.yun.springbootinit.utils.ValidatorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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

    private final MemberServiceImpl memberService;

    public MemberImportListener(MemberServiceImpl memberService) {
        this.memberService = memberService;
    }

    @Override
    public void invoke(MemberImportData memberImportData, AnalysisContext analysisContext) {
        try {
            // 字段校验
            ValidatorUtils.validate(memberImportData);
            if (memberService.checkPhoneExisted(memberImportData)) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "手机号已存在");
            }
            Member member = memberService.importDataToEntity(memberImportData);
            this.cacheList.add(member);
            if (this.cacheList.size() >= BATCH_SIZE) {
                memberService.batchSave(this.cacheList);
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
                memberService.saveBatch(this.cacheList);
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

    public List<ErrorInfo> getErrorInfoList() {
        return this.errorInfoList;
    }
}
