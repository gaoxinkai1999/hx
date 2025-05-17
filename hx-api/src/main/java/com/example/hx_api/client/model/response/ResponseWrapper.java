package com.example.hx_api.client.model.response;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * API响应包装类
 * 用于处理外部API返回的复杂JSON结构
 */
@Data
public class ResponseWrapper {
    /**
     * 状态码
     */
    private Integer STATUS;
    
    /**
     * 响应消息
     */
    private JSONObject MESSAGE;
    
    /**
     * 判断响应是否成功
     *
     * @return 是否成功
     */
    public boolean isSuccess() {
        return STATUS != null && STATUS == 0;
    }
} 