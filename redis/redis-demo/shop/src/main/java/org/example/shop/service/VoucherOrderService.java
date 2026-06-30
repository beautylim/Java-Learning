package org.example.shop.service;

import org.example.shop.model.entity.VoucherOder;

import java.math.BigDecimal;

public interface VoucherOrderService {

    Long createOrder(Long voucherId, int count, BigDecimal payAmount);

    VoucherOder findById(Long id);

    void handleVoucherOrder(VoucherOder voucherOder);
}
