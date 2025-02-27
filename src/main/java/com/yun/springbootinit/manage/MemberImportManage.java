package com.yun.springbootinit.manage;

import com.alibaba.excel.EasyExcel;
import com.yun.springbootinit.common.ErrorCode;
import com.yun.springbootinit.exception.BusinessException;
import com.yun.springbootinit.listener.MemberImportListener;
import com.yun.springbootinit.model.dto.member.MemberImportData;
import com.yun.springbootinit.model.vo.ImportResultVO;
import com.yun.springbootinit.service.IClubService;
import com.yun.springbootinit.service.IMemberService;
import com.yun.springbootinit.service.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Description: TODO
 * @Author: chenyun
 * @Date: 2025/2/28 0:48
 * @version: 1.0
 */
@Component
public class MemberImportManage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberImportManage.class);
    @Resource
    private IClubService clubService;

    @Resource
    private IUserService userService;

    @Resource
    private IMemberService memberService;

    @Resource
    private ConfigManage configManage;

    @Transactional(rollbackFor = Exception.class)
    public ImportResultVO importMember(MultipartFile file) {
        MemberImportListener listener = new MemberImportListener(memberService, clubService, userService, configManage);
        try {
            EasyExcel.read(file.getInputStream(), MemberImportData.class, listener)
                    .sheet()
                    .doRead();
            return new ImportResultVO(listener.getProcessedNum(), listener.getErrorInfoList());
        } catch (IOException e) {
            LOGGER.error("file read error, file name: {}", file.getOriginalFilename());
            throw new BusinessException(ErrorCode.FILE_OPERATE_ERROR, "文件读取失败");
        }
    }
}
