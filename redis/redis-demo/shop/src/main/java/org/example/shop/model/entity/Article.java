package org.example.shop.model.entity;

import lombok.Data;
import org.example.shop.model.dto.Cover;

import java.time.LocalDateTime;
@Data
public class Article {
    private Long id;
    private Long authorId;
    private String title;
    private String content;
    private Integer channelId;
    private Cover cover;
    private Integer commentCount;
    private Integer likeCount;
    private Integer readCount;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
