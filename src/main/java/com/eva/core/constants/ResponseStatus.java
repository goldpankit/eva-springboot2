package com.eva.core.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 响应状态定义
 */
@Getter
@AllArgsConstructor
public enum ResponseStatus {
    // 400开头表示参数错误
    BAD_REQUEST(4000, "参数错误"),
    DATA_EMPTY(4001, "找不到数据，该数据可能已被删除"),
    DATA_EXISTS(4002, "记录已存在"),
    DATA_ERROR(4003, "数据错误"),
    PWD_INCORRECT(4004, "密码不正确"),
    VERIFICATION_CODE_INCORRECT(4005, "验证码不正确或已过期"),
    ACCOUNT_INCORRECT(4006, "账号或密码不正确"),
    LOCAL_FILE_NOT_EXISTS(4007, "文件不存在"),
    PRIVILEGE_ERROR(4008, "疑似存在非法权限提升或不是最新数据，请刷新数据后重试"),
    TWO_FA_INCORRECT(4009, "登录密码不正确"),
    TWO_FA_REQUIRED(4010, "需要进行2FA认证"),
    TWO_FA_FAILED(4011, "2FA认证失败"),
    // 500开头表示未知的服务异常
    SERVER_ERROR(5000, "系统繁忙，请联系系统管理员"),
    EXPORT_EXCEL_ERROR(5010, "导出Excel失败，请联系系统管理员"),
    IMPORT_EXCEL_ERROR(5011, "导入Excel失败，请联系系统管理员"),
    // 510开头表示可能导致数据错误的异常
    REPEAT_REQUEST(5100, "请勿重复提交"),
    MASSIVE_REQUEST(5101, "请求过于频繁"),
    NOT_ALLOWED(5110, "不允许的操作"),
    ;

    private final int code;

    private final String message;
}
