package com.example.hx_api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.hx_api.Api.*;
import com.example.hx_api.Controller.Controller;
import com.example.hx_api.Dao.DeptDao;
import com.example.hx_api.Dao.UserDao;
import com.example.hx_api.Dao.VipDao;
import com.example.hx_api.Demo.Spider;
import com.example.hx_api.Demo.Vip_;
import com.example.hx_api.Demo.Vip_Dao;
import com.example.hx_api.Service.UpDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@SpringBootTest
class ShoubaApplicationTests {
    @Autowired
    Api api;
    @Autowired
    用户列表请求参数 a;
    @Autowired
    销售信息请求参数 b;

    @Autowired
    UserDao userDao;
    @Autowired
    DeptDao deptDao;
    @Autowired
    UpDate upDate;
    @Autowired
    Controller controller;
    @Autowired
    VipDao vipDao;

    @Autowired
    BeanFactory beanFactory;
    @Autowired
    Vip_Dao vip_dao;


    @Test
    void contextLoads()  {
//        int hyid=84106;
//        JSONObject demo = api.demo(new 会员详情参数(hyid));
//        JSONArray info = demo.getJSONObject("MESSAGE").getJSONArray("VIPINFO");
//        for (Object o : info) {
//            //更新未消费天数
//            if (( (JSONObject)o).getString("FIELD").equals("D_LASTBUY")){
//                int value = ((JSONObject) o).getIntValue("VALUE");
//                System.out.println(value);
//
//            }
//            //更新积分
//            if (( (JSONObject)o).getString("FIELD").equals("N_ALLVALUE")){
//                int value = ((JSONObject) o).getIntValue("VALUE");
//                System.out.println(value);
//            }
//
//        }
//        upDate.start();


        用户列表请求参数 a = new 用户列表请求参数();


        for (int i = 1; i < 24; i++) {

            a.all(i);
            响应 用户列表 = api.用户列表(a);
//            获取id和名字
            for (Object o : 用户列表.getMESSAGE()) {
                JSONObject messageObject = (JSONObject) o;
                int hyid= messageObject.getIntValue("HYID");
                String name=messageObject.getString("C_NAME");
                //            获取rfm值
                JSONObject demo1 = api.demo(new RFM参数(hyid));
                JSONObject message = demo1.getJSONArray("MESSAGE").getJSONObject(0);
                int R=message.getIntValue("rvalue");
                int F=message.getIntValue("fvalue");
                int M=message.getIntValue("mvalue");
                String phone = message.getString("C_MOBILE");
                Vip_ vip = new Vip_(hyid, name, R, F, M, phone);
                vip_dao.add(vip);
            }

        }



    }
}
