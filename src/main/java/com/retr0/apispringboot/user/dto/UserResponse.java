package com.retr0.apispringboot.user.dto;

import com.retr0.apispringboot.user.entity.User;

import java.time.LocalDateTime;

/**
 * 用户响应体。
 *
 * <p>刻意不包含 password：密码即使加密也不应该出现在任何响应里。
 */
public record UserResponse(Long id, String username, String nickName, Integer sex, LocalDateTime registerDate) {

    /** 实体 → 响应 DTO 的转换集中在这里，避免各处重复。 */
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getNickName(), user.getSex(),
                user.getRegisterDate());
    }
}
