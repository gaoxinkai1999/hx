package com.example.hx_api.client.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户列表请求参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerListRequest extends BaseRequest {
    /**
     * 请求类型
     */
    private String type = "RunProc";
    
    /**
     * 存储过程名称
     */
    private String procName = "p_klqe_QueryVipLimit_new";
    
    /**
     * 存储过程参数
     */
    private String procArray;
    
    /**
     * 设置全部会员查询参数
     *
     * @param pageId 页码
     */
    public void setAllCustomersParam(int pageId) {
        StringBuilder builder = new StringBuilder();
        builder.append("{\"INFO\":[");
        builder.append("{\"FIELD\":\"queryType\",\"VALUE\":\"all\",\"TYPE\":1},");
        builder.append("{\"FIELD\":\"orderType\",\"VALUE\":\"totalmoney\",\"TYPE\":1},");
        builder.append("{\"FIELD\":\"pageid\",\"VALUE\":").append(pageId).append(",\"TYPE\":0},");
        builder.append("{\"FIELD\":\"bsid\",\"VALUE\":255982,\"TYPE\":0},");
        builder.append("{\"FIELD\":\"filterJson\",\"VALUE\":\"");
        // 过滤参数构建
        builder.append("{\\\"fvalue\\\":\\\"2,3,4,5,6\\\",");
        builder.append("\\\"mvalue\\\":\\\"2,3,4,5,6\\\",");
        builder.append("\\\"rvalue\\\":\\\"1,2,3\\\"}\",\"TYPE\":1}");
        builder.append("]}");
        this.procArray = builder.toString();
    }
}