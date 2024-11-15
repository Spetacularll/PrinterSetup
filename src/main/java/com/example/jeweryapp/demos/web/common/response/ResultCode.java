package com.example.jeweryapp.demos.web.common.response;

import lombok.Getter;
import lombok.AllArgsConstructor;

/**
 * 响应码枚举
 */
@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    ERROR(500, "系统异常"),
    PARAM_ERROR(400, "参数错误"),
    BUSINESS_ERROR(501, "业务处理失败"),
    DATA_ERROR(502, "数据异常"),
    VALIDATION_ERROR(503, "验证失败");

    private final Integer code;
    private final String message;
}