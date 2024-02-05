package com.eva.core.prevent;

import com.eva.core.constants.Constants;
import com.eva.service.common.CacheProxy;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 防重复处理实现适配器
 */
public abstract class PreventRepeatHandlerAdapter {

    @Resource
    private CacheProxy<String, Object> cacheProxy;

    /**
     * 验证是否重复
     *
     * @param request 请求对象
     * @param interval 间隔
     * @return Boolean
     */
    public Boolean isRepeat(HttpServletRequest request, int interval) {
        String requestKey = Constants.CacheKey.REPEAT_REQUEST_PREFIX + this.sign(request);
        boolean isRepeat = cacheProxy.get(requestKey) != null;
        if (!isRepeat) {
            cacheProxy.put(requestKey, Byte.MIN_VALUE, Long.valueOf(interval));
        }
        return isRepeat;
    }


    /**
     * 参数签名，签名作为请求唯一标识
     *
     * @param request 请求对象
     * @return String
     */
    public abstract String sign (HttpServletRequest request);

}
