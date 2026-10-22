package com.retr0.apispringboot.demo;

import com.retr0.apispringboot.common.PageQuery;
import jakarta.validation.constraints.Size;

/**
 * 查询条件 = 分页参数（继承） + 业务条件（自己写）。
 */
public class DemoQuery extends PageQuery {

    @Size(max = 20, message = "keyword 长度不能超过 20")
    private String keyword;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}