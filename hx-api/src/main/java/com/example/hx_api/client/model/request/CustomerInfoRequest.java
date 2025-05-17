package com.example.hx_api.client.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 会员详情请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerInfoRequest extends BaseRequest {
    /**
     * 请求类型
     */
    private String type = "GetVipInfo";
    
    /**
     * 会员ID
     */
    private Integer hyid;
    
    /**
     * 构造函数
     *
     * @param hyid 会员ID
     */
    public CustomerInfoRequest(Integer hyid) {
        this.hyid = hyid;
    }
} 