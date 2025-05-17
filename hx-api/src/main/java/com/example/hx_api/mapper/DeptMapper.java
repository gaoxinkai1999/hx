package com.example.hx_api.mapper;

import com.alibaba.fastjson.JSONObject; // 新增导入
import com.example.hx_api.model.entity.Dept;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 部门Mapper接口
 */
@Mapper
public interface DeptMapper {
    /**
     * 根据ID获取部门
     */
    @Select("SELECT * FROM dept WHERE id = #{id}")
    Dept getDeptById(Integer id);

    /**
     * 获取所有部门
     */
    @Select("SELECT * FROM dept")
    List<Dept> getAllDepts();

    /**
     * 获取部门数量
     */
    @Select("SELECT COUNT(*) FROM user WHERE 所属部门id = #{deptId}")
    int getDeptCountById(Integer deptId);

    /**
     * 获取部门级联数据
     */
    @Select("SELECT * FROM dept")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "type", property = "type"),
            @Result(property = "users", column = "id",
                    many = @Many(select = "com.example.hx_api.mapper.UserMapper.getUsersByDeptId"))
    })
    List<Dept> getCascadeDept();

    /**
     * 获取指定部门下各用户的VIP统计信息 ("念念不忘", "好久不见" 数量)
     * SQL逻辑同旧版 DeptDao.getDeptCountById
     * @param deptId 部门ID
     * @return List of JSONObject, each representing a user with 'name', '念念不忘', '好久不见' fields.
     */
    @Select("SELECT user.name, " +
            "(SELECT IF(COUNT(*) >= 200, 200, COUNT(*)) FROM vip WHERE 未消费天数 <= 180 AND 维护人id = user.id) AS `念念不忘`, " +
            "(SELECT COUNT(*) - (SELECT IF(COUNT(*) >= 200, 200, COUNT(*)) FROM vip WHERE 未消费天数 <= 180 AND 维护人id = user.id) FROM vip WHERE 维护人id = user.id) AS `好久不见` " +
            "FROM user WHERE 所属部门id = #{deptId}")
    List<JSONObject> getDeptUserVipStats(@Param("deptId") Integer deptId);
}
