package com.example.hx_api.Api;

import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Post;
import org.springframework.stereotype.Component;

@Component
public interface Api {

    @Post("http://t3.3usoft.com:9129/AppGet.aspx")
    响应 用户列表(@Body 用户列表请求参数 a);

    @Post("http://t3.3usoft.com:9129/AppGet.aspx")
    响应 销售信息(@Body 销售信息请求参数 a);
}
