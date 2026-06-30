package org.example.shop.model.dto;

import lombok.Data;

@Data
public class PublishRequest {
    private String title;
    private String content;
    private int type_id;
    private Cover cover;

}
