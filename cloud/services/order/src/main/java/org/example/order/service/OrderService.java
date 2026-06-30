package org.example.order.service;

import org.example.model.Order;

public interface OrderService {

    Order create(Long userId, Long productId);

    Order getOrderById(Long id);
}
