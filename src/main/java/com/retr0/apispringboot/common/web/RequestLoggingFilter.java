package com.retr0.apispringboot.common.web;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 访问日志。记录：方法 路径 状态码 耗时。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)   // 比 TraceIdFilter 晚一点，这样日志里能带上 traceId
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final long SLOW_REQUEST_MILLIS = 1000L;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        long start = System.nanoTime();
        try {
            chain.doFilter(request, response);
        } finally {
            long costMillis = (System.nanoTime() - start) / 1_000_000;
            if (costMillis >= SLOW_REQUEST_MILLIS) {
                log.warn("慢请求 {} {} -> {} ({} ms)",
                        request.getMethod(), request.getRequestURI(), response.getStatus(), costMillis);
            } else {
                log.info("{} {} -> {} ({} ms)",
                        request.getMethod(), request.getRequestURI(), response.getStatus(), costMillis);
            }
        }
    }
}