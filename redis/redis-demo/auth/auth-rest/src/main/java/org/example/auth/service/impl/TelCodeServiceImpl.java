package org.example.auth.service.impl;

import org.example.auth.service.TelCodeService;
import org.example.auth.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class TelCodeServiceImpl implements TelCodeService {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String generateTelCode(String phoneNumber) {
        Random random = new Random();
        // 生成 100000 到 999999 之间的随机数
        int code = random.nextInt(900000) + 100000;
        String telCode = String.valueOf(code);
        redisUtil.setPhoneCode(phoneNumber, telCode);
        return telCode;
    }
}
