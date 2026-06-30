package org.example.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode
public class Order {
    private Long id;
    private BigDecimal totalPrice;
    private Long userId;
    private String address;
    private List<Object> products;
}
