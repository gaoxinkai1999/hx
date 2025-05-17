package com.example.hx_api.mapper;

import com.example.hx_api.model.entity.Vip;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * VIP会员数据访问接口
 */
@Mapper
public interface VipMapper {
    /**
     * 添加VIP
     */
    @Insert("insert into vip values (null,#{hyid},#{name},#{age},#{points},#{phone},#{nonConsumptionDays},#{address},#{maintainerId},curdate(),#{r},#{f},#{m}, #{lastExternalUpdateAt})")
    void insertVip(Vip vip);

    /**
     * 根据ID获取VIP
     */
    @Select("select * from vip where id=#{id}")
    @Results({
            @Result(column = "积分", property = "points"),
            @Result(column = "未消费天数", property = "nonConsumptionDays"),
            @Result(column = "adress", property = "address"),
            @Result(column = "维护人id", property = "maintainerId"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "last_external_update_at", property = "lastExternalUpdateAt"),
            @Result(column = "维护人id", property = "maintainer",
                    one = @One(select = "com.example.hx_api.mapper.UserMapper.getUserById"))
    })
    Vip getVipById(Integer id);

    /**
     * 根据会员ID获取VIP列表
     */
    @Select("select * from vip where hyid=#{hyid}")
    @Results({
            @Result(column = "积分", property = "points"),
            @Result(column = "未消费天数", property = "nonConsumptionDays"),
            @Result(column = "adress", property = "address"),
            @Result(column = "维护人id", property = "maintainerId"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "last_external_update_at", property = "lastExternalUpdateAt"),
            @Result(column = "维护人id", property = "maintainer",
                    one = @One(select = "com.example.hx_api.mapper.UserMapper.getUserById"))
    })
    List<Vip> getVipsByHyid(Integer hyid);

    /**
     * 更新VIP信息
     */
    @Update("update vip set name=#{name}, age=#{age}, 积分=#{points}, phone=#{phone}, 未消费天数=#{nonConsumptionDays}, adress=#{address}, R=#{r}, F=#{f}, M=#{m}, last_external_update_at=#{lastExternalUpdateAt} where hyid=#{hyid}")
    void updateVip(Vip vip);

    /**
     * 获取"念念不忘"会员列表
     * 规则: 未消费天数 <= 180，按未消费天数升序取前200，再按M值降序、未消费天数降序排序。PageHelper对此结果分页。
     */
    @Select("SELECT * FROM (SELECT * FROM vip FORCE INDEX(idx_维护人id_未消费天数) WHERE 未消费天数 <= 180 AND 维护人id =#{userId} ORDER BY 未消费天数 ASC LIMIT 200) AS subquery ORDER BY M DESC, 未消费天数 DESC")
    @Results({
            @Result(column = "积分", property = "points"),
            @Result(column = "未消费天数", property = "nonConsumptionDays"),
            @Result(column = "adress", property = "address"),
            @Result(column = "维护人id", property = "maintainerId"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "last_external_update_at", property = "lastExternalUpdateAt"),
            @Result(column = "维护人id", property = "maintainer",
                    one = @One(select = "com.example.hx_api.mapper.UserMapper.getUserById"))
    })
    List<Vip> getNeverForgetVips(Integer userId);

    /**
     * 获取"好久不见"会员列表
     * 规则: 排除掉"念念不忘"的会员（即上述按规则获取的前200名），其余按M值降序、未消费天数降序排序。PageHelper对此结果分页。
     */
    @Select("SELECT * FROM vip FORCE INDEX(idx_维护人id) WHERE 维护人id = #{userId} AND id NOT IN (SELECT id FROM (SELECT id FROM vip FORCE INDEX(idx_维护人id_未消费天数) WHERE 未消费天数 <= 180 AND 维护人id = #{userId} ORDER BY 未消费天数 ASC LIMIT 200) AS never_forget_ids_subquery) ORDER BY M DESC, 未消费天数 DESC")
    @Results({
            @Result(column = "积分", property = "points"),
            @Result(column = "未消费天数", property = "nonConsumptionDays"),
            @Result(column = "adress", property = "address"),
            @Result(column = "维护人id", property = "maintainerId"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "last_external_update_at", property = "lastExternalUpdateAt"),
            @Result(column = "维护人id", property = "maintainer",
                    one = @One(select = "com.example.hx_api.mapper.UserMapper.getUserById"))
    })
    List<Vip> getLongTimeNoSeeVips(Integer userId);

