package org.example.shop.service.impl;

import org.example.shop.clients.VoucherRedisCacheClient;
import org.example.shop.exception.ShopException;
import org.example.shop.mapper.VoucherMapper;
import org.example.shop.model.entity.Voucher;
import org.example.shop.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VoucherServiceImpl implements VoucherService {
    @Autowired
    private VoucherMapper voucherMapper;

    @Autowired
    private VoucherRedisCacheClient voucherRedisCacheClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional
    public Voucher insert(Voucher voucher) {
        voucherMapper.insert(voucher);
        stringRedisTemplate.opsForValue().set("voucher:stock:" + voucher.getId(), voucher.getStock().toString());
        return voucher;
    }

    @Override
    public List<Voucher> select() {
        return voucherMapper.selectAll();
    }

    @Override
    public Voucher selectById(Long id) {
        try {
            return voucherRedisCacheClient.queryByIdMutex(id, "voucher:", voucherMapper::selectById);
        }  catch (Exception e) {
            throw new ShopException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public int deductStock(Long id, Integer count) {
        int res = voucherMapper.deductStock(id, count);
        if (res < 1) {
            throw new RuntimeException("优惠券争抢激烈，请稍后再试");
        }
        voucherRedisCacheClient.deleteKey("voucher:" + id);
        return 0;
    }
}
