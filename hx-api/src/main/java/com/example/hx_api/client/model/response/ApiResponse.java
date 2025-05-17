package com.example.hx_api.client.model.response;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * API响应类
 */
@Data
public class ApiResponse {
    /**
     * 状态码
     */
    private Integer STATUS;
    
    /**
     * 响应消息
     * 可能是JSONArray或JSONObject，使用Object类型以支持两种情况
     */
    private Object MESSAGE;
    
    /**
     * 判断响应是否成功
     *
     * @return 是否成功
     */
    public boolean isSuccess() {
        return STATUS != null && STATUS == 0;
    }
    
    /**
     * 获取消息内容为JSONObject
     * 
     * @return JSONObject类型的消息或null
     */
    public JSONObject getMessageAsObject() {
        if (MESSAGE instanceof JSONObject) {
            return (JSONObject) MESSAGE;
        } else if (MESSAGE instanceof JSONArray && ((JSONArray) MESSAGE).size() > 0) {
            Object item = ((JSONArray) MESSAGE).get(0);
            if (item instanceof JSONObject) {
                return (JSONObject) item;
            }
        }
        return null;
    }
    
    /**
     * 获取消息内容为JSONArray
     * 
     * @return JSONArray类型的消息或null
     */
    public JSONArray getMessageAsArray() {
        if (MESSAGE instanceof JSONArray) {
            return (JSONArray) MESSAGE;
        } else if (MESSAGE instanceof JSONObject) {
            JSONArray array = new JSONArray();
            array.add(MESSAGE);
            return array;
        }
        return null;
    }
} 