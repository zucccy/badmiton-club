package com.yun.springbootinit.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @Author: chenyun
 * @Date: 2025/2/25 22:17
 * @version: 1.0
 */
public enum MemberAthleteLevelEnum {

    NATIONAL("国家级", "national"),
    PROVINCIAL("省级", "provincial"),
    CITY("市级", "city"),
    COUNTY("县级", "county");

    private final String text;

    private final String value;

    MemberAthleteLevelEnum(String text, String value) {
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
    public static MemberAthleteLevelEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (MemberAthleteLevelEnum anEnum : MemberAthleteLevelEnum.values()) {
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
    public static MemberAthleteLevelEnum getEnumByText(String text) {
        if (ObjectUtils.isEmpty(text)) {
            return null;
        }
        for (MemberAthleteLevelEnum anEnum : MemberAthleteLevelEnum.values()) {
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
