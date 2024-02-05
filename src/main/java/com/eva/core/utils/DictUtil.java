package com.eva.core.utils;

import com.eva.core.constants.Constants;
import com.eva.core.model.DictCache;
import com.eva.core.model.DictDataCache;
import com.eva.core.constants.ResponseStatus;
import com.eva.core.exception.BusinessException;
import com.eva.service.common.CacheProxy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component
public final class DictUtil {

    @Resource
    private CacheProxy<String, Map<String, DictCache>> cacheProxy;

    /**
     * 获取字典
     *
     * @param code 字典编码
     * @return DictCache
     */
    public DictCache getDict (String code) {
        return getCache().get(code);
    }

    /**
     * 获取字典数据
     *
     * @param code 字典编码
     * @param dataValue 数据值
     * @return DictDataCache
     */
    public DictDataCache getDictData (String code, String dataValue) {
        DictCache dictCache = this.getDict(code);
        if (dictCache == null) {
            return null;
        }
        for (DictDataCache dictDataCache : dictCache.getDataList()) {
            if (dictDataCache.getValue().equals(dataValue)) {
                return dictDataCache;
            }
        }
        return null;
    }

    /**
     * 获取字典数据标签
     *
     * @param code 字典编码
     * @param dataValue 数据值
     * @return 字典数据标签
     */
    public String getDictDataLabel (String code, String dataValue) {
        DictDataCache dataCache = this.getDictData(code, dataValue);
        if (dataCache != null) {
            return dataCache.getLabel();
        }
        return code;
    }

    /**
     * 断言数据，如果在指定字典中不存在数据，则抛出异常
     *
     * @param code 字典编码
     * @param dataValue 字典数据值
     * @param message 错误消息
     */
    public void assertData (String code, String dataValue, String message) {
        this.assertData(code, dataValue, ResponseStatus.BAD_REQUEST, message);
    }

    /**
     * 断言数据，如果在指定字典中不存在数据，则抛出异常
     *
     * @param code 字典编码
     * @param dataValue 字典数据值
     * @param status 响应状态
     * @param message 错误消息
     */
    public void assertData (String code, String dataValue, ResponseStatus status, String message) {
        DictCache dictCache = this.getDict(code);
        if (dictCache == null) {
            throw new BusinessException(status, message);
        }
        for (DictDataCache dictDataCache : dictCache.getDataList()) {
            if (dictDataCache.getValue().equals(dataValue)) {
                return;
            }
        }
        throw new BusinessException(status, message);
    }

    /**
     * 读取字典缓存数据
     *
     * @return 缓存数据
     */
    private Map<String, DictCache> getCache () {
        return cacheProxy.get(Constants.CacheKey.DICTIONARIES);
    }
}
