package com.example.hx_api.Dao;

import com.alibaba.fastjson.JSONObject;
import com.example.hx_api.PoJo.Dept;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface DeptDao {

   void addDept();

   void delDept();
   @Select("select * from dept")
   ArrayList<Dept> getDepts();

   @Select("select  * from dept")
   @Results ({
           @Result(column = "id", property = "users",
                   many = @Many(select = "com.example.hx_api.Dao.UserDao.getUsersByDeptId")),
           @Result(column = "id", property = "id")
   }

   )
   ArrayList<Dept> getCascadeDept();

   @Select("select * from dept where id=#{id}")
   Dept getDeptById(int id);



   @Select("select name,(select if(count(*)>=200,200,count(*)) from vip where 未消费天数<=180 and 维护人id=user.id)  as 念念不忘,(select count(*)-(select if(count(*)>=200,200,count(*)) from vip where 未消费天数<=180 and 维护人id=user.id) from vip where  维护人id=user.id) as 好久不见  from user where 所属部门id=#{id};")
   ArrayList<JSONObject> getDeptCountById(int id);
}
