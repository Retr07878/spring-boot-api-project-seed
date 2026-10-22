package com.retr0.apispringboot.demo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 新增演示对象的请求体。
 *
 * <p>用 record 承载：不可变、字段即构造参数，Jackson 能直接反序列化。
 */
public record DemoCreateRequest(

        @NotBlank(message = "name 不能为空")
        @Size(max = 20, message = "name 长度不能超过 20")
        String name,

        @NotNull(message = "age 不能为空")
        @Min(value = 0, message = "age 不能小于 0")
        @Max(value = 150, message = "age 不能大于 150")
        Integer age,

        @Email(message = "email 格式不正确")
        String email
) {
}
