package org.example.order.mapper;

import org.example.model.Order;

public interface OrderMapper {

    int createOrder(Order order);

    Order getOrderById(Long id);
}
