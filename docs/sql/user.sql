-- 用户表（阶段 3 手工建表用；阶段 4 会改由 Flyway 管理）
CREATE DATABASE IF NOT EXISTS `api_seed`
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE `api_seed`;

CREATE TABLE IF NOT EXISTS `user` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT,
    `username`      VARCHAR(64)  NOT NULL COMMENT '登录名，唯一',
    `password`      VARCHAR(128) NOT NULL COMMENT '密码（阶段 3 明文，阶段 5 改 BCrypt）',
    `nick_name`     VARCHAR(64)  DEFAULT NULL COMMENT '昵称',
    `sex`           TINYINT      DEFAULT NULL COMMENT '性别：0 未知，1 男，2 女',
    `register_date` DATETIME     NOT NULL COMMENT '注册时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_username` (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;
