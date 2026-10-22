package com.retr0.apispringboot.common;

import java.util.List;

/**
 * 统一分页结果。
 *
 * @param items      当前页数据
 * @param total      总条数
 * @param page       当前页码（从 1 开始）
 * @param size       每页条数
 * @param totalPages 总页数
 */
public record PageResult<T>(List<T> items, long total, int page, int size, int totalPages) {

    public static <T> PageResult<T> of(List<T> items, long total, int page, int size) {
        int totalPages = size <= 0 ? 0 : (int) ((total + size - 1) / size);
        return new PageResult<>(items, total, page, size, totalPages);
    }
}