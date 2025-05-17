import axios from 'axios';

// 响应处理函数
const handleResponse = response => {
  // 检查响应是否有data.data格式（Result<T>格式）
  if (response.data && Object.prototype.hasOwnProperty.call(response.data, 'code')) {
    return response.data.data;
  }
  return response.data;
};

// 会员相关API
export const vipApi = {
  // 获取念念不忘会员列表
  getNeverForgetVips: async (userId, page, size, sortBy, sortDir) => {
    const params = { page, size };
    if (sortBy && sortDir) {
      params.sortBy = sortBy;
      params.sortDir = sortDir;
    }
    const response = await axios.get(`/api/vips/never-forget/${userId}`, { params });
    return handleResponse(response);
  },
  
  // 获取好久不见会员列表
  getLongTimeNoSeeVips: async (userId, page, size, sortBy, sortDir) => {
    const params = { page, size };
    if (sortBy && sortDir) {
      params.sortBy = sortBy;
      params.sortDir = sortDir;
    }
    const response = await axios.get(`/api/vips/long-time-no-see/${userId}`, { params });
    return handleResponse(response);
  },
  
  // 获取念念不忘会员数量
  getNeverForgetVipsCount: async (userId) => {
    const response = await axios.get(`/api/vips/never-forget-count/${userId}`); // vips
    return handleResponse(response);
  },
  
  // 获取好久不见会员数量
  getLongTimeNoSeeVipsCount: async (userId) => {
    const response = await axios.get(`/api/vips/long-time-no-see-count/${userId}`); // vips
    return handleResponse(response);
  },
  
  // 获取所有VIP数量
  getAllVipsCount: async () => {
    const response = await axios.get('/api/vips/count'); // vips
    return handleResponse(response);
  },
  
  // 获取所有VIP
  getAllVips: async () => {
    const response = await axios.get('/api/vips'); // vips
    return handleResponse(response);
  },
  
  // 根据ID获取VIP信息
  getVipById: async (id) => {
    const response = await axios.get(`/api/vips/${id}`); // vips
    return handleResponse(response);
  },
  
  // 根据用户ID获取VIP列表
  getVipsByUserId: async (userId) => {
    const response = await axios.get(`/api/vips/user/${userId}`); // vips
    return handleResponse(response);
  },
  
  // 根据名称获取VIP列表
  getVipsByName: async (name) => {
    const response = await axios.get(`/api/vips/name/${name}`); // vips
    return handleResponse(response);
  },
  
  // 模糊查询VIP
  findVipsByNameLike: async (name) => {
    const response = await axios.get(`/api/vips/search?name=${name}`); // vips
    return handleResponse(response);
  },

  // 根据手机号查找会员 (新添加)
  findVipsByPhoneNumber: async (phoneNumber) => {
    const response = await axios.get(`/api/vips/by-phone?phoneNumber=${phoneNumber}`); // vips
    return handleResponse(response); // 注意：后端直接返回JSONObject，handleResponse可能需要调整或调用者直接处理原始data
  },
  
  // 根据会员ID获取会员详情 (新添加)
  getVipDetailByHyid: async (hyid) => {
    const response = await axios.get(`/api/vips/detail/${hyid}`);
    return handleResponse(response);
  },
  
  // 添加VIP
  addVip: async (vipData) => {
    const response = await axios.post('/api/vips', vipData); // vips
    return handleResponse(response);
  },
  
  // 更新VIP信息
  updateVip: async (vipData) => {
    const response = await axios.put('/api/vips', vipData); // vips
    return handleResponse(response);
  },
  
  // 删除VIP
  deleteVip: async (id) => {
    const response = await axios.delete(`/api/vips/${id}`); // vips
    return handleResponse(response);
  },
  
  // 移动VIP到新的维护人
  moveVip: async (vipId, userId) => {
    const response = await axios.put(`/api/vips/move/${vipId}/${userId}`); // vips
    return handleResponse(response);
  }
  // 移除了 checkRepeat 和 checkRepeatAdmin
};

// 用户相关API
export const userApi = {
  // 用户登录
  login: async (userData) => {
    const response = await axios.post('/api/user/login', userData);
    return handleResponse(response);
  },
  
  // 获取用户信息
  getUserInfo: async (id) => {
    const response = await axios.get(`/api/user/${id}`);
    return handleResponse(response);
  },
  
  // 更新用户信息
  updateUser: async (userData) => {
    const response = await axios.put('/api/user', userData);
    return handleResponse(response);
  },
  
  // 添加用户
  addUser: async (userData) => {
    const response = await axios.post('/api/user', userData); // 确认后端UserController的addUser是否是 @PostMapping("/")
    return handleResponse(response);
  },
  
  // 移动用户到新的部门
  moveUser: async (userId, deptId) => {
    const response = await axios.put(`/api/user/move/${userId}/${deptId}`);
    return handleResponse(response);
  }
};

// 部门相关API
export const deptApi = {
  // 获取所有部门
  getAllDepts: async () => {
    const response = await axios.get('/api/depts'); // depts
    return handleResponse(response);
  },
  
  // 获取级联部门
  getCascadeDept: async () => {
    const response = await axios.get('/api/depts/cascade'); // depts
    return handleResponse(response);
  },
  
  // 获取部门统计信息
  getDeptStatsById: async (id) => { // Renamed from getDeptCountById for clarity and new path
    const response = await axios.get(`/api/depts/${id}/stats`); // depts
    return handleResponse(response);
  }
};

// 客户相关API (旧版，需要映射到新的VIP API)
export const customerApi = {
  // 根据手机号查找客户 -> 映射到 vipApi.findVipsByPhoneNumber
  findCustomersByPhone: async (phone) => {
    const response = await axios.get(`/api/vips/by-phone?phoneNumber=${phone}`);
    return handleResponse(response); // 注意返回的是原始JSONObject
  },
  
  // 获取客户信息 -> 映射到 vipApi.getVipById (假设hyid是vip的自增id)
  // 如果hyid是业务id，则后端需要相应接口
  getCustomerInfo: async (hyid) => {
    const response = await axios.get(`/api/vips/${hyid}`); // 假设hyid可以作为vip id使用
    return handleResponse(response);
  }
};
