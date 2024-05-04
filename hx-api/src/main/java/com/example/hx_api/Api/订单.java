package com.example.hx_api.Api;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class 订单 {

    public String C_TYPE;
    //销售店铺
    public String C_ZHUTI;
    public String D_DATE;
}
