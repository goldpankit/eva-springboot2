package com.eva.core.initializer;

import com.eva.biz.system.SystemDictBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 项目启动后加载字典数据至缓存
 */
@Slf4j
@Component
public class SystemDictCacheInitializer implements ApplicationRunner {

    @Resource
    private SystemDictBiz systemDictBiz;

    @Override
    public void run(ApplicationArguments args) {
        systemDictBiz.loadToCache();
    }
}
