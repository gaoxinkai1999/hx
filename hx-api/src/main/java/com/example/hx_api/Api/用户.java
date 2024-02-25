package com.example.hx_api.Api;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class 用户 {
    //姓名
    public String C_NAME;
    //手机号
    public String C_MOBILE;
    //总积分
    public int n_allvalue;
    //用户id
    public String HYID;
    //序号
    public String rowid;
    //总页数
    public int ipage;

    public String d_lastBuy;

    public int 总购买数;
    public int 兽霸购买数;
}
