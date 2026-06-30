package org.example.shop.mapper;

import org.example.shop.model.entity.VoucherOder;

public interface VoucherOrderMapper {

    int insertVoucherOrder(VoucherOder  voucherOder);

    VoucherOder selectVoucherOrderById(Long id);

    int countVoucherOrder(Long userId, Long voucherId);
}
