package com.example.hx_api.Dao;

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
}
