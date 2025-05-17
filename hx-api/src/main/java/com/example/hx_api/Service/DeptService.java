package com.example.hx_api.service;

import com.alibaba.fastjson.JSONObject;
import com.example.hx_api.model.vo.DeptVO;
import java.util.List;

/**
 * 部门服务接口
 */
public interface DeptService {

    /**
     * 获取所有部门列表 (不含用户信息)
     * @return 部门视图对象列表
     */
    List<DeptVO> getAllDepts();

    /**
     * 获取级联部门列表 (包含部门下的用户基本信息)
     * @return 部门视图对象列表 (包含用户)
     */
    List<DeptVO> getCascadeDepts();

    /**
     * 根据部门ID获取部门统计信息
     * (例如：部门下各用户的"念念不忘"和"好久不见"会员数量)
     * @param deptId 部门ID
     * @return 统计信息列表，每个元素是一个JSONObject，代表一个用户的统计
     */
    List<JSONObject> getDeptStatsById(Integer deptId);
}
