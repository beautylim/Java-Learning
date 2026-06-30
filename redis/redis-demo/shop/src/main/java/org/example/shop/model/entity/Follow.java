package org.example.shop.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Follow extends RedisData{
    private Long id;

    private Long userId;

    private Long followUserId;

    private LocalDateTime createTime;
}
