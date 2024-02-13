package com.eva.core.utils;

import com.eva.core.model.AppConfig;
import com.eva.core.secure.AESUtil;
import com.eva.core.secure.SecureUtil;
import com.eva.core.session.SessionUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 工具类
 */
@Component
public final class Utils {

    /**
     * 应用配置
     */
    public static AppConfig AppConfig;

    /**
     * OSS
     */
    public static OSSUtil OSS;

    /**
     * 字典
     */
    public static DictUtil Dict;

    /**
     * 会话
     */
    public static SessionUtil Session;

    /**
     * Spring上下文
     */
    public static SpringContextUtil SpringContext;

    /**
     * 地区处理
     */
    public static final LocationUtil Location = new LocationUtil();

    /**
     * Http请求处理
     */
    public static final HttpUtil Http = new HttpUtil();

    /**
     * 摘要处理
     */
    public static final DigestUtil Digest = new DigestUtil();

    /**
     * 用户客户端信息
     */
    public static final UserClientUtil User_Client = new UserClientUtil();

    /**
     * 服务端信息
     */
    public static final ServerUtil Server = new ServerUtil();

    /**
     * MyBatis Plus处理
     */
    public static final MyBatisPlus MP = new MyBatisPlus();

    /**
     * 安全处理
     */
    public static SecureUtil Secure;

    /**
     * 日期处理
     */
    public static final DateUtil Date = new DateUtil();

    /**
     * 线程池
     */
    public static final ThreadPoolUtil ThreadPool = new ThreadPoolUtil();

    /**
     * AES
     */
    public static AESUtil AES;

    @Resource
    public void setAppConfig(AppConfig appConfig) {
        Utils.AppConfig = appConfig;
    }

    @Resource
    public void setDict(DictUtil dict) {
        Utils.Dict = dict;
    }

    @Resource(name = "UtilSession")
    public void setSession(SessionUtil session) {
        Utils.Session = session;
    }

    @Resource
    public void setOSS(OSSUtil ossUtil) {
        Utils.OSS = ossUtil;
    }

    @Resource
    public void setSpringContext(SpringContextUtil springContext) {
        Utils.SpringContext = springContext;
    }

    @Resource
    public void setSecure(SecureUtil secure) {
        Utils.Secure = secure;
    }

    @Resource
    public void setAES(AESUtil aes) {
        Utils.AES = aes;
    }

}
