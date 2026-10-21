package com.retr0.apispringboot.common;

import java.io.Serializable;
/**
 * 统一响应结构。
 *
 * @param code    业务码（ErrorCode 的名字，如 OK、NOT_FOUND）
 * @param message 给调用方看的说明
 * @param data    业务数据；失败时可放错误明细，没有就是 null
 */
public record ApiResponse<T>(String code, String message, T data) implements Serializable {
        public static <T> ApiResponse<T> success(T data) {
            return new ApiResponse<>(ErrorCode.OK.name(), ErrorCode.OK.getMessage(), data);
        }
         public static <T> ApiResponse<Void> success() {
        return new ApiResponse<>(ErrorCode.OK.name(), ErrorCode.OK.getMessage(), null);
    }
    public static <T> ApiResponse<T> fail(ErrorCode errorCode, String message) {
        return new ApiResponse<>(errorCode.name(), message, null);
    }

    /** 失败，带明细。比如参数校验失败时，data 里放“字段 -> 错误信息”。 */
    public static <T> ApiResponse<T> fail(ErrorCode errorCode, String message, T data) {
        return new ApiResponse<>(errorCode.name(), message, data);
    }
}
