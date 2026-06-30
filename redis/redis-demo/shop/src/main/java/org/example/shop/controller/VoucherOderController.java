package org.example.shop.controller;

import org.example.shop.model.entity.VoucherOder;
import org.example.shop.service.VoucherOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("voucher-order")
public class VoucherOderController {
    @Autowired
    private VoucherOrderService voucherOrderService;

    @PostMapping("/seckill")
    public ResponseEntity<String> seckill(@RequestParam("voucherId") Long voucherId, @RequestParam("count") int count, @RequestParam("payAmount") BigDecimal payAmount) {
        return ResponseEntity.status(HttpStatus.OK).body("{\"id\":" + voucherOrderService.createOrder(voucherId, count, payAmount) + "}");
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoucherOder> getVoucherOrder(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(voucherOrderService.findById(id));

    }

}
