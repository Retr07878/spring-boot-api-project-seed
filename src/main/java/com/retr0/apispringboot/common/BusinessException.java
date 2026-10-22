package com.retr0.apispringboot.common;

/**
 * 业务异常。
 *
 * <p>表示"业务规则不满足"（用户不存在、用户名重复……），
 * 由 {@link GlobalExceptionHandler} 统一翻译成 HTTP 响应。
 */
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Object data;

    public BusinessException(ErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }

    public BusinessException(ErrorCode errorCode, String message, Object data) {
        super(message);
        this.errorCode = errorCode;
        this.data = data;
    }

    public static BusinessException notFound(String message) {
        return new BusinessException(ErrorCode.NOT_FOUND, message);
    }

    public static BusinessException conflict(String message) {
        return new BusinessException(ErrorCode.CONFLICT, message);
    }

    public static BusinessException invalidParam(String message) {
        return new BusinessException(ErrorCode.INVALID_PARAM, message);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Object getData() {
        return data;
    }
}
