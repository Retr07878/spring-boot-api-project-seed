package com.retr0.apispringboot.demo;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class DemoPageQuery {
    @Min(value = 1, message = "page 必须大于等于 1")
    private int page = 1;

    @Min(value = 1, message = "size 必须大于等于 1")
    @Max(value = 100, message = "size 不能超过 100")
    private int size = 10;

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
}
