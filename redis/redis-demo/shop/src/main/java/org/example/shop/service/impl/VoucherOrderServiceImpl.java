package org.example.shop.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.auth.LoginUser;
import org.example.shop.clients.VoucherOrderRedisCacheClient;
import org.example.shop.mapper.VoucherOrderMapper;
import org.example.shop.model.entity.Voucher;
import org.example.shop.model.entity.VoucherOder;
import org.example.shop.service.VoucherOrderService;
import org.example.shop.service.VoucherService;
import org.example.shop.utils.RedisIdGenerator;
import org.example.shop.utils.UserUtil;
import org.jspecify.annotations.NonNull;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class VoucherOrderServiceImpl implements VoucherOrderService {
    @Autowired
    private VoucherService  voucherService;

    @Autowired
    private RedisIdGenerator redisIdGenerator;

    @Autowired
    private VoucherOrderMapper voucherOrderMapper;

    @Autowired
    private VoucherOrderRedisCacheClient voucherOrderRedisCacheClient;

//    @Autowired
//    private RedisLockUtil  redisLockUtil;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserUtil userUtil;

    private BlockingQueue<VoucherOder> voucherOrderQueue = new ArrayBlockingQueue<>(1024*1024);

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Lazy
    @Autowired
    private VoucherOrderService self;

//    @Autowired
//    private RedissonClient redissonClient2;
//
//    @Autowired
//    private RedissonClient redissonClient3;
    private DefaultRedisScript<Long> redisSecKillScript;

    private String consumerName;

    @Value("${server.port}")
    private String port;

    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private ObjectMapper objectMapper;


    @PostConstruct
    public void init() {
        redisSecKillScript = new DefaultRedisScript<>();
        redisSecKillScript.setLocation(new ClassPathResource("seckill.lua"));
        redisSecKillScript.setResultType(Long.class);
        executorService.submit(new VoucherOrderHandler());

        consumerName = appName + ".consumer.orders." + port;
        System.out.println("consumerName:" + consumerName);
    }



    private class VoucherOrderHandler implements Runnable {
        private String queueName = "stream.orders";
        private String groupName = "g1";
        @Override
        public void run() {
            while (true) {
                try {
                    List<@NonNull MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream()
                            .read(Consumer.from(groupName, consumerName),
                            StreamReadOptions.empty().count(1).block(Duration.ofSeconds(2)),
                            StreamOffset.create(queueName, ReadOffset.lastConsumed())
                    );
                    if (list == null || list.isEmpty()) {
                        continue;
                    }
                    MapRecord<String, Object, Object> record = list.get(0);
                    Map<Object, Object> values = record.getValue();
                    VoucherOder voucherOder = objectMapper.convertValue(values, VoucherOder.class);
                    voucherOder.setStatus(1);
                    voucherOder.setPayAmount(new BigDecimal(80));
                    self.handleVoucherOrder(voucherOder);
                    stringRedisTemplate.opsForStream().acknowledge(queueName, groupName, record.getId());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    //重试
                    // 读取pending-list中的消息
                    handlePendingMessage();
                }
            }
        }

        private void handlePendingMessage() {
            while (true) {
                try {
                    List<@NonNull MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream()
                            .read(Consumer.from(groupName, consumerName),
                                    StreamReadOptions.empty().count(1),
                                    StreamOffset.create(queueName, ReadOffset.from("0")));
                    if (list == null || list.isEmpty()) {
                        break;
                    }
                    MapRecord<String, Object, Object> record = list.get(0);
                    Map<Object, Object> values = record.getValue();
                    VoucherOder voucherOder = objectMapper.convertValue(values, VoucherOder.class);
                    self.handleVoucherOrder(voucherOder);
                    stringRedisTemplate.opsForStream().acknowledge(queueName, groupName, record.getId());
                } catch (Exception e) {
                    System.out.println("处理订单异常：" + e.getMessage());
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }



    @Override
    public Long createOrder(Long voucherId, int count, BigDecimal payAmount) {
        LoginUser loginUser = userUtil.getCurrentUser();
        Long orderId = redisIdGenerator.generateId("Voucher");

        // 执行脚本
        Long result = stringRedisTemplate.execute(redisSecKillScript, Collections.emptyList(), voucherId.toString(), loginUser.getUserId().toString(), orderId.toString());
        if (result == 1) {
            throw new RuntimeException("优惠券已领完");
        } else if (result == 2) {
            throw new RuntimeException("不能重复下单");
        } else if (result != 0) {
            throw new RuntimeException("结果返回异常");
        }

        return orderId;
    }


    private Long createOrderWithBlockingQueue(Long voucherId, int count, BigDecimal payAmount) {
        LoginUser loginUser = userUtil.getCurrentUser();
        // 执行脚本
        Long result = stringRedisTemplate.execute(redisSecKillScript, Collections.emptyList(), voucherId.toString(), loginUser.getUserId().toString());
        if (result == 1) {
            throw new RuntimeException("优惠券已领完");
        } else if (result == 2) {
            throw new RuntimeException("不能重复下单");
        } else if (result != 0) {
            throw new RuntimeException("结果返回异常");
        }
        //生成订单id
        Long orderId = redisIdGenerator.generateId("Voucher");
        VoucherOder voucherOder = new VoucherOder();
        voucherOder.setId(orderId);
        voucherOder.setVoucherId(voucherId);
        voucherOder.setUserId(loginUser.getUserId());
        voucherOder.setStatus(1);
        voucherOder.setPayAmount(payAmount);

        voucherOrderQueue.add(voucherOder);
        //开启新的线程

        //阻塞队列
        // 或者发通知
        return orderId;
    }

    public Long createOrderWithNormal(Long voucherId, int count, BigDecimal payAmount) {
            Voucher voucher = voucherService.selectById(voucherId);
            if (voucher.getStartTime() != null && voucher.getStartTime().isAfter(LocalDateTime.now())) {
                throw new RuntimeException("优惠券还不能领取");
            }

            if (voucher.getStock() - count < 0) {
                throw new RuntimeException("优惠券库存不足");
            }

            if (voucher.getLimitPerUser() < count) {
                throw new RuntimeException("一人只能领取" + voucher.getLimitPerUser() + "优惠券");
            }

            if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null &&
                    SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof LoginUser loginUser) {
                Long userId = loginUser.getUserId();
//            if (redisLockUtil.tryLock("voucher-order:" + userId, 10)) {
                RLock lock = redissonClient.getLock("lock:voucher-order:" + userId);
//            RLock lock1 = redissonClient.getLock("lock:voucher-order:" + userId);
//            RLock lock2 = redissonClient2.getLock("lock:voucher-order:" + userId);
//            RLock lock3 = redissonClient3.getLock("lock:voucher-order:" + userId);
//
//            RLock lock = redissonClient.getMultiLock(lock1, lock2, lock3);
                if (lock.tryLock()) {
                    try {
                        int orderCount = voucherOrderMapper.countVoucherOrder(userId, voucherId);
                        if (orderCount >= voucher.getLimitPerUser()) {
                            throw new RuntimeException("您已经领取过" + orderCount + "张该优惠券");
                        }
                        voucherService.deductStock(voucherId, count);
                        VoucherOder voucherOder = new VoucherOder();
                        voucherOder.setId(redisIdGenerator.generateId("Voucher"));
                        voucherOder.setVoucherId(voucherId);
                        voucherOder.setUserId(userId);
                        voucherOder.setStatus(1);
                        voucherOder.setPayAmount(payAmount);
                        voucherOrderMapper.insertVoucherOrder(voucherOder);
                        return voucherOder.getId();
                    } finally {
                        lock.unlock();
                    }
                }

            } else {
                throw new RuntimeException("Cannot get userId");
            }
            return null;
    }

    @Transactional
    @Override
    public void handleVoucherOrder(VoucherOder voucherOder) {
        voucherService.deductStock(voucherOder.getVoucherId(), 1);
        voucherOrderMapper.insertVoucherOrder(voucherOder);
    }

    @Override
    public VoucherOder findById(Long id) {
        try {
            return voucherOrderRedisCacheClient.queryByIdMutex(id, "VoucherOrder:", voucherOrderMapper::selectVoucherOrderById);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
