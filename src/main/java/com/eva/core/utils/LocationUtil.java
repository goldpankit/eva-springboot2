package com.eva.core.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.nio.charset.Charset;

/**
 * 地区工具类
 * 第三方服务：<a href="http://whois.pconline.com.cn/">whois</a>
 */
@Slf4j
public class LocationUtil {

    // 地区API
    private static final String GET_LOCATION_API = "http://whois.pconline.com.cn/ipJson.jsp?json=true&ip=%s";

    /**
     * 获取地区信息
     *
     * @param ip IP
     * @return Info
     */
    public Info getLocation (String ip) {
        try {
            return Utils.Http.build(String.format(GET_LOCATION_API, ip), Charset.forName("GBK").toString())
                    .setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9")
                    .setRequestProperty("Accept-Encoding", "gzip, deflate")
                    .gzip()
                    .get()
                    .toClass(Info.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 获取IP详细地址
     *
     * @param ip IP
     * @return String
     */
    public String getLocationString (String ip) {
        Info info = this.getLocation(ip);
        if (info == null) {
            return "UNKNOWN";
        }
        return info.getAddr();
    }

    /**
     * 地区信息包装
     */
    @Data
    public static class Info implements Serializable {

        @ApiModelProperty("省")
        private String pro;

        @ApiModelProperty("省编码")
        private String proCode;

        @ApiModelProperty("市")
        private String city;

        @ApiModelProperty("省编码")
        private String cityCode;

        @ApiModelProperty("详细地址")
        private String addr;
    }
}
