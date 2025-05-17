package com.example.hx_api.controller;

import com.example.hx_api.common.response.Result;
import com.example.hx_api.model.dto.UserDTO;
import com.example.hx_api.model.vo.UserVO;
import com.example.hx_api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<UserVO> login(@RequestBody UserDTO userDTO) {
        UserVO userVO = userService.login(userDTO);
        if (userVO == null) {
            return Result.fail("用户名或密码错误");
        }
        return Result.success(userVO);
    }
    
    /**
     * 获取用户信息
     */
    @GetMapping("/{id}")
    public Result<UserVO> getUserInfo(@PathVariable Integer id) {
        return Result.success(userService.getUserInfo(id));
    }
    
    /**
     * 更新用户信息
     */
    @PutMapping
    public Result<Void> updateUser(@RequestBody UserDTO userDTO) {
        userService.updateUser(userDTO);
        return Result.success();
    }
    
    /**
     * 添加用户
     */
    @PostMapping
    public Result<Boolean> addUser(@RequestBody UserDTO userDTO) {
        return Result.success(userService.addUser(userDTO));
    }
    
    /**
     * 移动用户到新部门
     */
    @PutMapping("/move/{userId}/{deptId}")
    public Result<Boolean> moveUser(@PathVariable Integer userId, @PathVariable Integer deptId) {
        return Result.success(userService.moveUser(userId, deptId));
    }
} 