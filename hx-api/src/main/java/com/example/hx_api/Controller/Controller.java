package com.example.hx_api.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.hx_api.Dao.UserDao;
import com.example.hx_api.Dao.VipDao;
import com.example.hx_api.PoJo.User;
import com.example.hx_api.PoJo.Vip;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@CrossOrigin
public class Controller {
    @Autowired
    UserDao userDao;
    @Autowired
    VipDao vipDao;

    @PostMapping("/getVipByUserID")
    public ArrayList<Vip> getVipByUserID(@RequestBody JSONObject json) {
        return userDao.getVipByUserID(json.getIntValue("id"));
    }

    @PostMapping("/Login")
    public User Login(@RequestBody User user) {
        System.out.println(user);
        User us = userDao.getUserByName(user.username);
        System.out.println(us);
        if (us!=null){
            if (us.password.equals(user.password)){
                return us;
            }
        }
        return null;
    }
    @PostMapping("/addVip")
    public void addVip(@RequestBody  Vip vip){
            vipDao.addVip(vip);
    }

    @PostMapping("/getVipById")
    public Vip getVipById(@RequestBody JSONObject json){
        return vipDao.getVipByID(json.getIntValue("id"));
    }

    @PostMapping("/delVipById")
    public void delVipById(@RequestBody JSONObject json){
       vipDao.delVipByID(json.getIntValue("id"));
    }
    @PostMapping("/getAllVips")
    public ArrayList<Vip> getAllVips(){
        return vipDao.getAllVips();
    }
}
