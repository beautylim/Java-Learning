package org.example.shop.model.entity;

import lombok.Data;

@Data
public class ArticleQuery {
    private int pageNum;
    private int pageSize;
    private int channelId;
    private int status;
    private String startTime;
    private String endTime;
    private Long authorId;
}
