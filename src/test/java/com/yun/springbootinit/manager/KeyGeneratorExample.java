package com.yun.springbootinit.manager;

/**
 * @Description: TODO
 * @Author: chenyun
 * @Date: 2025/3/10 22:44
 * @version: 1.0
 */
import java.security.SecureRandom;
import java.util.Base64;

public class KeyGeneratorExample {
    public static void main(String[] args) {
        // 生成 24 字节（192 位）的密钥
        byte[] keyBytes = new byte[24];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(keyBytes);

        // 将字节数组转换为 Base64 编码的字符串，方便存储和使用
        String base64EncodedKey = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("生成的 24 字节密钥（Base64 编码）: " + base64EncodedKey);
    }
}
