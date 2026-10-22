package com.retr0.apispringboot.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 新增用户的请求体。
 *
 * <p>只包含"允许客户端传入"的字段：id、registerDate 由服务端生成。
 * 注意：密码目前按原样存储，阶段 5 接入认证后会改成 BCrypt 哈希。
 */
public record UserCreateRequest(

        @NotBlank(message = "username 不能为空")
        @Size(max = 64, message = "username 长度不能超过 64")
        String username,

        @NotBlank(message = "password 不能为空")
        @Size(min = 6, max = 64, message = "password 长度需要在 6 到 64 之间")
        String password,

        @Size(max = 64, message = "nickName 长度不能超过 64")
        String nickName,

        @Min(value = 0, message = "sex 只能是 0、1、2")
        @Max(value = 2, message = "sex 只能是 0、1、2")
        Integer sex
) {
}
