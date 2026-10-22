package com.retr0.apispringboot.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorcode();
        log.info("业务异常: {} - {}", errorCode.name(), ex.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus()).body(ApiResponse.fail(errorCode, ex.getMessage(), ex.getData()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleUnexpected(Exception ex) {
        // 未知异常必须打印完整堆栈（error 级别），给开发排查用
        log.error("未处理异常", ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                // 注意：只给前端通用文案，绝不把堆栈/SQL/类名暴露出去
                .body(ApiResponse.fail(ErrorCode.INTERNAL_ERROR, "服务器内部错误"));
    }
}
