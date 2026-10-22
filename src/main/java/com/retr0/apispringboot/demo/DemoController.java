package com.retr0.apispringboot.demo;

import com.retr0.apispringboot.common.ApiResponse;
import com.retr0.apispringboot.common.BusinessException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 【临时】仅用于验证 2.2 全局异常处理，验证完后删除。
 */
@RestController
@RequestMapping("/demo")
public class DemoController {

    /** 2.2：业务异常 */
    @GetMapping("/not-found")
    public ApiResponse<String> notFound() {
        throw BusinessException.notFound("用户不存在：1");
    }

    /** 2.2：未知异常 */
    @GetMapping("/boom")
    public ApiResponse<String> boom() {
        throw new IllegalStateException("故意制造的未知异常");
    }

    /** 2.3：POST 请求体校验（@Valid 触发 DemoCreateRequest 里的规则） */
    @PostMapping("/create")
    public ApiResponse<String> create(@Valid @RequestBody DemoCreateRequest request) {
        return ApiResponse.success("创建成功：" + request.getName() + "，" + request.getAge() + "岁");
    }

    /** 2.3：GET 查询参数校验（@Valid 触发 DemoPageQuery 里的规则） */
    @GetMapping("/page")
    public ApiResponse<String> page(@Valid @ModelAttribute DemoPageQuery query) {
        return ApiResponse.success("page=" + query.getPage() + ", size=" + query.getSize());
    }
}