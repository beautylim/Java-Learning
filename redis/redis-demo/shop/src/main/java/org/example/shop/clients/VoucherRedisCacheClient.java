package org.example.shop.clients;

import org.example.shop.model.entity.Voucher;
import org.springframework.stereotype.Component;

@Component
public class VoucherRedisCacheClient extends RedisCacheClient<Voucher>{

    public VoucherRedisCacheClient() {
        super(Voucher.class);
    }
}
