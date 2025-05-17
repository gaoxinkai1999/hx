package com.example.hx_api.service;

import com.example.hx_api.model.dto.UserDTO;
import com.example.hx_api.model.vo.UserVO;

/**
 * 用户服务接口
 */
public interface UserService {
    /**
     * 用户登录
     *
     * @param userDTO 用户DTO
     * @return 用户信息
     */
    UserVO login(UserDTO userDTO);

    /**
     * 获取用户信息
     *
     * @param id 用户ID
     * @return 用户信息
     */
    UserVO getUserInfo(Integer id);

    /**
     * 更新用户信息
     *
     * @param userDTO 用户DTO
     */
    void updateUser(UserDTO userDTO);

    /**
     * 添加用户
     *
     * @param userDTO 用户DTO
     * @return 是否添加成功
     */
    boolean addUser(UserDTO userDTO);

    /**
     * 移动用户到新部门
     *
     * @param userId 用户ID
     * @param deptId 部门ID
     * @return 是否移动成功
     */
    boolean moveUser(Integer userId, Integer deptId);
} 