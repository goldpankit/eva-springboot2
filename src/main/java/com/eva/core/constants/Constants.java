package com.eva.core.constants;

/**
 * 框架级常量
 */
public interface Constants {

    // token请求头
    String HEADER_TOKEN = "eva-auth-token";

    // 2fa密码请求头
    String HEADER_2FA_PASSWORD = "X-2fa-Password";

    // 操作类型响应头
    String HEADER_OPERA_TYPE = "eva-opera-type";

    // 下载文件名称响应头
    String HEADER_DOWNLOAD_FILENAME = "eva-download-filename";

    /**
     * 缓存Key
     */
    interface CacheKey {

        // 系统配置，用于将系统配置存储在缓存中
        String SYSTEM_CONFIGS = "SYSTEM_CONFIGS";

        // 字典，用于将字典及数据存储在缓存中
        String DICTIONARIES = "DICTIONARIES";

        // 重复请求，用于防止重复调用时记录请求信息
        String REPEAT_REQUEST_PREFIX = "eva:prevent:repeat";

    }

    /**
     * 系统配置
     */
    interface SystemConfig {
        // 作用域：服务端和客户端
        String SCOPE_SERVER_AND_CLIENT = "SERVER_AND_CLIENT";
    }

    /**
     * 系统字典
     */
    interface SystemDict {

        // 作用域：管理后台
        String SCOPE_BACK_END = "BACK_END";
    }

    /**
     * 系统菜单
     */
    interface SystemMenu {

        // 菜单类型：目录
        String TYPE_DIR = "DIR";

        // 菜单类型：外部链接
        String TYPE_EXTERNAL = "EXTERNAL";

        // 菜单类型：IFrame嵌入
        String TYPE_IFRAME = "IFRAME";
    }

    /**
     * 系统跟踪日志
     */
    interface SystemTraceLog {

        // 高等级异常
        byte EXCEPTION_LEVEL_DANGER = 10;

        // 中等级异常
        byte EXCEPTION_LEVEL_WARN = 5;

        // 低等级异常
        byte EXCEPTION_LEVEL_LOW = 0;
    }

}
