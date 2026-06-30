package org.example.shop.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class Cover {
    private Integer type;
    private List<String> images;
}
