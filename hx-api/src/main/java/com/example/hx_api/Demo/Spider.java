package com.example.hx_api.Demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.hx_api.Api.*;


public class Spider extends Thread {


    public int pageid;


    public Api api;

    public 用户列表请求参数 参数;
    public Vip_Dao vipDao;

    public Spider(int pageid, Api api, 用户列表请求参数 参数, Vip_Dao vipDao) {
        this.pageid = pageid;
        this.api = api;
        this.参数 = 参数;
        this.vipDao = vipDao;
    }


    @Override
    public void run() {
        参数.setProcArray(pageid);
        JSONObject demo1 = api.demo(参数);
        响应 响应 = demo1.toJavaObject(响应.class);
        JSONArray message = 响应.getMESSAGE();
        //遍历每一个会员
//        for (Object o : message) {
//            JSONObject vipInfo = (JSONObject) o;
//
//
//            int hyid = vipInfo.getIntValue("HYID");
//            String name = vipInfo.getString("C_NAME");
//            //获取rfm参数并解析
//
//            JSONObject demo = api.demo(new RFM参数(hyid));
//            JSONObject rfm = demo.getJSONArray("MESSAGE").getJSONObject(0);
//            int r = rfm.getIntValue("rvalue");
//            int f = rfm.getIntValue("fvalue");
//            int m = rfm.getIntValue("mvalue");
////           vipDao.add(new Vip_(hyid,name,r,f,m));
//        }
    }


}
