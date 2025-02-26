package com.yun.springbootinit.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @Author: chenyun
 * @Date: 2025/2/25 22:22
 * @version: 1.0
 */
public enum MemberRefereeLevelEnum {

    NATIONAL("国家级", "national"),
    FIRST("一级", "first"),
    SECOND("二级", "second"),
    THIRD("三级", "third");

    private final String text;

    private final String value;

    MemberRefereeLevelEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static MemberRefereeLevelEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (MemberRefereeLevelEnum anEnum : MemberRefereeLevelEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 根据 text 获取枚举
     *
     * @param text
     * @return
     */
    public static MemberRefereeLevelEnum getEnumByText(String text) {
        if (ObjectUtils.isEmpty(text)) {
            return null;
        }
        for (MemberRefereeLevelEnum anEnum : MemberRefereeLevelEnum.values()) {
            if (anEnum.text.equals(text)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
