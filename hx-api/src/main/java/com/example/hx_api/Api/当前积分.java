package com.example.hx_api.Api;

public class 当前积分 implements 请求参数{
    public String type = "GetVipInfo";
    public int hyid = 49083;
    public int bsid = 255982;
    public String tokenid = "443842AC-E352-4AD0-B454-9671585E16EB";
    public String pushtokenid = "";
    public int device = 1;
    public String version = "4.0.2.6";
    public String dbname = "xerp_qxhexingxc";

    public 当前积分(int hyid) {
        this.hyid = hyid;
    }
}
