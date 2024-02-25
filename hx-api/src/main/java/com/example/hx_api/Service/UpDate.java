package com.example.hx_api.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.hx_api.Api.Api;
import com.example.hx_api.Api.RFM参数;
import com.example.hx_api.Api.会员详情参数;
import com.example.hx_api.Dao.VipDao;
import com.example.hx_api.PoJo.Vip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UpDate {

    @Autowired
    VipDao vipDao;
    @Autowired
    Api api;
    public void start(){
        ArrayList<Vip> allVips = vipDao.getVips();
        for (Vip vip : allVips) {
//            System.out.println(vip.hyid+"---"+vip.name);


            try {

                JSONObject demo = api.demo(new 会员详情参数(vip.hyid));
                JSONArray info = demo.getJSONObject("MESSAGE").getJSONArray("VIPINFO");
                for (Object o : info) {
                    //更新未消费天数
                    if (( (JSONObject)o).getString("FIELD").equals("D_LASTBUY")){
                        int value = ((JSONObject) o).getIntValue("VALUE");
                        vip.未消费天数=value;

                    }
                    //更新积分
                    if (( (JSONObject)o).getString("FIELD").equals("N_ALLVALUE")){
                        int value = ((JSONObject) o).getIntValue("VALUE");
                        vip.积分=value;
                    }

                }
                //获取RFM值
                JSONObject demo1 = api.demo(new RFM参数(vip.hyid));
                JSONObject message = demo1.getJSONArray("MESSAGE").getJSONObject(0);
                vip.R=message.getIntValue("rvalue");
                vip.F=message.getIntValue("fvalue");
                vip.M=message.getIntValue("mvalue");
                vipDao.setVip(vip);
            }catch (Exception e){
                System.out.println(vip.hyid+"---"+vip.name+"会员信息异常");
            }



        }
    }
}
