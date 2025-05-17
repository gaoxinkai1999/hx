package com.example.hx_api.service.impl;

import com.example.hx_api.mapper.UserMapper;
import com.example.hx_api.model.dto.UserDTO;
import com.example.hx_api.model.entity.User;
import com.example.hx_api.model.vo.UserVO;
import com.example.hx_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserVO login(UserDTO userDTO) {
        // 根据用户名和密码查询用户
        User user = userMapper.login(userDTO.getName(), userDTO.getPassword());
        // 如果用户不存在，返回null
        if (user == null) {
            return null;
        }
        // 转换为VO对象
        return convertToVO(user);
    }

    @Override
    public UserVO getUserInfo(Integer id) {
        User user = userMapper.getUserById(id);
        if (user == null) {
            return null;
        }
        return convertToVO(user);
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        userMapper.updateUser(user);
    }

    @Override
    public boolean addUser(UserDTO userDTO) {
        User existingUser = userMapper.getUserByName(userDTO.getName());
        if (existingUser != null) {
            return false;
        }
        User user = convertToEntity(userDTO);
        return userMapper.addUser(user) > 0;
    }

    @Override
    public boolean moveUser(Integer userId, Integer deptId) {
        return userMapper.moveUser(userId, deptId) > 0;
    }

    /**
     * 将实体对象转换为视图对象
     */
    private UserVO convertToVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setName(user.getName());
        
        if (user.getDept() != null) {
            vo.setDeptName(user.getDept().getName());
            vo.setDeptType(user.getDept().getType());
        }
        
        return vo;
    }

    /**
     * 将DTO对象转换为实体对象
     */
    private User convertToEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setId(dto.getId());
        user.setName(dto.getName());
        user.setPassword(dto.getPassword());
        
        // 设置所属部门ID
        user.set所属部门id(dto.getDeptId());
        
        // 同时设置deptId字段，确保兼容性
        user.setDeptId(dto.getDeptId());
        
        return user;
    }
} 