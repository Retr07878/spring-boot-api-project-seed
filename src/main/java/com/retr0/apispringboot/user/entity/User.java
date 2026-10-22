package com.retr0.apispringboot.user.entity;

import java.time.LocalDateTime;

/**
 * 用户实体，对应数据库表 {@code user}。
 *
 * <p>实体只在 Mapper / Service 内部使用，不作为接口入参或返回值（那是 DTO 的职责）。
 * 字段与表列一一对应；MyBatis 依靠 {@code map-underscore-to-camel-case} 把
 * {@code nick_name} 自动映射为 {@code nickName}。
 */
public class User {

    private Long id;
    private String username;
    private String password;
    private String nickName;
    private Integer sex;
    private LocalDateTime registerDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
        this.registerDate = registerDate;
    }
}
