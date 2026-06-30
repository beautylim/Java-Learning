package org.example.order.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.example.model.Order;
import org.example.order.properties.OrderProperties;
import org.example.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderProperties orderProperties;
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestParam(name="userId") Long userId, @RequestParam(name = "productId")  Long productId) {
        System.out.println("createOrder");
        System.out.println(orderProperties.getTimeout());
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(userId, productId));
    }

    @PostMapping("/seckill")
    @SentinelResource(value = "seckill-order", blockHandler = "seckillBlockHandler")
    public ResponseEntity<Order> secKill(@RequestParam(name="userId", required = false) Long userId, @RequestParam(name = "productId")  Long productId) {
        System.out.println(orderProperties.getTimeout());
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(userId, productId));
    }

    public ResponseEntity<Order> seckillBlockHandler(Long userId, Long productId, BlockException blockException) {
        Order order = new Order();
        order.setTotalPrice(new BigDecimal("0.00"));
        order.setUserId(0L);
        order.setAddress("create seckill block handler");
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        System.out.println("getOrder");
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOrderById(id));
    }
}
