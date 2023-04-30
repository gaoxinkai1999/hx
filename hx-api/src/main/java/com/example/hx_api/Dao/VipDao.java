package com.example.hx_api.Dao;


import com.example.hx_api.PoJo.Vip;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface VipDao {

    @Insert("insert into vip values (null,#{hyid},#{name},#{age},#{积分},#{phone},#{未消费天数},#{adress},#{维护人.id})")
    void addVip(Vip vip);

    @Select("select * from vip where phone=#{phone}")
    ArrayList<Vip> getVipByPhone(String phone);


    @Select("select * from vip where id=#{id}")
    @Results({
            @Result(column = "维护人id", property = "维护人",
                    one = @One(select = "com.example.hx_api.Dao.UserDao.getUsersByDeptId")),
    })
    Vip getVipByID(int id);

    @Delete("delete from vip where id=#{id}")
    void delVipByID(int id);


   @Update("update vip set name=#{name} ,age=#{age},积分=#{积分},phone=#{phone},未消费天数=#{未消费天数},adress=#{adress} where id=#{id}")
    void setVip(Vip vip);

    @Select("select * from vip where 维护人id=#{id} and 未消费天数>=#{start} and 未消费天数<#{end} order by 未消费天数")
    ArrayList<Vip> getVipByUserID(int id,int start,int end);

    /**
     * 用于检查会员是否重复
     * @param hyid
     * @return
     */
    @Select("select * from vip where hyid=#{hyid}")
    @Results({
            @Result(column = "维护人id", property = "维护人",
                    one = @One(select = "com.example.hx_api.Dao.UserDao.getUserById"))
    })
   ArrayList<Vip> getVipsByHyid(int hyid);


    /**
     * 获取全部会员数据，可能有重复，用于更新数据
     */
    @Select("select * from vip")
    ArrayList<Vip> getVips();
}
