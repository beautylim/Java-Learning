package org.example.shop.model.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Blog extends RedisData{
    private Long id;
    private Long shopId;
    private String title;
    private String content;
    private Long userId;
    private String tags;
    private int likesCount;
    private int commentsCount;


    private boolean isLike;

    private String userName;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;


}
