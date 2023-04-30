package com.example.hx_api.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.hx_api.Api.Api;
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
            JSONObject demo = api.demo(new 会员详情参数(vip.id));
            JSONArray info = demo.getJSONObject("MESSAGE").getJSONArray("VIPINFO");
            for (Object o : info) {
              if (( (JSONObject)o).getString("FIELD").equals("D_LASTBUY")){
                  int value = ((JSONObject) o).getIntValue("VALUE");
                  vip.未消费天数=value;
                 vipDao.setVip(vip);
              }
            }
        }
    }
}
