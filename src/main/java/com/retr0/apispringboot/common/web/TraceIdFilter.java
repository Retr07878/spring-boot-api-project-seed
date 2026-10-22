package com.retr0.apispringboot.common.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * 请求追踪 ID 过滤器。
 * 为每个请求生成 traceId → 放进 MDC（日志模板能取到）→ 同时写进响应头。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)   // 最先执行，保证后面的日志都能带上 traceId
public class TraceIdFilter extends OncePerRequestFilter {

    public static final String TRACE_ID_HEADER = "X-Trace-Id";
    public static final String MDC_KEY = "traceId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        // 1. 优先沿用调用方传进来的（便于前后端/网关串联），否则自己生成
        String traceId = request.getHeader(TRACE_ID_HEADER);
        if (!StringUtils.hasText(traceId) || traceId.length() > 64) {
            traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        }

        // 2. 放进 MDC（日志上下文）和响应头
        MDC.put(MDC_KEY, traceId);
        response.setHeader(TRACE_ID_HEADER, traceId);

        try {
            chain.doFilter(request, response);   // 放行，后面的 Controller 执行
        } finally {
            // 3. 必须清理！Tomcat 线程是复用的，不清会串到下一个请求
            MDC.remove(MDC_KEY);
        }
    }
}