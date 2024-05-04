package com.example.hx_api.Demo;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;

@Mapper
public interface Vip_Dao {

    @Insert("insert into hx.demo values(#{hyid},#{name},#{r},#{f},#{m},#{phone}) ")
    void add(Vip_ vip);


}
