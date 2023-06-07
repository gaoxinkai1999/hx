package com.example.hx_api.Api;


public class RFM参数 implements 请求参数 {

  public String type="New_Getvipwechatid";
    public   int key_id=49083;
    public    int bsid=255982;
    public String tokenid="443842AC-E352-4AD0-B454-9671585E16EB";
    public   String pushtokenid="";
    public    int device=1;
    public    String version="3.5.7";
    public    String dbname="xerp_qxhexingxc";

    public RFM参数(int hyid){
        this.key_id=hyid;
    }
}
