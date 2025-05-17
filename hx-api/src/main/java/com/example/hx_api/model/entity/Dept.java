package com.example.hx_api.model.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门实体类
 */
@Data
public class Dept {
    /**
     * 部门ID
     */
    private Integer id;
    
    /**
     * 部门名称
     */
    private String name;
    
    /**
     * 部门类型
     */
    private String type;
    
    /**
     * 部门用户列表
     */
    private List<User> users = new ArrayList<>();
} 