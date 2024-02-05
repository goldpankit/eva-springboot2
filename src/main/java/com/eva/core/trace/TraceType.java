package com.eva.core.trace;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 跟踪类型
 */
@Getter
@AllArgsConstructor
public enum TraceType {
    AUTO("AUTO", "自动识别"),
    CREATE("CREATE", "新建"),
    UPDATE("UPDATE", "修改"),
    DELETE("DELETE", "删除"),
    DELETE_BATCH("DELETE_BATCH", "批量删除"),
    IMPORT("IMPORT", "导入"),
    EXPORT("EXPORT", "导出"),
    RESET("RESET", "重置"),
    UNKNOWN("UNKNOWN", "未知操作"),
    ;

    /**
     * 跟踪类型
     */
    private final String type;

    /**
     * 跟踪备注
     */
    private final String remark;
}
