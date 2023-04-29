package com.example.hx_api.Api;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class 响应 {
    public int STATUS;
    JSONArray MESSAGE;
}
