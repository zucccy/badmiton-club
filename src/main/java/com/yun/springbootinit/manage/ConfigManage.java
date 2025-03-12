package com.yun.springbootinit.manage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Description: TODO
 * @Author: chenyun
 * @Date: 2025/2/28 0:42
 * @version: 1.0
 */
@Component
public class ConfigManage {

    private static String defaultPassword;

    private static String secretKey;

    @Value("${app.default-password}")
    public void setDefaultPassword(String value) {
        ConfigManage.defaultPassword = value;
    }

    @Value("${app.aes.secret-key}")
    public void setSecretKey(String value) {
        ConfigManage.secretKey = value;
    }

    public static String getDefaultPassword() {
        return defaultPassword;
    }

    public static String getSecretKey() {
        return secretKey;
    }
}
