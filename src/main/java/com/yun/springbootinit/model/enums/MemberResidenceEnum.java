package com.yun.springbootinit.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @Author: chenyun
 * @Date: 2025/2/25 22:24
 * @version: 1.0
 */
public enum MemberResidenceEnum {

    LONGGANG("龙港", "long_gang"),
    CANGNAN("苍南", "cang_nan"),
    PINGYANG("平阳", "ping_yang"),
    WENZHOU("温州市内", "wen_zhou"),
    ZHEJIANG("浙江省内", "zhe_jiang"),
    OTHER("浙江省外", "other");

    private final String text;

    private final String value;

    MemberResidenceEnum(String text, String value) {
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
    public static MemberResidenceEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (MemberResidenceEnum anEnum : MemberResidenceEnum.values()) {
            if (anEnum.value.equals(value)) {
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
