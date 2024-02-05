package com.eva.core.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * 摘要工具类
 */
public class DigestUtil {

    /**
     * 获取手机号码摘要
     * 逻辑：前3个数字 + **** + 后4个数字
     *
     * @param mobile 手机号码
     * @return 手机号摘要
     */
    public String digestMobile (String mobile) {
        if (StringUtils.isBlank(mobile)) {
            throw new IllegalArgumentException("手机号码格式不正确");
        }
        if (mobile.length() != 11) {
            throw new IllegalArgumentException("手机号码格式不正确");
        }
        return mobile.substring(0, 3) + "****" + mobile.substring(7);
    }

    /**
     * 获取邮箱摘要
     * 逻辑：获取前缀，前缀第一个字符 + **** + 前缀最后一个字符 + 邮箱后缀
     *
     * @param email 邮箱
     * @return 邮箱摘要
     */
    public String digestEmail (String email) {
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("邮箱格式不正确");
        }
        // 将邮箱根据最后一个@符号分段，获取到邮箱后缀
        String[] emailArr = StringUtils.split(email, "@");
        if (emailArr.length < 2) {
            throw new IllegalArgumentException("邮箱格式不正确");
        }
        String suffix = emailArr[emailArr.length - 1];
        // 将前缀拼接成字符串
        StringBuilder prefix = new StringBuilder();
        for (int i = 0; i < emailArr.length - 1; i++) {
            prefix.append(emailArr[i]);
        }
        // 没有前缀，邮箱格式不正确
        if (prefix.length() == 0) {
            throw new IllegalArgumentException("邮箱格式不正确");
        }
        // 只有一个字符的邮箱，如a@qq.com，则为a****@qq.com
        if (prefix.length() == 1) {
            return prefix + "****" + suffix;
        }
        // 1个字符以上的邮箱，如ab@qq.com，则为a****b@qq.com
        return prefix.substring(0, 1) + "****" + prefix.substring(prefix.length() - 1) + "@" + suffix;
    }
}
