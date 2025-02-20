package com.yun.springbootinit.utils;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: cheny
 * @Description: Excel相关工具类
 * @Date: 2023/7/20 0:31
 */
@Slf4j
public class ExcelUtils {

    /**
     * 读取excel文件并转换为csv
     *
     * @param multipartFile
     * @return
     * @throws FileNotFoundException
     */
    public static String excelToCsv(MultipartFile multipartFile) throws FileNotFoundException {
//        File file = ResourceUtils.getFile("classpath:网站数据.xlsx");
        List<Map<Integer, String>> list = null;
        try (InputStream inputStream = multipartFile.getInputStream()) {
            list = EasyExcel.read(inputStream)
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            log.error("Excel表格处理错误", e);
        }

        // 如果list为空，则直接返回
        if (CollUtil.isEmpty(list)) {
            return "";
        }
        // 转换为csv格式并拼接字符串
        StringBuilder stringBuilder = new StringBuilder();
        // 读取表头
        LinkedHashMap<Integer, String> headerMap = (LinkedHashMap) list.get(0);
        // 使用stream流中filter过滤headerMap的所有value中为null的对象，然后组成list
        List<String> headerList = headerMap.values().stream().filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
        // 使用StringUtils.join()方法拼接headerList中每个对象，使用分隔符","，使用stringBuilder.append方法将字符串拼接起来，每行结尾添加换行符"\n"
        stringBuilder.append(StringUtils.join(headerList, ",")).append("\n");
//        System.out.println(StringUtils.join(headerList, ","));
        // 读取每行数据
        for (int i = 1; i < list.size(); i++) {
            LinkedHashMap<Integer, String> dataMap = (LinkedHashMap) list.get(i);
            // 获取dataMap的value中不为null的对象，并组成list
            List<String> dataList = dataMap.values().stream().filter(ObjectUtils::isNotEmpty).collect(Collectors.toList());
            // 使用stringBuilder.append方法将字符串拼接起来，每行结尾添加换行符"\n"
            stringBuilder.append(StringUtils.join(dataList, ",")).append("\n");
//            System.out.println(StringUtils.join(dataList, ","));
        }
//        System.out.println(list);
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {
//        excelToCsv(null);
    }
}
