package com.eva.core.secure;

import java.nio.charset.StandardCharsets;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.eva.core.utils.Utils;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * AES加解密
 */
@Data
@Component
public class AESUtil {

    public AESUtil() {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 加密
     *
     * @param plainText 明文
     * @param key 密钥
     * @param keyLen 密钥长度
     * @param iv 向量
     * @return String
     */
    public String encrypt(String plainText, String key, int keyLen, String iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE,
                    new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES"),
                    new IvParameterSpec(paddingIv(iv, keyLen)));
            return new Base64()
                    .encodeAsString(cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new SecurityException("AES encrypt throw an exception", e);
        }
    }

    /**
     * 解密
     *
     * @param cipherText 密文
     * @param key 密钥
     * @param keyLen 密钥长度
     * @param iv 向量
     * @return String
     */
    public String decrypt(String cipherText, String key, int keyLen, String iv) {
        try {
            byte[] encrypted = new Base64().decode(cipherText);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            cipher.init(Cipher.DECRYPT_MODE,
                    new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES"),
                    new IvParameterSpec(paddingIv(iv, keyLen)));
            return new String(cipher.doFinal(encrypted), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new SecurityException("AES decrypt throw an exception", e);
        }
    }

    /**
     * 加密数据
     *
     * @return String
     */
    public String encryptData(String plainText) throws SecurityException {
        return this.encrypt(plainText,
                Utils.AppConfig.getSecurity().getData().getKey(),
                Utils.AppConfig.getSecurity().getData().getKeyLen(),
                Utils.AppConfig.getSecurity().getData().getIv());
    }

    /**
     * 解密传输参数
     *
     * @param cipherText 密文
     * @return String
     */
    public String decryptData(String cipherText) throws SecurityException {
        return this.decrypt(cipherText,
                Utils.AppConfig.getSecurity().getData().getKey(),
                Utils.AppConfig.getSecurity().getData().getKeyLen(),
                Utils.AppConfig.getSecurity().getData().getIv());
    }

    /**
     * 加密传输参数
     *
     * @return String
     */
    public String encryptTransmission(String plainText) throws SecurityException {
        return this.encrypt(plainText,
                Utils.AppConfig.getSecurity().getTransmission().getKey(),
                Utils.AppConfig.getSecurity().getTransmission().getKeyLen(),
                Utils.AppConfig.getSecurity().getTransmission().getIv());
    }

    /**
     * 解密数据
     *
     * @param cipherText 密文
     * @return String
     */
    public String decryptTransmission(String cipherText) throws SecurityException {
        return this.decrypt(cipherText,
                Utils.AppConfig.getSecurity().getTransmission().getKey(),
                Utils.AppConfig.getSecurity().getTransmission().getKeyLen(),
                Utils.AppConfig.getSecurity().getTransmission().getIv());
    }

    /**
     * 初始化向量一直保持16位
     *
     * @param iv 向量
     * @return byte[]
     */
    private byte[] paddingIv(String iv, Integer keyLen) {
        byte[] ivBytes = iv.getBytes(StandardCharsets.UTF_8);
        byte[] bs = new byte[keyLen / 8];
        System.arraycopy(ivBytes, 0, bs, 0, ivBytes.length);
        return bs;
    }
}
