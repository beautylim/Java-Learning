package org.example.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Product {
    private Long id;
    private BigDecimal price;
    private String description;
    private int quantity;
}
