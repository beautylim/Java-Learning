package org.example.shop.model.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Shop extends RedisData {
    private Long id;
    private String shopName;
    private String tags;
    private String address;
    private String logoPath;
    private String coverPath;
    private float score;
    private Double x;
    private Double y;
    private Double distance;
}
