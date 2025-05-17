package com.example.hx_api.mapper;

import com.example.hx_api.model.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper {
    /**
     * 根据ID获取用户
     */
    @Select("SELECT u.*, d.id as dept_id, d.name as dept_name, d.type as dept_type " +
            "FROM user u " +
            "LEFT JOIN dept d ON u.所属部门id = d.id " +
            "WHERE u.id = #{id}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "password", property = "password"),
            @Result(column = "dept_id", property = "deptId"),
            @Result(property = "dept", column = "dept_id",
                    one = @One(select = "com.example.hx_api.mapper.DeptMapper.getDeptById"))
    })
    User getUserById(Integer id);

    /**
     * 根据用户名获取用户
     */
    @Select("SELECT u.*, d.id as dept_id, d.name as dept_name, d.type as dept_type " +
            "FROM user u " +
            "LEFT JOIN dept d ON u.所属部门id = d.id " +
            "WHERE u.name = #{name}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "password", property = "password"),
            @Result(column = "dept_id", property = "deptId"),
            @Result(property = "dept", column = "dept_id",
                    one = @One(select = "com.example.hx_api.mapper.DeptMapper.getDeptById"))
    })
    User getUserByName(String name);

    /**
     * 根据部门ID获取用户列表
     */
    @Select("SELECT * FROM user WHERE 所属部门id = #{deptId}")
    List<User> getUsersByDeptId(Integer deptId);

    /**
     * 添加用户
     */
    @Insert("INSERT INTO user(name, password, 所属部门id) VALUES(#{name}, #{password}, #{deptId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addUser(User user);

    /**
     * 修改用户
     */
    @Update("UPDATE user SET name=#{name}, password=#{password}, 所属部门id=#{deptId} WHERE id=#{id}")
    int updateUser(User user);

    /**
     * 移动用户到新部门
     */
    @Update("UPDATE user SET 所属部门id=#{deptId} WHERE id=#{userId}")
    int moveUser(@Param("userId") Integer userId, @Param("deptId") Integer deptId);

    /**
     * 根据用户名和密码获取用户
     */
    @Select("SELECT u.*, d.id as dept_id, d.name as dept_name, d.type as dept_type " +
            "FROM user u " +
            "LEFT JOIN dept d ON u.所属部门id = d.id " +
            "WHERE u.name = #{name} AND u.password = #{password}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "password", property = "password"),
            @Result(column = "dept_id", property = "deptId"),
            @Result(property = "dept", column = "dept_id",
                    one = @One(select = "com.example.hx_api.mapper.DeptMapper.getDeptById"))
    })
    User login(@Param("name") String name, @Param("password") String password);
} 