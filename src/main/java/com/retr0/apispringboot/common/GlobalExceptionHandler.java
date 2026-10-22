package com.retr0.apispringboot.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

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
    /** 参数校验失败（@Valid）→ 400 + 字段明细 */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(BindException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        // getFieldErrors() 里就是每个字段的校验失败信息
        ex.getFieldErrors().forEach(fieldError ->
                errors.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage()));

        log.info("参数校验失败: {}", errors);
        return ResponseEntity.badRequest()
                .body(ApiResponse.fail(ErrorCode.INVALID_PARAM, "请求参数不合法", errors));
    }

    /** 请求体不是合法 JSON，或者类型对不上 → 400 */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotReadable(HttpMessageNotReadableException ex) {
        log.info("请求体解析失败: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(ApiResponse.fail(ErrorCode.INVALID_PARAM, "请求体格式错误"));
    }

    /** 缺少必填的 query 参数 → 400 */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingParam(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.fail(ErrorCode.INVALID_PARAM, "缺少请求参数：" + ex.getParameterName()));
    }

    /** 参数类型不对，比如 ?page=abc → 400 */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.fail(ErrorCode.INVALID_PARAM, "参数类型错误：" + ex.getName()));
    }
}
