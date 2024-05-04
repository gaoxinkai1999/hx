package com.example.hx_api.Demo;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class Vip_ {
    public int hyid;
    public String name;
    public int r;
    public int f;
    public int m;
    public String phone;

    public Vip_(int hyid, String name, int r, int f, int m,String phone) {
        this.hyid = hyid;
        this.name = name;
        this.r = r;
        this.f = f;
        this.m = m;
        this.phone=phone;
    }
}
