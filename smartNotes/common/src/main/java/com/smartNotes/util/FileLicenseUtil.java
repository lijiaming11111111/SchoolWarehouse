package com.smartNotes.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * License许可生成与验证工具类
 */
@Component
@Slf4j
public class FileLicenseUtil {

    // 签名用的秘钥（必须保密，且不能泄露）
    @Value("${file.pre-signature.secret-key}")
    private String secretKey;

    /**
     * 预签名URL过期时间
     */
    @Value("${file.pre-signature.expire}")
    private Integer expire;

    @Value("${file.url}")
    private String url;

    /**
     * 生成license签名
     * @param fileId 文件ID
     * @param userId 用户ID
     * @param expireTime 过期时间戳
     * @return Base64编码后的license字符串
     */
    public String generateLicense(Long fileId, Long userId, long expireTime) throws Exception {
        String data = fileId + ":" + userId + ":" + expireTime;
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(
                secretKey.trim().getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );
        mac.init(keySpec);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }

    /**
     * 验证license签名是否合法
     */
    public boolean verifyLicense(Long fileId, Long userId, long expireTime, String license) throws Exception {
        String expected = generateLicense(fileId, userId, expireTime);
        return expected.equals(license);
    }

    /**
     * 生成预签名URL
     * @param fileId 文件ID
     * @param userId 用户ID
     * @return 预签名URL
     * @throws Exception 生成签名异常
     */
    public String generateSignedUrl(Long fileId, Long userId) throws Exception {
        long expireTime = System.currentTimeMillis() + expire * 3600000;
        String license = generateLicense(fileId, userId, expireTime);
        return String.format(url + "?fileId=%s&license=%s&expire=%d", fileId, license, expireTime);
    }
}
