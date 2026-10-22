package com.retr0.apispringboot.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 全局异常处理器。
 *
 * <p>项目里所有异常的统一出口：把异常翻译成 {@link ApiResponse}，
 * 并让 HTTP 状态码与业务码语义保持一致。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** 业务异常：状态码从 ErrorCode 里取。 */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusinessException(BusinessException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        log.info("业务异常: {} - {}", errorCode.name(), ex.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(ApiResponse.fail(errorCode, ex.getMessage(), ex.getData()));
    }

    /**
     * 参数校验失败 → 400 + 字段明细。
     *
     * <p>注意：{@code MethodArgumentNotValidException}（@RequestBody 校验失败）是
     * {@code BindException} 的子类，所以这一个方法能同时覆盖：
     * <ul>
     *   <li>POST JSON 请求体校验</li>
     *   <li>GET 查询对象（@ModelAttribute）校验</li>
     * </ul>
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(BindException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fieldError : ex.getFieldErrors()) {
            // isBindingFailure() = true 表示"类型转换失败"（如 ?page=abc），
            // 这类消息是框架默认的英文内部信息，统一换成中文提示，避免泄露实现细节
            String message = fieldError.isBindingFailure()
                    ? "参数格式不正确"
                    : fieldError.getDefaultMessage();
            errors.putIfAbsent(fieldError.getField(), message);
        }
        log.info("参数校验失败: {}", errors);
        return ResponseEntity.badRequest()
                .body(ApiResponse.fail(ErrorCode.INVALID_PARAM, "请求参数不合法", errors));
    }

    /** 单个方法参数上的约束校验失败（Spring 6.1+ 行为）→ 400。 */
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodValidation(HandlerMethodValidationException ex) {
        log.info("方法参数校验失败: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(ApiResponse.fail(ErrorCode.INVALID_PARAM, "请求参数不合法"));
    }

    /** 请求体不是合法 JSON → 400。 */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotReadable(HttpMessageNotReadableException ex) {
        log.info("请求体解析失败: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(ApiResponse.fail(ErrorCode.INVALID_PARAM, "请求体格式错误"));
    }

    /** 缺少必填的 query 参数 → 400。 */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingParam(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.fail(ErrorCode.INVALID_PARAM, "缺少请求参数：" + ex.getParameterName()));
    }

    /** 直接接收的参数类型不对（如 @RequestParam int page 收到 abc）→ 400。 */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Object>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.fail(ErrorCode.INVALID_PARAM, "参数类型错误：" + ex.getName()));
    }

    /** 兜底：任何没被上面接住的异常 → 500。 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleUnexpected(Exception ex) {
        // 未知异常必须打印完整堆栈（error 级别），给开发排查用
        log.error("未处理异常", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                // 只给前端通用文案，绝不把堆栈 / SQL / 类名暴露出去
                .body(ApiResponse.fail(ErrorCode.INTERNAL_ERROR, "服务器内部错误"));
    }
}
