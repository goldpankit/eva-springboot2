package com.eva.core.secure;

import com.alibaba.fastjson.JSON;
import com.eva.core.model.ApiResponse;
import com.eva.core.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 安全处理工具类
 */
@Slf4j
@Component
public class SecureUtil {

    /**
     * 加密字段
     *
     * @param plainText 明文
     * @return 加密后的字段值
     */
    public String encryptField(String plainText) {
        return Utils.AES.encryptData(plainText);
    }

    /**
     * 解密字段
     *
     * @param cipherText 密文
     * @return 解密后的字段值
     */
    public String decryptField(String cipherText) {
        try {
            return Utils.AES.decryptData(cipherText);
        } catch (SecurityException e) {
            return cipherText;
        }
    }

    /**
     * 加密密码，公式为：MD5(MD5(password) + salt)
     *
     * @param password 密码
     * @param salt 密码盐
     * @return String
     */
    public String encryptPassword(String password, String salt) {
        return this.encryptMD5Password(DigestUtils.md5DigestAsHex(password.getBytes()), salt);
    }

    /**
     * 加密密码
     *
     * @param md5Password 密码
     * @param salt 密码盐
     * @return String
     */
    public String encryptMD5Password(String md5Password, String salt) {
        return DigestUtils.md5DigestAsHex((md5Password + salt).getBytes());
    }

    /**
     * 是否为安全请求
     *
     * @param request 请求对象
     * @return boolean
     */
    public boolean isSecureRequest(HttpServletRequest request) {
        // 上传文件
        if (request.getContentType() != null && request.getContentType().contains("multipart/form-data")) {
            return Boolean.FALSE;
        }
        // Swagger请求不做加解密处理
        String requestOrigion = request.getHeader("Request-Origion");
        if("Knife4j".equals(requestOrigion)
                && Utils.AppConfig.getApiDoc().getEnabled()) {
            return Boolean.FALSE;
        }
        // URI被排除
        String uri = request.getRequestURI().replace(request.getContextPath(), "");
        boolean excluded = false;
        for (String excludePathPattern : Utils.AppConfig.getSecurity().getTransmission().getExcludePathPatterns()) {
            excludePathPattern = StringUtils.replace(excludePathPattern,"**", ".*");
            if (uri.matches(excludePathPattern)) {
                excluded = true;
                break;
            }
        }
        if (excluded) {
            return Boolean.FALSE;
        }
        // URI不匹配
        boolean matched = false;
        for (String pathPattern : Utils.AppConfig.getSecurity().getTransmission().getPathPatterns()) {
            pathPattern = StringUtils.replace(pathPattern,"**", ".*");
            if (uri.matches(pathPattern)) {
                matched = true;
                break;
            }
        }
        if (matched) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * 加密请求参数
     *
     * @param request 请求参数
     * @return String
     */
    public String encryptRequest(String request) {
        return Utils.AES.encryptTransmission(request);
    }

    /**
     * 解密请求参数
     *
     * @param request 请求参数
     * @return String
     */
    public String decryptTransmission(String request) {
        return Utils.AES.decryptTransmission(request);
    }

    /**
     * 加密响应内容
     *
     * @param response 响应内容
     * @return String
     */
    public String encryptTransmission(String response) {
        return Utils.AES.encryptTransmission(response);
    }

    /**
     * 解密响应内容
     *
     * @param response 响应内容
     * @return ApiResponse
     */
    public ApiResponse<?> decryptResponse (String response) {
        String responseBody = Utils.AES.decryptTransmission(response);
        return JSON.parseObject(responseBody, ApiResponse.class);
    }
}
