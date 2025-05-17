package com.example.hx_api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.hx_api.mapper.DeptMapper;
import com.example.hx_api.model.entity.Dept;
import com.example.hx_api.model.entity.User;
import com.example.hx_api.model.vo.DeptVO;
import com.example.hx_api.model.vo.UserBasicVO;
import com.example.hx_api.service.DeptService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Override
    public List<DeptVO> getAllDepts() {
        List<Dept> depts = deptMapper.getAllDepts();
        return depts.stream().map(dept -> {
            DeptVO deptVO = new DeptVO();
            BeanUtils.copyProperties(dept, deptVO);
            // users 列表在此方法中不填充
            return deptVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<DeptVO> getCascadeDepts() {
        List<Dept> deptsWithUsers = deptMapper.getCascadeDept(); // 这个方法应该返回 Dept 包含 List<User>
        return deptsWithUsers.stream().map(dept -> {
            DeptVO deptVO = new DeptVO();
            BeanUtils.copyProperties(dept, deptVO, "users"); // 复制基本属性

            if (dept.getUsers() != null) {
                List<UserBasicVO> userBasicVOs = dept.getUsers().stream().map(user -> {
                    UserBasicVO userBasicVO = new UserBasicVO();
                    userBasicVO.setId(user.getId());
                    userBasicVO.setName(user.getName());
                    return userBasicVO;
                }).collect(Collectors.toList());
                deptVO.setUsers(userBasicVOs);
            } else {
                deptVO.setUsers(new ArrayList<>()); // 确保 users 列表不为 null
            }
            return deptVO;
        }).collect(Collectors.toList());
    }

    @Override
    public List<JSONObject> getDeptStatsById(Integer deptId) {
        // 调用DeptMapper中新添加的方法
        return deptMapper.getDeptUserVipStats(deptId);
    }
}
