package org.example.shop.service;

import org.example.shop.model.entity.Voucher;

import java.util.List;

public interface VoucherService {

    Voucher insert(Voucher voucher);

    List<Voucher> select();

    Voucher selectById(Long id);

    int deductStock(Long id, Integer count);
}
