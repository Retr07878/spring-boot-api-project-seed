package com.retr0.apispringboot.common;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * 统一分页参数。
 * 所有查询条件类都继承它，自动获得 page/size 及校验规则。
 */
public class PageQuery {

    public static final int DEFAULT_PAGE = 1;
    public static final int DEFAULT_SIZE = 10;
    public static final int MAX_SIZE = 100;

    @Min(value = 1, message = "page 必须大于等于 1")
    private int page = DEFAULT_PAGE;

    @Min(value = 1, message = "size 必须大于等于 1")
    @Max(value = MAX_SIZE, message = "size 不能超过 " + MAX_SIZE)
    private int size = DEFAULT_SIZE;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    /** 转成 SQL 的 OFFSET，SQL 里写 LIMIT #{size} OFFSET #{offset} */
    public long offset() {
        return (long) (page - 1) * size;
    }
}
