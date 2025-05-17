package com.example.hx_api.model.dto;

import lombok.Data;

/**
 * 用户数据传输对象
 */
@Data
public class UserDTO {
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
     * 所属部门ID
     */
    private Integer deptId;
    
    /**
     * 所属部门对象
     */
    private Object 所属部门;
} 