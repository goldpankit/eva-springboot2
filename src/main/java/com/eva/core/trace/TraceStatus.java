package com.eva.core.trace;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 跟踪状态
 */
@Getter
@AllArgsConstructor
public enum TraceStatus {
    SUCCESS((byte)1, "成功"),
    FAILED((byte)0, "失败"),
    ;

    private final byte code;

    private final String remark;
}
