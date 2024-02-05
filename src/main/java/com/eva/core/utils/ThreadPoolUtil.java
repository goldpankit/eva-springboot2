package com.eva.core.utils;

import java.util.concurrent.Executors;

/**
 * 线程池处理
 */
public class ThreadPoolUtil {

    /**
     * 开启新线程并启动
     *
     * @param runnable Runnable
     */
    public void start (Runnable runnable) {
        Executors.defaultThreadFactory().newThread(runnable).start();
    }
}
