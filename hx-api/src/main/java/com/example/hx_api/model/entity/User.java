package com.example.hx_api.model.entity;

import lombok.Data;

/**
 * 用户实体类
 */
@Data
public class User {
    /**
     * 用户ID
     */
    private Integer id;
    
    /**
     * 用户名
     */
    private String name;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 所属部门
     */
    private Dept dept;
    
    /**
     * 所属部门ID
     */
    private Integer 所属部门id;
    
    /**
     * 临时字段，与Java命名规范一致，用于mybatis映射
     */
    private Integer deptId;
} 