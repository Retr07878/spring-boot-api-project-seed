package com.retr0.apispringboot.demo;

import jakarta.validation.constraints.*;

public class DemoCreateRequest {
    @NotBlank(message = "name 不能为空")
    @Size(max = 20, message = "name 长度不能超过 20")
     String name;

    @NotNull(message = "age 不能为空")
    @Min(value = 0, message = "age 不能小于 0")
    @Max(value = 150, message = "age 不能大于 150")
    Integer age;

    @Email(message = "email 格式不正确")
    String email;

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }
}
