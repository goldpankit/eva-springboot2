package com.eva.core.excel;

import java.util.List;

/**
 * 数据导入回调
 */
public interface ExcelImportCallback<T> {

    /**
     * 导入回调方法
     *
     * @param rows 数据行
     * @param sync 是否同步已存在数据
     * @return 同步成功数
     */
    int callback(List<T> rows, boolean sync);
}
