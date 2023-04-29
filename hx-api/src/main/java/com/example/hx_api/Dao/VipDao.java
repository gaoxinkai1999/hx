package com.example.hx_api.Dao;


import com.example.hx_api.PoJo.Vip;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface VipDao {

    @Insert("insert into vip values (null,#{vipname},#{age},#{积分},#{phone},#{未消费天数},#{adress},#{维护人id})")
    void addVip(Vip vip);

    @Select("select * from vip where phone=#{phone}")
    ArrayList<Vip> getVipByPhone(String phone);


    @Select("select * from vip where id=#{id}")
    Vip getVipByID(int id);

    @Delete("delete from vip where id=#{id}")
    void delVipByID(int id);

    @Select("select * from vip")
    ArrayList<Vip> getAllVips();
}
