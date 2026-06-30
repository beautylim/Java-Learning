package org.example.shop.model.entity;

import lombok.Data;

import java.util.List;

@Data
public class ScrollData {
    private List<?> list;
    private Long minTime;
    private Long offset;
}
