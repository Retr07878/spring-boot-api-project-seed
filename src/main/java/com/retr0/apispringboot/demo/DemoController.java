package com.retr0.apispringboot.demo;

import com.retr0.apispringboot.common.ApiResponse;
import com.retr0.apispringboot.common.BusinessException;
import com.retr0.apispringboot.common.PageResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 【临时】仅用于验证阶段 2 的基础设施，阶段 3 开始后删除。
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    /** 2.2：业务异常 → 404。 */
    @GetMapping("/not-found")
    public ApiResponse<String> notFound() {
        throw BusinessException.notFound("用户不存在：1");
    }

    /** 2.2：未知异常 → 500。 */
    @GetMapping("/boom")
    public ApiResponse<String> boom() {
        throw new IllegalStateException("故意制造的未知异常");
    }

    /** 2.3：POST 请求体校验。 */
    @PostMapping("/create")
    public ApiResponse<String> create(@Valid @RequestBody DemoCreateRequest request) {
        return ApiResponse.success("创建成功：" + request.name() + "，" + request.age() + "岁");
    }

    /**
     * 2.4：分页演示（还没接数据库，用内存列表模拟）。
     *
     * <p>接了数据库后只需把这里换成 Mapper 查询，返回结构不变。
     */
    @GetMapping("/page")
    public ApiResponse<PageResult<String>> page(@Valid @ModelAttribute DemoQuery query) {
        // 假装这是数据库里的全部数据
        List<String> all = List.of("A", "B", "C", "D", "E");

        // 手动模拟 SQL 的 LIMIT #{size} OFFSET #{offset}
        int from = (int) Math.min(query.offset(), all.size());
        int to = Math.min(from + query.getSize(), all.size());
        List<String> items = all.subList(from, to);

        return ApiResponse.success(PageResult.of(items, all.size(), query.getPage(), query.getSize()));
    }
}
