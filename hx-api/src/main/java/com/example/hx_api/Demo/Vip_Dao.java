package com.example.hx_api.Demo;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;

@Mapper
public interface Vip_Dao {

    @Insert("insert into hx.all_vip values(#{hyid},#{name},#{r},#{f},#{m}) ")
    void add(Vip_ vip);

    @Select("select * from hx.all_vip where  r>=4 and f>=2 and m>=2 and hyid not in (select hyid from vip)")
    ArrayList<Vip_> getAll();

    @Update("update hx.all_vip set num=#{num} where hyid=#{hyid}")
    void setNum(int num,int hyid);
}
