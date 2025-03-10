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
    @Value("${app.default-password}")
    private String defaultPassword;

    @Value("${app.aes.secret-key}")
    private String secretKey;

    public String getDefaultPassword() {
        return defaultPassword;
    }

    public String getSecretKey() {
        return secretKey;
    }
}
