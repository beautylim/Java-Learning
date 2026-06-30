package org.example.shop.mapper;

import org.example.shop.model.entity.Voucher;

import java.util.List;

public interface VoucherMapper {

    int insert(Voucher voucher);

    List<Voucher> selectAll();

    Voucher selectById(Long id);

    int deductStock(Long id, Integer count);
}
