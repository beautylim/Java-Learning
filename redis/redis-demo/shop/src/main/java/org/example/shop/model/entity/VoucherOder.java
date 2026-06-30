package org.example.shop.model.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class VoucherOder extends RedisData {
    /**
     * 订单ID（主键，由业务生成）
     */
    private Long id;

    /**
     * 下单的用户id
     */
    private Long userId;

    /**
     * 优惠券id
     */
    private Long voucherId;

    /**
     * 支付方式 1: 余额支付; 2: 支付宝; 3: 微信
     */
    private Integer payType;

    /**
     * 订单状态 1: 未支付; 2: 已支付; 3: 已核销; 4: 已取消
     */
    private Integer status;

    /**
     * 实际支付金额
     */
    private BigDecimal payAmount;

    /**
     * 下单时间
     */
    private LocalDateTime createTime;

    /**
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 核销时间
     */
    private LocalDateTime useTime;

    /**
     * 退款时间
     */
    private LocalDateTime refundTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
