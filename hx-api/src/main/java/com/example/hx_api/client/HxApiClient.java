package com.example.hx_api.client;

import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Post;
import com.example.hx_api.client.model.request.CustomerInfoRequest;
import com.example.hx_api.client.model.request.CustomerListRequest;
import com.example.hx_api.client.model.request.RfmRequest;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 和兴系统API客户端
 */
@Component
@Scope("prototype")
@BaseRequest(baseURL = "${globalApiBaseUrl}")
public interface HxApiClient {

    /**
     * 获取用户列表
     *
     * @param request 用户列表请求参数
     * @return API响应
     */
    @Post("/AppGet.aspx")
    JSONObject getCustomerList(@Body CustomerListRequest request);

    /**
     * 获取会员详情
     *
     * @param request 会员详情请求参数
     * @return API响应
     */
    @Post("/AppGet.aspx")
    JSONObject getCustomerInfo(@Body CustomerInfoRequest request);

    /**
     * 获取会员RFM信息
     *
     * @param request RFM请求参数
     * @return API响应
     */
    @Post(value = "/AppGet.aspx", contentType = "application/x-www-form-urlencoded")
    JSONObject getRfmInfo(@Body RfmRequest request);

    /**
     * 通用API请求
     *
     * @param params 请求参数
     * @return JSON响应
     */
    @Post("/AppGet.aspx")
    JSONObject sendRequest(@Body Object params);
}
