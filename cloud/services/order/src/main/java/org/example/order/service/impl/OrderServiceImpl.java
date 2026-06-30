package org.example.order.service.impl;

import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.example.model.Order;
import org.example.model.Product;
import org.example.order.feign.ProductFeignClient;
import org.example.order.mapper.OrderMapper;
import org.example.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

//    @Autowired
//    private ProductClient productClient;

    @Autowired
    private ProductFeignClient productFeignClient;


    @SentinelResource(value = "createOrder", blockHandler = "createBlockHandler")
    @Override
    public Order create(Long userId, Long productId) {
        Order order = new Order();
        order.setTotalPrice(new BigDecimal("10000.00"));
        order.setUserId(userId);
        order.setAddress("某某小区某号几零几");
        Product product = productFeignClient.getProduct(productId);
        order.setProducts(List.of(product));
        return order;
    }

    public Order createBlockHandler(Long userId, Long productId, BlockException blockException) {
        Order order = new Order();
        order.setTotalPrice(new BigDecimal("0.00"));
        order.setUserId(0L);
        order.setAddress("create block handler");
        return order;
    }

    @SentinelResource(value = "getOrder", blockHandler = "createBlockHandler")
    @Override
    public Order getOrderById(Long id) {
        Order order = new Order();
        order.setId(id);
        order.setTotalPrice(new BigDecimal("10000.00"));
        order.setUserId(1L);
        order.setAddress("get order by id");
        return order;
    }


}
