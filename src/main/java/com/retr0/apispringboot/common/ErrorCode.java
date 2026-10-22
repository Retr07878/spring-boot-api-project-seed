package com.retr0.apispringboot.common;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    OK(HttpStatus.OK,"success"),
    INVALID_PARAM(HttpStatus.BAD_REQUEST, "请求参数不合法"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "未认证"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "没有权限"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "资源不存在"),
    CONFLICT(HttpStatus.CONFLICT, "数据冲突"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "服务器内部错误");
    private final HttpStatus httpStatus;
    private final String message;
    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
    public String getMessage() {
        return message;
    }
}
