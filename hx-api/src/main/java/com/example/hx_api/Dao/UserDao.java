package com.example.hx_api.Dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.hx_api.PoJo.User;
import com.example.hx_api.PoJo.Vip;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface UserDao {




    @Insert("insert into user values (null,#{name},#{password},#{所属部门.id})")
    void addUser(User user);


    @Select("select * from user")
    ArrayList<User>getUsers();
    @Select("select * from user where name=#{name}")
    User getUserByName(String username);

    @Select("select id,name from user where 所属部门id=#{DeptId}")
    ArrayList<User>  getUsersByDeptId(int DeptId);

    @Select("select * from user where id=#{id}")
    @Results({
            @Result(column = "所属部门id", property = "所属部门",
                    one = @One(select = "com.example.hx_api.Dao.DeptDao.getDeptById")),
    })
    User getUserById(int id);

    /**
     * 更改员工账户信息
     */
    @Update("update user set name=#{name},password=#{password} where id=#{id}")
    void setUser(User user);

    /**
     * 更改员工所在部门
     */
    @Update("update user set 所属部门id=#{DeptId} where id=#{UserId}")
    void moveUser(int DeptId,int UserId);
}
