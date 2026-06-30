package org.example.shop.model.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Voucher extends RedisData {

    /**
     * 优惠券ID
     */
    private Long id;

    /**
     * 店铺ID（如果是商户券）
     */
    private Long shopId;

    /**
     * 优惠券标题
     */
    private String title;

    /**
     * 副标题
     */
    private String subTitle;

    /**
     * 优惠券类型 1: 满减券; 2: 折扣券; 3: 代金券
     */
    private Integer type;

    /**
     * 折扣金额/折扣率（满减金额、打折比例0-100）
     */
    private BigDecimal discount;

    /**
     * 使用门槛（满多少元可用，0表示无门槛）
     */
    private BigDecimal conditionAmount;

    /**
     * 库存数量
     */
    private Integer stock;

    /**
     * 已发放数量
     */
    private Integer issuedCount;

    /**
     * 已使用数量
     */
    private Integer useCount;

    /**
     * 每人限领数量
     */
    private Integer limitPerUser;

    /**
     * 状态 0: 已下架; 1: 上架中; 2: 已过期
     */
    private Integer status;

    /**
     * 生效时间
     */
    private LocalDateTime startTime;

    /**
     * 失效时间
     */
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
