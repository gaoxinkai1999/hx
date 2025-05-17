package com.example.hx_api.client.model.request;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * RFM参数请求
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RfmRequest extends BaseRequest {
    /**
     * 请求类型
     */
    private String type = "New_Getvipwechatid";
    
    /**
     * 会员ID，对应原键值key_id
     */
    @JSONField(name = "key_id")
    private Integer keyId;
    
    /**
     * 构造函数
     *
     * @param hyid 会员ID
     */
    public RfmRequest(Integer hyid) {
        this.keyId = hyid;
    }
} 