package com.example.hx_api.model.vo;

import lombok.Data;

/**
 * 用户视图对象
 */
@Data
public class UserVO {
    /**
     * 用户ID
     */
    private Integer id;
    
    /**
     * 用户名
     */
    private String name;
    
    /**
     * 部门名称
     */
    private String deptName;
    
    /**
     * 部门类型
     */
    private String deptType;
} 