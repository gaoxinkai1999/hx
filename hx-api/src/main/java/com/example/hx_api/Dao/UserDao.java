package com.example.hx_api.Dao;

import com.example.hx_api.PoJo.User;
import com.example.hx_api.PoJo.Vip;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface UserDao {

    @Select("select * from vip where 维护人id=#{id}")
    ArrayList<Vip> getVipByUserID(int id);


    @Insert("insert into user values (null,#{username},#{password},#{职位},#{所属部门})")
    void addUser(User user);


    @Select("select * from user")
    ArrayList<User>getUsers();
    @Select("select * from user where username=#{username}")
    User getUserByName(String username);

}
