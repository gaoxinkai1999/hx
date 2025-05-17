package com.example.hx_api.model.vo;

import lombok.Data;
import java.util.List;

/**
 * 部门信息视图对象 (可能包含用户列表)
 */
@Data
public class DeptVO {
    private Integer id;
    private String name;
    private String type;
    private List<UserBasicVO> users; // 用于级联查询时显示部门下的用户
}
