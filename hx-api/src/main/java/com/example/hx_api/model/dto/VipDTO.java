package com.example.hx_api.model.dto;

import lombok.Data;

/**
 * VIP数据传输对象
 */
@Data
public class VipDTO {
    /**
     * 主键ID
     */
    private Integer id;
    
    /**
     * 会员ID
     */
    private Integer hyid;
    
    /**
     * 会员姓名
     */
    private String name;
    
    /**
     * 年龄
     */
    private String age;
    
    /**
     * 积分
     */
    private Integer points;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 未消费天数
     */
    private Integer nonConsumptionDays;
    
    /**
     * 地址
     */
    private String address;
    
    /**
     * 维护人ID
     */
    private Integer maintainerId;
    
    /**
     * 复购周期
     */
    private Integer r;
    
    /**
     * 消费频率
     */
    private Integer f;
    
    /**
     * 消费金额
     */
    private Integer m;
} 