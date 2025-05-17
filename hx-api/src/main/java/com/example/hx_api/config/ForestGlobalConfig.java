package com.example.hx_api.config;

import com.dtflys.forest.Forest;
import com.dtflys.forest.config.ForestConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class ForestGlobalConfig {

    @Value("${hxApiBaseUrl}")
    private String hxApiBaseUrlValue;

    @PostConstruct
    public void init() {
        // 获取全局配置
        ForestConfiguration configuration = Forest.config();
        // 设置全局变量
        configuration.setVariableValue("globalApiBaseUrl", hxApiBaseUrlValue);
    }
}
