-- 查看有哪些数据库

show databases;

show variables like '%time_zone%';

-- 创建数据库
CREATE DATABASE IF NOT EXISTS user_db
DEFAULT CHARACTER SET utf8mb4
DEFAULT COLLATE utf8mb4_unicode_ci;

-- 进入数据库
use user_db

-- 创建用户表
CREATE TABLE sys_user (
      user_id bigint AUTO_INCREMENT PRIMARY KEY,
      username varchar(50) NOT NULL,
      password varchar(100) NOT NULL,
      email varchar(100) DEFAULT NULL,
      create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
      update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
      UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

show tables;

drop table sys_user;
