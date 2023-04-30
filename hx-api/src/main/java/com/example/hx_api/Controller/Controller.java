package com.example.hx_api.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.hx_api.Api.Api;
import com.example.hx_api.Api.会员详情参数;
import com.example.hx_api.Api.手机号码查找会员参数;
import com.example.hx_api.Dao.DeptDao;
import com.example.hx_api.Dao.UserDao;
import com.example.hx_api.Dao.VipDao;
import com.example.hx_api.PoJo.Dept;
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
    @Autowired
    DeptDao deptDao;
    @Autowired
    Api api;


    @PostMapping("/getVipByUserID")
    public ArrayList<Vip> getVipByUserID(@RequestBody JSONObject json) {
        return vipDao.getVipByUserID(json.getIntValue("id"), json.getIntValue("start"), json.getIntValue("end"));
    }

    @PostMapping("/Login")
    public User Login(@RequestBody User user) {
        User us = userDao.getUserByName(user.name);
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

    @PostMapping("/getDepts")
    public ArrayList<Dept>getDepts(){
        return  deptDao.getDepts();
    }
    @PostMapping("/addUser")
    public void  addUser(@RequestBody User user){
        userDao.addUser(user);
    }

    @PostMapping("/getCascadeDept")
    public ArrayList<Dept> getCascadeDept(){
         return    deptDao.getCascadeDept();
    }

    @PostMapping("/FindVips")
    public JSONObject FindVips(@RequestBody JSONObject json){
       return api.demo(new 手机号码查找会员参数(json.getString("phone")));
    }
    @PostMapping("/FindVipInfo")
    public JSONObject FindVipInfo(@RequestBody JSONObject json){
       return api.demo(new 会员详情参数(json.getIntValue("hyid")));
    }

    @PostMapping("/CheckRepeat")
    public boolean CheckRepeat(@RequestBody JSONObject json){
        ArrayList<Vip> vips = vipDao.getVipsByHyid(json.getIntValue("hyid"));
        if (vips.isEmpty()){
            return false;
        }else {
            User user = userDao.getUserById(json.getIntValue("userid"));
            for (Vip vip : vips) {
                if (vip.维护人.所属部门.type.equals(user.所属部门.type)){
                    return true;
                }
            }
        }
        return false;
    }

}
