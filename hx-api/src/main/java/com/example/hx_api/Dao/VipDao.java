package com.example.hx_api.Dao;


import com.alibaba.fastjson.JSONObject;
import com.example.hx_api.PoJo.Vip;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;

@Mapper
public interface VipDao {
    /**
     * 添加vip
     *
     * @param vip
     */

    @Insert("insert into vip values (null,#{hyid},#{name},#{age},#{积分},#{phone},#{未消费天数},#{adress},#{维护人.id},curdate(),#{R},#{F},#{M})")
    void addVip(Vip vip);

    @Select("select * from vip where id=#{id}")
    @Results({
            @Result(column = "维护人id", property = "维护人",
                    one = @One(select = "com.example.hx_api.Dao.UserDao.getUserById")),
    })
    Vip getVipByID(int id);


    @Delete("delete from vip where id=#{id}")
    void delVip(int id);

    /**
     * 更新vip数据  用于每天服务器定时更新 和pc端数据更新
     *
     * @param vip
     */
    @Update("update vip set name=#{name} ,age=#{age},积分=#{积分},phone=#{phone},未消费天数=#{未消费天数},adress=#{adress},R=#{R},F=#{F},M=#{M} where hyid=#{hyid}")
    void setVip(Vip vip);


    /**
     * 用于检查会员是否重复
     *
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
     * 获取全部会员数据，用于更新数据
     */
    @Select("select * from vip group by hyid")
    ArrayList<Vip> getVips();

    /**
     * 获取全部会员数据，用于欢聚一堂
     */
    @Select("select * from vip ")
    @Results({
            @Result(column = "维护人id", property = "维护人",
                    one = @One(select = "com.example.hx_api.Dao.UserDao.getUserById"))
    })
    ArrayList<Vip> getAllVips();


    /**
     * 念念不忘vip数据 满足200个且 180天以内未消费
     */
    @Select("SELECT *\n" +
            "FROM (\n" +
            "         SELECT *\n" +
            "         FROM vip\n" +
            "         WHERE 未消费天数 <= 180 AND 维护人id =#{UserId}\n" +
            "         ORDER BY 未消费天数\n" +
            "         LIMIT 0, 200\n" +
            "     ) AS subquery\n" +
            "ORDER BY M DESC,未消费天数 DESC;")
    ArrayList<Vip> 念念不忘(int UserId);

    /**
     * 好久不见vip数据 除了念念不忘的其他vip数据
     */
    @Select("select * from vip where  维护人id=#{UserId} and id not in (select * from (select id from vip where 未消费天数<=180 and 维护人id=#{UserId} order by 未消费天数 limit 0,200) as t) order by M DESC,未消费天数 desc")
    ArrayList<Vip> 好久不见(int UserId);

    /**
     * 念念不忘vip数据数量
     *
     * @return
     */
    @Select("select count(*) from (select id from vip where 未消费天数<=180 and 维护人id=#{UserId} order by 未消费天数 limit 0,200) as `v*`")
    int 念念不忘数量(int UserId);

    /**
     * 好久不见vip数据数量
     *
     * @return
     */
    @Select("select count(*)-(select count(*) from (select * from vip where 未消费天数<=180 and 维护人id=#{UserId} order by 未消费天数 limit 0,200) as `v*`) from vip where  维护人id=#{UserId} ;")
    int 好久不见数量(int UserId);

    @Select("select count(*) from vip")
    int 欢聚一堂数量();


    @Select("select * from vip where 维护人id=#{UserId} order by m desc,未消费天数 desc")
    ArrayList<Vip> getVipsByUserId(int UserId);

    /**
     * 检查同一用户是否重复添加某会员
     */
    @Select("select count(*) from vip where 维护人id=#{UserId} and hyid=#{hyid}")
    int CheckVipRepeat(int UserId, int hyid);

    /**
     * 修改vip的维护人id
     */
    @Update("update vip set 维护人id=#{UserId},create_time=curdate() where id=#{id}")
    void setVipUser(int UserId, int id);

    @Select("select name from vip where name like #{name} group by name")
    ArrayList<Vip> FindVipLikeName(String name);

    @Select("select * from vip where name=#{name}")
    @Results({
            @Result(column = "维护人id", property = "维护人",
                    one = @One(select = "com.example.hx_api.Dao.UserDao.getUserById"))
    })
    ArrayList<Vip> getVipsByName(String name);

    /**
     * 测试
     */
    @Select("select id,name from hx.user where 所属部门id =#{id}")
    @Results({
            @Result(column = "id", property = "念念不忘",
                    one = @One(select = "com.example.hx_api.Dao.VipDao.念念不忘数量"),
                    javaType = Integer.class),
            @Result(column = "id", property = "好久不见",
                    one = @One(select = "com.example.hx_api.Dao.VipDao.好久不见数量"),
                    javaType = Integer.class),


    })
    ArrayList<JSONObject> demo(int id);
}
