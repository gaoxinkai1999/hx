package com.example.hx_api.Controller;

import com.alibaba.fastjson.JSONObject;
import com.example.hx_api.Api.Api;
import com.example.hx_api.Api.RFM参数;
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

    @PostMapping("/getUserInfo")
    public User getUserInfo(@RequestBody JSONObject json){
       return userDao.getUserById(json.getIntValue("id"));
    }


    @PostMapping("/Login")
    public User Login(@RequestBody User user) {
        User us = userDao.getUserByName(user.name);
        if (us!=null){
            if (us.password.equals(user.password)){
                return us;
            }
        }
        return null;
    }
    @PostMapping("/addVip")
    public boolean addVip(@RequestBody  Vip vip){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hyid",vip.hyid);
        jsonObject.put("userid",vip.维护人.id);
        boolean b = CheckRepeat(jsonObject);
        if (b){
            return false;
        }else {
            vipDao.addVip(vip);
            return true;
        }
    }
    @PostMapping("/addVipPlus")
    public boolean addVipPlus(@RequestBody  Vip vip){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hyid",vip.hyid);
        jsonObject.put("UserId",vip.维护人.id);
        boolean b = CheckRepeat_plus(jsonObject);
        if (b){
            return false;
        }else {
            vipDao.addVip(vip);
            return true;
        }
    }

    @PostMapping("/getVipById")
    public Vip getVipById(@RequestBody JSONObject json){
        return vipDao.getVipByID(json.getIntValue("id"));
    }



    @PostMapping("/getDepts")
    public ArrayList<Dept>getDepts(){
        return  deptDao.getDepts();
    }
    @PostMapping("/addUser")
    public boolean  addUser(@RequestBody User user){
        User userByName = userDao.getUserByName(user.name);
        if (userByName==null){
            userDao.addUser(user);
            return true;
        }else {
            return false;
        }
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
    @PostMapping("/getRFM")
    public JSONObject getRFM(@RequestBody JSONObject json){
        return api.demo(new RFM参数(json.getIntValue("hyid")));
    }

    /**
     * 员工端检查重复 不可与相同类型员工重复
     * @param json
     * @return
     */
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
    /**
     * 管理员端检查重复 可与相同类型员工重复，不可和自己重复
     * @param json
     * @return
     */

    @PostMapping("/CheckRepeat_plus")
    public boolean CheckRepeat_plus(@RequestBody JSONObject json){
        ArrayList<Vip> vips = vipDao.getVipsByHyid(json.getIntValue("hyid"));
        if (vips.isEmpty()){
            return false;
        }else {
            int userid = json.getIntValue("UserId");
            for (Vip vip : vips) {
                if (vip.维护人.id==userid){
                    return true;
                }
            }
        }
        return false;
    }
    @PostMapping("/念念不忘")
    public ArrayList<Vip>  念念不忘(@RequestBody JSONObject json){
       return vipDao.念念不忘(json.getIntValue("UserId"));
    }
    @PostMapping("/好久不见")
    public ArrayList<Vip> 好久不见(@RequestBody JSONObject json){
        return vipDao.好久不见(json.getIntValue("UserId"));
    }

    @PostMapping("/念念不忘数量")
    public int 念念不忘数量(@RequestBody JSONObject json){
        return vipDao.念念不忘数量(json.getIntValue("UserId"));
    }

    @PostMapping("/好久不见数量")
    public int 好久不见数量(@RequestBody JSONObject json){
        return vipDao.好久不见数量(json.getIntValue("UserId"));
    }

    @PostMapping("/setUser")
    public void setUser(@RequestBody User user){
        userDao.setUser(user);
    }


    @PostMapping("/getAllVips")
    public ArrayList<Vip> getAllVips(){
       return vipDao.getAllVips();
    }
    @PostMapping("/欢聚一堂数量")
    public int 欢聚一堂数量(){
       return vipDao.欢聚一堂数量();
    }
    @PostMapping("/getVipsByUserId")
    public ArrayList<Vip> getVipsByUserId(@RequestBody JSONObject json){
      return   vipDao.getVipsByUserId(json.getIntValue("UserId"));
    }
    @PostMapping("/setVip")
    public void setVip(@RequestBody Vip vip){
        vipDao.setVip(vip);
    }
    @PostMapping("/getDeptCountById")
    public ArrayList<JSONObject> getDeptCountById(@RequestBody JSONObject json){
//       return deptDao.getDeptCountById(json.getIntValue("id"));
        return vipDao.demo(json.getIntValue("id"));
    }
    @PostMapping("/MoveVip")
    public boolean MoveVip(@RequestBody JSONObject json){
        Vip vip = json.getObject("Vip", Vip.class);
        int userId = json.getIntValue("UserId");
        int i = vipDao.CheckVipRepeat(userId, vip.hyid);
        if (i!=0){
            return false;
        }else {
            vipDao.setVipUser(userId, vip.id);
            return true;
        }


    }
    @PostMapping("/FindVipLikeName")
    public ArrayList<Vip> FindVip(@RequestBody JSONObject json){
        String name = json.getString("name").trim();
        System.out.println(name);
        if (name.isEmpty()){
            System.out.println("空的");
            return new ArrayList<Vip>();
        }else {
            char[] chars = name.toCharArray();
            StringBuilder likeName= new StringBuilder("%");
            for (char aChar : chars) {
                likeName.append(aChar).append("%");
            }
            return vipDao.FindVipLikeName(likeName.toString());
        }
    }
    @PostMapping("/getVipsByName")
    public ArrayList<Vip> getVipsByName(@RequestBody JSONObject json){
       return vipDao.getVipsByName(json.getString("name").trim());
    }
    @PostMapping("/delVip")
    public void delVip(@RequestBody JSONObject json){
        vipDao.delVip(json.getIntValue("id"));
    }


    @PostMapping("/MoveUser")

    public void MoveUser(@RequestBody JSONObject json){
        userDao.moveUser(json.getIntValue("DeptId"),json.getIntValue("UserId"));
    }
}
