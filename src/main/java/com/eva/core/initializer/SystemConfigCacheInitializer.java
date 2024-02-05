package com.eva.core.initializer;

import com.eva.biz.system.SystemConfigBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 项目启动后加载系统配置数据至缓存
 */
@Slf4j
@Component
public class SystemConfigCacheInitializer implements ApplicationRunner {

    @Resource
    private SystemConfigBiz systemConfigBiz;

    @Override
    public void run(ApplicationArguments args) {
        systemConfigBiz.loadToCache();
    }
}
