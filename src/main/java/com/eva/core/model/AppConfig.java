package com.eva.core.model;

import com.eva.core.constants.Constants;
import com.eva.service.common.CacheProxy;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 项目属性配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app", ignoreInvalidFields = true)
public class AppConfig {

    // 生产环境
    private static final String ENV_PRODUCTION = "production";

    // 测试模式
    private static final String MODE_TESTING = "testing";

    @Resource
    private CacheProxy<String, Map<String, SystemConfigCache>> cacheProxy;

    @ApiModelProperty("模式")
    private String mode;

    @ApiModelProperty("环境")
    private String env;

    @ApiModelProperty("版本")
    private String version;

    @ApiModelProperty("超级管理员角色编码")
    private String superAdminRole;

    @Resource
    @ApiModelProperty("会话配置")
    private SessionConfig session;

    @Resource
    @ApiModelProperty("验证码配置")
    private Captcha captcha;

    @Resource
    @ApiModelProperty("安全配置")
    private Security security;

    @Resource
    @ApiModelProperty("接口文档配置")
    private ApiDocConfig apiDoc;

    @Resource
    @ApiModelProperty("跟踪日志")
    private Trace trace;

    /**
     * 获取系统配置内容
     *
     * @param key 配置编码
     * @return 配置值
     */
    public String get (String key) {
        SystemConfigCache cache = cacheProxy
                .get(Constants.CacheKey.SYSTEM_CONFIGS)
                .get(key);
        if (cache == null) {
            return null;
        }
        return cache.getValue();
    }

    /**
     * 判断是否为测试模式，当前测试模式下完成以下事项
     * - 不校验验证码
     *
     * @return boolean
     */
    public boolean isTestingMode () {
        // 生产环境无测试模式
        if (ENV_PRODUCTION.equals(env)) {
            return Boolean.FALSE;
        }
        return MODE_TESTING.equalsIgnoreCase(mode);
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "project.session", ignoreInvalidFields = true)
    @ApiModel("会话配置")
    public static class SessionConfig {

        @ApiModelProperty("会话超时时间")
        private Integer expire;

        @ApiModelProperty("会话模式")
        private String mode;

        @ApiModelProperty("拦截器配置")
        private Interceptor interceptor;

        @Data
        @ApiModel("会话拦截配置")
        public static class Interceptor {

            @ApiModelProperty("拦截路径")
            private String[] pathPatterns;

            @ApiModelProperty("排除拦截路径")
            private String[] excludePathPatterns;
        }
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "project.captcha", ignoreInvalidFields = true)
    @ApiModel("验证码")
    public static class Captcha {

        @Resource
        @ApiModelProperty("图片验证码")
        private ImageCaptchaConfig image;
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "project.captcha.image", ignoreInvalidFields = true)
    @ApiModel("图片验证码配置")
    public static class ImageCaptchaConfig {

        @ApiModelProperty("验证码超时时间")
        private Integer expire;
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "project.security", ignoreInvalidFields = true)
    @ApiModel("安全配置")
    public static class Security {

        @Resource
        @ApiModelProperty("数据安全密钥定义")
        private AES data;

        @Resource
        @ApiModelProperty("数据传输安全密钥定义")
        private SecureTransmission transmission;
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "project.security.data", ignoreInvalidFields = true)
    @ApiModel("AES配置")
    public static class AES {

        @ApiModelProperty("密钥")
        private String key;

        @ApiModelProperty("密钥长度")
        private Integer keyLen;

        @ApiModelProperty("偏移量")
        private String iv;
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "project.security.transmission", ignoreInvalidFields = true)
    @ApiModel("AES配置")
    public static class SecureTransmission {

        @ApiModelProperty("密钥")
        private String key;

        @ApiModelProperty("密钥长度")
        private Integer keyLen;

        @ApiModelProperty("偏移量")
        private String iv;

        @ApiModelProperty("需要加密的接口路径")
        private String[] pathPatterns;

        @ApiModelProperty("不需要加密的接口路径")
        private String[] excludePathPatterns;
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "project.api-doc", ignoreInvalidFields = true)
    @ApiModel("接口文档配置")
    public static class ApiDocConfig {

        @ApiModelProperty(value = "访问路径")
        private String host;

        @ApiModelProperty(value = "接口文档名称")
        private String title;

        @ApiModelProperty(value = "swagger文档描述")
        private String description;

        @ApiModelProperty(value = "启用Swagger，生产环境建议关闭")
        private Boolean enabled;

        @ApiModelProperty(value = "禁用swagger时的重定向地址")
        private String redirectUri;
    }

    @Data
    @Configuration
    @ConfigurationProperties(prefix = "project.trace", ignoreInvalidFields = true)
    @ApiModel("跟踪日志配置")
    public static class Trace {

        @ApiModelProperty("开启智能跟踪模式")
        private Boolean smart;

        @ApiModelProperty("排除跟踪的URL正则")
        private String[] excludePatterns;
    }
}
