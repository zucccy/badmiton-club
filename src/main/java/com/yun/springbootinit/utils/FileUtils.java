package com.yun.springbootinit.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.yun.springbootinit.common.ErrorCode;
import com.yun.springbootinit.exception.BusinessException;
import com.yun.springbootinit.model.enums.FileUploadBizEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: cheny
 * @Description: file相关工具类
 * @Date: 2023/7/20 0:31
 */
public class FileUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private static final long ONE_HUNDRED_MB = 100 * 1024 * 1024L;
    /**
     * 校验文件
     *
     * @param file
     */
    public static void validFile(MultipartFile file) {
        // 文件大小
        long fileSize = file.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(file.getOriginalFilename());
        if (fileSize > ONE_HUNDRED_MB) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过100MB");
        }
        if (!Arrays.asList(".xlsx", ".xls").contains(fileSuffix)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误，只支持.xlsx和.xls结尾的文件");
        }
    }
}
