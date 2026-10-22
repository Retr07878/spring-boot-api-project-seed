package com.retr0.apispringboot.user.dto;

import com.retr0.apispringboot.common.PageQuery;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * 用户列表的查询条件。
 *
 * <p>继承 {@link PageQuery} 后自动获得 page / size 及边界校验；
 * 这里只写用户模块自己的业务条件（模糊查询 + 性别筛选）。
 */
public class UserQuery extends PageQuery {

    @Size(max = 64, message = "username 长度不能超过 64")
    private String username;

    @Size(max = 64, message = "nickName 长度不能超过 64")
    private String nickName;

    @Min(value = 0, message = "sex 只能是 0、1、2")
    @Max(value = 2, message = "sex 只能是 0、1、2")
    private Integer sex;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}
