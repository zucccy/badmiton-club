package com.yun.springbootinit.utils;

/**
 * @Description: TODO
 * @Author: chenyun
 * @Date: 2025/3/10 22:35
 * @version: 1.0
 */
import com.yun.springbootinit.manage.ConfigManage;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class AESUtils {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String SECRET_KEY; // 密钥，长度必须为 16、24 或 32 字节

    static {
        SECRET_KEY = new ConfigManage().getSecretKey();
    }

    public static String encrypt(String plainText) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        SecureRandom secureRandom = new SecureRandom(SECRET_KEY.getBytes());
        keyGenerator.init(128, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] keyBytes = secretKey.getEncoded();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText) throws Exception {
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedText);
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        SecureRandom secureRandom = new SecureRandom(SECRET_KEY.getBytes());
        keyGenerator.init(128, secureRandom);
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] keyBytes = secretKey.getEncoded();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
