package com.example.hx_api.PoJo;

import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@ToString
public class Vip {
    public int id;
    public String vipname;
    public String age;
    public String 积分;
    public String phone;
    public int 未消费天数;
    public String adress;
    public int 维护人id;
}
