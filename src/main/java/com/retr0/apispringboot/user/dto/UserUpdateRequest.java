package com.retr0.apispringboot.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * 修改用户的请求体。
 *
 * <p>所有字段可选，只更新传入的字段；password 为空表示不修改密码。
 */
public record UserUpdateRequest(

        @Size(min = 6, max = 64, message = "password 长度需要在 6 到 64 之间")
        String password,

        @Size(max = 64, message = "nickName 长度不能超过 64")
        String nickName,

        @Min(value = 0, message = "sex 只能是 0、1、2")
        @Max(value = 2, message = "sex 只能是 0、1、2")
        Integer sex
) {
}
