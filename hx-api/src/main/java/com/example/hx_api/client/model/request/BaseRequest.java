package com.example.hx_api.client.model.request;

import lombok.Data;

/**
 * API基础请求参数
 */
@Data
public abstract class BaseRequest {
    /**
     * 数据库名称
     */
    protected String dbname = "xerp_qxhexingxc";
    
    /**
     * 业务ID
     */
    protected Integer bsid = 255982;
    
    /**
     * 令牌ID
     */
    protected String tokenid = "443842AC-E352-4AD0-B454-9671585E16EB";
    
    /**
     * 推送令牌ID
     */
    protected String pushtokenid = "";
    
    /**
     * 设备类型
     */
    protected Integer device = 1;
    
    /**
     * 版本号
     */
    protected String version = "3.5.7";
} 