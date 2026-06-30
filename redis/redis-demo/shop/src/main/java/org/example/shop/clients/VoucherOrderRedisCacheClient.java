package org.example.shop.clients;

import org.example.shop.model.entity.VoucherOder;
import org.springframework.stereotype.Service;

@Service
public class VoucherOrderRedisCacheClient extends RedisCacheClient<VoucherOder>{

    public VoucherOrderRedisCacheClient() {
        super(VoucherOder.class);
    }
}
