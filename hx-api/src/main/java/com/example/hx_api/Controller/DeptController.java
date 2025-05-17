package com.example.hx_api.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.hx_api.common.response.Result;
import com.example.hx_api.model.vo.DeptVO;
import com.example.hx_api.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门接口
 */
@RestController
@RequestMapping("/api/depts")
@CrossOrigin // 允许跨域请求
public class DeptController {

    @Autowired
    private DeptService deptService;

    /**
     * 获取所有部门列表 (不含用户信息)
     * 对应旧 /getDepts
     */
    @GetMapping
    public Result<List<DeptVO>> getAllDepts() {
        return Result.success(deptService.getAllDepts());
    }

    /**
     * 获取级联部门列表 (包含部门下的用户基本信息)
     * 对应旧 /getCascadeDept
     */
    @GetMapping("/cascade")
    public Result<List<DeptVO>> getCascadeDepts() {
        return Result.success(deptService.getCascadeDepts());
    }

    /**
     * 根据部门ID获取部门下各用户的VIP统计信息
     * 对应旧 /getDeptCountById
     * @param deptId 部门ID
     * @return 统计信息列表
     */
    @GetMapping("/{deptId}/stats")
    public Result<List<JSONObject>> getDeptStatsById(@PathVariable Integer deptId) {
        return Result.success(deptService.getDeptStatsById(deptId));
    }
}
