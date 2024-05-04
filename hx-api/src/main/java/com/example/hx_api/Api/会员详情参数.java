package com.example.hx_api.Api;

import org.springframework.stereotype.Component;


public class 会员详情参数 implements 请求参数{
    public String type="GetVipInfo";
    public int hyid;
    public int bsid=255982;
    public String dbname="xerp_qxhexingxc";
//    public String pushtokenid="";
//    public int device=1;
    public String tokenid="443842AC-E352-4AD0-B454-9671585E16EB ";
    public String version="3.5.1";

    public 会员详情参数(int hyid){
        this.hyid=hyid;
    }

}