    /**
     * 获取"念念不忘"会员数量
     * (计算的是符合条件且限定在前200名内的数量)
     */
    @Select("SELECT COUNT(*) FROM (SELECT id FROM vip FORCE INDEX(idx_维护人id_未消费天数) WHERE 未消费天数 <= 180 AND 维护人id = #{userId} ORDER BY 未消费天数 ASC LIMIT 200) AS never_forget_count_subquery")
    int getNeverForgetVipsCount(Integer userId);

    /**
     * 获取"好久不见"会员数量
     * (计算的是排除了"念念不忘"会员后的数量)
     */
    @Select("SELECT COUNT(*) FROM vip FORCE INDEX(idx_维护人id) WHERE 维护人id = #{userId} AND id NOT IN (SELECT id FROM (SELECT id FROM vip FORCE INDEX(idx_维护人id_未消费天数) WHERE 未消费天数 <= 180 AND 维护人id = #{userId} ORDER BY 未消费天数 ASC LIMIT 200) AS never_forget_ids_for_count_subquery)")
    int getLongTimeNoSeeVipsCount(Integer userId);

    /**
     * 根据用户ID获取VIP列表
     */
    @Select("select * from vip FORCE INDEX(idx_维护人id) where 维护人id=#{userId} order by m desc,未消费天数 desc")
    @Results({
            @Result(column = "积分", property = "points"),
            @Result(column = "未消费天数", property = "nonConsumptionDays"),
            @Result(column = "adress", property = "address"),
            @Result(column = "维护人id", property = "maintainerId"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "last_external_update_at", property = "lastExternalUpdateAt"),
            @Result(column = "维护人id", property = "maintainer",
                    one = @One(select = "com.example.hx_api.mapper.UserMapper.getUserById"))
    })
    List<Vip> getVipsByUserId(Integer userId);

    /**
     * 检查VIP是否重复
     */
    @Select("select count(*) from vip where 维护人id=#{userId} and hyid=#{hyid}")
    int checkVipExists(Integer userId, Integer hyid);

    /**
     * 更新VIP的维护人
     */
    @Update("update vip set 维护人id=#{userId},create_time=curdate() where id=#{id}")
    void updateVipMaintainer(Integer userId, Integer id);

    /**
     * 根据名称获取VIP列表
     */
    @Select("select * from vip where name=#{name}")
    @Results({
            @Result(column = "积分", property = "points"),
            @Result(column = "未消费天数", property = "nonConsumptionDays"),
            @Result(column = "adress", property = "address"),
            @Result(column = "维护人id", property = "maintainerId"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "last_external_update_at", property = "lastExternalUpdateAt"),
            @Result(column = "维护人id", property = "maintainer",
                    one = @One(select = "com.example.hx_api.mapper.UserMapper.getUserById"))
    })
    List<Vip> getVipsByName(String name);

    /**
     * 模糊查询VIP
     */
    @Select("select name from vip where name like #{name} group by name")
    List<Vip> findVipsByNameLike(String name);

    /**
     * 删除VIP
     */
    @Delete("delete from vip where id=#{id}")
    void deleteVip(Integer id);

    /**
     * 获取所有VIP
     */
    @Select("select * from vip")
    @Results({
            @Result(column = "积分", property = "points"),
            @Result(column = "未消费天数", property = "nonConsumptionDays"),
            @Result(column = "adress", property = "address"),
            @Result(column = "维护人id", property = "maintainerId"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "last_external_update_at", property = "lastExternalUpdateAt"),
            @Result(column = "维护人id", property = "maintainer",
                    one = @One(select = "com.example.hx_api.mapper.UserMapper.getUserById"))
    })
    List<Vip> getAllVips();

    /**
     * 获取所有VIP数量
     */
    @Select("select count(*) from vip FORCE INDEX(PRIMARY)")
    int getAllVipsCount();

    /**
     * 获取一批需要从外部API更新数据的VIP记录。     * 获取last_external_update_at为NULL或不在今天的记录。     * @param batchSize 批次大小     * @return VIP列表
     */
    @Select("SELECT * FROM vip WHERE last_external_update_at IS NULL OR DATE(last_external_update_at) != CURDATE() LIMIT #{batchSize}")
    @Results({
            @Result(column = "积分", property = "points"),
            @Result(column = "未消费天数", property = "nonConsumptionDays"),
            @Result(column = "adress", property = "address"),
            @Result(column = "维护人id", property = "maintainerId"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "last_external_update_at", property = "lastExternalUpdateAt"),
            @Result(column = "维护人id", property = "maintainer",
                    one = @One(select = "com.example.hx_api.mapper.UserMapper.getUserById"))
    })
    List<Vip> findVipsForBatchUpdate(@Param("batchSize") int batchSize);
}
