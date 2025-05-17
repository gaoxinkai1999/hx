package com.example.hx_api.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.hx_api.service.VipService;
import com.example.hx_api.client.HxApiClient;
import com.example.hx_api.client.model.request.CustomerInfoRequest;
import com.example.hx_api.client.model.request.FindVipByPhoneRequest;
import com.example.hx_api.client.model.request.RfmRequest;
import com.example.hx_api.common.exception.BusinessException;
import com.example.hx_api.mapper.VipMapper;
import com.example.hx_api.mapper.UserMapper;
import com.example.hx_api.model.dto.VipDTO;
import com.example.hx_api.model.entity.User;
import com.example.hx_api.model.entity.Vip;
import com.example.hx_api.model.vo.UserVO;
import com.example.hx_api.model.vo.VipVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 导入分页相关
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import java.util.Map;
import java.util.HashMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Date; // 导入 Date
import java.time.LocalDateTime; // 导入 LocalDateTime

/**
 * VIP会员服务实现类
 */
@Slf4j
@Service
public class VipServiceImpl implements VipService {

    @Value("${hx.api.batch-update.batch-size:100}") // 从配置文件读取，默认为100
    private int vipUpdateBatchSize;

    // 辅助方法：将前端的排序属性名映射到数据库列名
    private String mapSortPropertyToColumn(String propertyName) {
        if (propertyName == null) {
            return null;
        }
        switch (propertyName) {
            case "points":
                return "积分"; // 根据VipMapper中的@Result
            case "nonConsumptionDays":
                return "未消费天数"; // 根据VipMapper中的@Result
            case "r":
                return "R"; // 假设数据库列名与属性名一致
            case "f":
                return "F"; // 假设数据库列名与属性名一致
            case "m":
                return "M"; // 假设数据库列名与属性名一致
            case "address":
                return "adress"; // 根据VipMapper中的@Result
            case "createTime":
                return "create_time"; // 根据VipMapper中的@Result
            // 如果前端可能传递其他可排序字段，在此处添加映射
            default:
                // 对于其他情况，可以返回属性名本身（如果列名和属性名一致或有自动映射）
                // 或者记录一个警告/错误，因为这是一个未知的排序字段
                log.warn("未知的排序属性: {}", propertyName);
                return propertyName; // 或者返回null/抛出异常，取决于严格性要求
        }
    }
    
    @Autowired
    private VipMapper vipMapper;

    @Autowired
    private UserMapper userMapper; // 注入 UserMapper

    @Autowired
    private HxApiClient hxApiClient;
    
    // 假设有一个UserContext类来获取当前用户信息，实际项目中应通过Spring Security等机制获取
    // 例如:
    // class UserContext {
    //     public static UserVO getCurrentUser() {
    //         // 实现获取当前登录用户逻辑
    //         // UserVO应包含id, role (e.g., "ADMIN", "STAFF"), deptType
    //         UserVO currentUser = new UserVO();
    //         // currentUser.setId(1); // 示例用户ID
    //         // currentUser.setRole("STAFF"); // 示例角色
    //         // currentUser.setDeptType("TYPE_A"); // 示例部门类型
    //         // currentUser.setDeptId(10); // 示例部门ID
    //         return currentUser; // 返回当前用户信息，如果未登录则返回null或抛异常
    //     }
    // }

    @Override
    public VipVO getVipById(Integer id) {
        Vip vip = vipMapper.getVipById(id);
        if (vip == null) {
            return null;
        }
        return convertToVO(vip);
    }
    
    @Override
    public List<VipVO> getVipsByHyid(Integer hyid) {
        List<Vip> vips = vipMapper.getVipsByHyid(hyid);
        return vips.stream().map(this::convertToVO).collect(Collectors.toList());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addVip(VipDTO vipDTO) {
        // 参数校验
        if (vipDTO == null || vipDTO.getHyid() == null || vipDTO.getMaintainerId() == null) {
            log.error("添加VIP失败：参数缺失, vipDTO={}", vipDTO);
            return false;
        }

        // 校验：目标维护人是否已经维护了这个HYID
        int count = vipMapper.checkVipExists(vipDTO.getMaintainerId(), vipDTO.getHyid());
        if (count > 0) {
            log.warn("添加VIP失败：会员ID {} 已由维护人ID {} 维护。", vipDTO.getHyid(), vipDTO.getMaintainerId());
            return false; // 已存在，不允许添加
        }

        // 获取维护人信息，判断其部门信息
        User maintainer = userMapper.getUserById(vipDTO.getMaintainerId());
        if (maintainer == null) {
            log.error("添加VIP失败：维护人不存在, maintainerId={}", vipDTO.getMaintainerId());
            return false;
        }
        
        // 获取部门类型
        String currentUserDeptType = null;
        if (maintainer.getDept() != null) {
            currentUserDeptType = maintainer.getDept().getType();
        }

        // 校验：是否同一部门类型的其他员工已维护此HYID
        // 这里简化为对所有用户都进行这一校验，不再区分ADMIN和STAFF
        List<Vip> existingVipsWithSameHyid = vipMapper.getVipsByHyid(vipDTO.getHyid());
        for (Vip existingVip : existingVipsWithSameHyid) {
            if (existingVip.getMaintainerId() != null && !existingVip.getMaintainerId().equals(vipDTO.getMaintainerId())) {
                User existingMaintainer = userMapper.getUserById(existingVip.getMaintainerId());
                if (existingMaintainer != null && existingMaintainer.getDept() != null &&
                    currentUserDeptType != null && currentUserDeptType.equals(existingMaintainer.getDept().getType())) {
                    log.warn("添加VIP失败：会员ID {} 已由同部门类型下的其他维护人 {} (ID:{}) 维护。",
                            vipDTO.getHyid(), existingMaintainer.getName(), existingMaintainer.getId());
                    return false; // 同部门类型下其他员工已维护此会员
                }
            }
        }

        // 数据转换与保存
        Vip vip = new Vip();
        BeanUtils.copyProperties(vipDTO, vip);
        
        // 设置创建时间，如果实体没有自动设置
        if (vip.getCreateTime() == null) {
            vip.setCreateTime(new java.util.Date());
        }

        // 初始化 lastExternalUpdateAt 为 null 或特定初始值，确保新添加的VIP会被首次更新任务选中
        vip.setLastExternalUpdateAt(null); 

        // 保存到数据库
        vipMapper.insertVip(vip);
        log.info("VIP添加成功: hyid={}, name={}, maintainerId={}", vip.getHyid(), vip.getName(), vip.getMaintainerId());
        return true;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateVip(VipDTO vipDTO) {
        Vip vip = new Vip();
        BeanUtils.copyProperties(vipDTO, vip);
        // 当通过DTO更新时，我们不应该修改lastExternalUpdateAt，除非DTO明确包含了这个字段
        // vipMapper.updateVip 方法会更新传入的vip对象中所有非null字段 (基于MyBatis的动态SQL)
        // 如果vipDTO中不包含lastExternalUpdateAt, 那么vip对象中的此字段为null, update时不会更新它。
        // 如果vipDTO需要能设置lastExternalUpdateAt，则VipDTO也应有此字段。
        // 目前的 updateVip SQL 会更新 last_external_update_at=#{lastExternalUpdateAt}。
        // 如果vip.getLastExternalUpdateAt()为null，它可能将数据库字段设为null，需根据实际SQL行为决定。
        // 为安全起见，如果通过此方法更新时不希望影响 lastExternalUpdateAt，应该：
        // 1. VipDTO 不包含 lastExternalUpdateAt
        // 2. 从数据库先查出Vip完整实体，用DTO的值覆盖部分字段，再保存。
        // 或者，提供一个专门用于部分更新的 updateVipSelective(Vip vip) 方法。
        // 目前假设调用此updateVip是希望完整更新（除了ID），包括lastExternalUpdateAt（如果DTO里有）。
        vipMapper.updateVip(vip); 
    }
    
    @Override
    public Map<String, Object> getNeverForgetVips(Integer userId, int page, int size, String sortBy, String sortDir) {
        if (sortBy != null && !sortBy.isEmpty() && sortDir != null && !sortDir.isEmpty()) {
            String columnName = mapSortPropertyToColumn(sortBy);
            if (columnName != null) { // 确保转换后的列名有效
                String orderDirection = "asc".equalsIgnoreCase(sortDir) ? "ASC" : "DESC";
                PageHelper.orderBy(columnName + " " + orderDirection);
            }
        }
        PageHelper.startPage(page, size);
        List<Vip> vips = vipMapper.getNeverForgetVips(userId);
        PageInfo<Vip> pageInfo = new PageInfo<>(vips);
        List<VipVO> vipVOs = pageInfo.getList().stream().map(this::convertToVO).collect(Collectors.toList());
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", vipVOs);
        result.put("total", pageInfo.getTotal());
        return result;
    }
    
    @Override
    public Map<String, Object> getLongTimeNoSeeVips(Integer userId, int page, int size, String sortBy, String sortDir) {
        if (sortBy != null && !sortBy.isEmpty() && sortDir != null && !sortDir.isEmpty()) {
            String columnName = mapSortPropertyToColumn(sortBy);
            if (columnName != null) { // 确保转换后的列名有效
                String orderDirection = "asc".equalsIgnoreCase(sortDir) ? "ASC" : "DESC";
                PageHelper.orderBy(columnName + " " + orderDirection);
            }
        }
        PageHelper.startPage(page, size);
        List<Vip> vips = vipMapper.getLongTimeNoSeeVips(userId);
        PageInfo<Vip> pageInfo = new PageInfo<>(vips);
        List<VipVO> vipVOs = pageInfo.getList().stream().map(this::convertToVO).collect(Collectors.toList());
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", vipVOs);
        result.put("total", pageInfo.getTotal());
        return result;
    }
    
    @Override
    public int getNeverForgetVipsCount(Integer userId) {
        return vipMapper.getNeverForgetVipsCount(userId);
    }
    
    @Override
    public int getLongTimeNoSeeVipsCount(Integer userId) {
        return vipMapper.getLongTimeNoSeeVipsCount(userId);
    }
    
    @Override
    public List<VipVO> getVipsByUserId(Integer userId) {
        List<Vip> vips = vipMapper.getVipsByUserId(userId);
        return vips.stream().map(this::convertToVO).collect(Collectors.toList());
    }
    
    @Override
    public Map<String, Object> getVipsByUserIdPaged(Integer userId, int page, int size, String sortBy, String sortDir) {
        if (sortBy != null && !sortBy.isEmpty() && sortDir != null && !sortDir.isEmpty()) {
            String columnName = mapSortPropertyToColumn(sortBy);
            if (columnName != null) { // 确保转换后的列名有效
                String orderDirection = "asc".equalsIgnoreCase(sortDir) ? "ASC" : "DESC";
                PageHelper.orderBy(columnName + " " + orderDirection);
            }
        }
        PageHelper.startPage(page, size);
        List<Vip> vips = vipMapper.getVipsByUserId(userId);
        PageInfo<Vip> pageInfo = new PageInfo<>(vips);
        List<VipVO> vipVOs = pageInfo.getList().stream().map(this::convertToVO).collect(Collectors.toList());
        
        Map<String, Object> result = new HashMap<>();
        result.put("list", vipVOs);
        result.put("total", pageInfo.getTotal());
        return result;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean moveVip(Integer vipId, Integer userId) {
        Vip vip = vipMapper.getVipById(vipId);
        if (vip == null) {
            throw new BusinessException("VIP不存在");
        }
        
        int count = vipMapper.checkVipExists(userId, vip.getHyid());
        if (count > 0) {
            return false;
        }
        
        vipMapper.updateVipMaintainer(userId, vipId);
        return true;
    }
    
    @Override
    public List<VipVO> findVipsByNameLike(String name) {
        if (name == null || name.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        // 构建模糊查询条件
        StringBuilder likeName = new StringBuilder("%");
        for (char c : name.trim().toCharArray()) {
            likeName.append(c).append("%");
        }
        
        List<Vip> vips = vipMapper.findVipsByNameLike(likeName.toString());
        return vips.stream().map(this::convertToVO).collect(Collectors.toList());
    }
    
    @Override
    public List<VipVO> getVipsByName(String name) {
        List<Vip> vips = vipMapper.getVipsByName(name.trim());
        return vips.stream().map(this::convertToVO).collect(Collectors.toList());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteVip(Integer id) {
        vipMapper.deleteVip(id);
    }
    
    @Override
    public List<VipVO> getAllVips() {
        List<Vip> vips = vipMapper.getAllVips();
        return vips.stream().map(this::convertToVO).collect(Collectors.toList());
    }
    
    @Override
    public int getAllVipsCount() {
        return vipMapper.getAllVipsCount();
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAllVipsRfmInfo() {
        List<Vip> vips = vipMapper.getAllVips();
        log.info("开始更新VIP RFM信息，总数: {}", vips.size());
        
        for (Vip vip : vips) {
            try {
                // 更新会员详情信息
                JSONObject customerInfoResponse = hxApiClient.getCustomerInfo(new CustomerInfoRequest(vip.getHyid()));
                if (customerInfoResponse != null && customerInfoResponse.getIntValue("STATUS") == 0) { // 成功状态码为0
                    JSONObject message = customerInfoResponse.getJSONObject("MESSAGE");
                    if (message != null) {
                        JSONArray vipInfo = message.getJSONArray("VIPINFO");
                        updateVipInfoFromResponse(vip, vipInfo);
                    }
                } else {
                    log.warn("获取会员详情失败或响应无效，hyid: {} Details: {}", vip.getHyid(), customerInfoResponse != null ? customerInfoResponse.toJSONString() : "null response");
                }
                
                // 更新RFM信息
                JSONObject rfmResponse = hxApiClient.getRfmInfo(new RfmRequest(vip.getHyid()));
                if (rfmResponse != null && rfmResponse.getIntValue("STATUS") == 0) { // 成功状态码为0
                    JSONArray messageArray = rfmResponse.getJSONArray("MESSAGE"); // MESSAGE is an array
                    if (messageArray != null && !messageArray.isEmpty()) {
                        JSONObject rfmInfo = messageArray.getJSONObject(0); // Get the first object from the array
                        updateRfmInfoFromResponse(vip, rfmInfo);
                    }
                } else {
                    log.warn("获取RFM信息失败或响应无效，hyid: {}. 响应详情: {}",
                            vip.getHyid(), rfmResponse != null ? rfmResponse.toJSONString() : "null response");
                }
                
                // 保存更新后的会员信息
                vipMapper.updateVip(vip);
                log.debug("更新VIP成功, hyid: {}, name: {}", vip.getHyid(), vip.getName());
            } catch (Exception e) {
                log.error("更新VIP信息失败, hyid: {}, name: {}, error: {}", vip.getHyid(), vip.getName(), e.getMessage(), e);
            }
        }
        
        log.info("VIP RFM信息更新完成");
    }
    
    /**
     * 从会员详情响应中更新会员信息
     */
    private void updateVipInfoFromResponse(Vip vip, JSONArray vipInfo) {
        if (vipInfo == null) {
            return;
        }
        
        for (int i = 0; i < vipInfo.size(); i++) {
            JSONObject info = vipInfo.getJSONObject(i);
            String field = info.getString("FIELD");
            
            if ("D_LASTBUY".equals(field)) {
                vip.setNonConsumptionDays(info.getIntValue("VALUE"));
            } else if ("N_ALLVALUE".equals(field)) {
                vip.setPoints(info.getIntValue("VALUE"));
            }
        }
    }
    
    /**
     * 从RFM响应中更新会员RFM信息
     */
    private void updateRfmInfoFromResponse(Vip vip, JSONObject rfmInfo) {
        if (rfmInfo == null) {
            return;
        }
        
        vip.setR(rfmInfo.getIntValue("rvalue"));
        vip.setF(rfmInfo.getIntValue("fvalue"));
        vip.setM(rfmInfo.getIntValue("mvalue"));
    }
    
    /**
     * 转换实体为视图对象
     */
    private VipVO convertToVO(Vip vip) {
        if (vip == null) {
            return null;
        }
        
        VipVO vo = new VipVO();
        BeanUtils.copyProperties(vip, vo);
        
        if (vip.getMaintainer() != null) {
            UserVO userVO = new UserVO();
            userVO.setId(vip.getMaintainer().getId());
            userVO.setName(vip.getMaintainer().getName());
            
            if (vip.getMaintainer().getDept() != null) {
                userVO.setDeptName(vip.getMaintainer().getDept().getName());
                userVO.setDeptType(vip.getMaintainer().getDept().getType());
            }
            
            vo.setMaintainer(userVO);
        }
        
        return vo;
    }

    @Override
    public JSONObject findVipsByPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            log.warn("通过手机号查找会员失败：手机号码为空");
            // 或者可以返回一个表示错误的JSONObject
            return new JSONObject().fluentPut("STATUS", -1).fluentPut("MESSAGE", "手机号码不能为空");
        }
        FindVipByPhoneRequest request = new FindVipByPhoneRequest(phoneNumber);
        log.info("开始通过手机号查找会员，请求参数: {}", request);
        try {
            JSONObject response = hxApiClient.sendRequest(request);
            log.info("通过手机号查找会员成功，响应: {}", response);
            return response;
        } catch (Exception e) {
            log.error("通过手机号查找会员失败，手机号: {}, 错误: {}", phoneNumber, e.getMessage(), e);
            // 返回一个表示错误的JSONObject
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("STATUS", -1); // 使用与外部API类似的错误状态码
            // 通常外部API的错误信息在MESSAGE字段，但这里是客户端调用出错
            JSONArray messageArray = new JSONArray();
            messageArray.add(new JSONObject().fluentPut("ERROR_INFO", "调用外部服务查找会员失败: " + e.getMessage()));
            errorResponse.put("MESSAGE", messageArray);
            return errorResponse;
        }
    }

    @Override
    public JSONObject getVipDetailByHyid(Integer hyid) {
        if (hyid == null) {
            log.warn("获取会员详情失败：会员ID为空");
            return new JSONObject().fluentPut("STATUS", -1).fluentPut("MESSAGE", "会员ID不能为空");
        }
        
        CustomerInfoRequest request = new CustomerInfoRequest(hyid);
        log.info("开始获取会员详情，会员ID: {}, 请求参数: {}", hyid, request);
        try {
            // 直接使用sendRequest获取JSONObject响应
            JSONObject response = hxApiClient.sendRequest(request);
            // 无需转换，直接返回
            log.info("获取会员详情成功，会员ID: {}", hyid);
            return response;
        } catch (Exception e) {
            log.error("获取会员详情异常，会员ID: {}, 错误: {}", hyid, e.getMessage(), e);
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("STATUS", -1);
            errorResponse.put("MESSAGE", "调用外部服务获取会员详情失败: " + e.getMessage());
            return errorResponse;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateVipDataFromExternalApi(Integer hyid) {
        if (hyid == null) {
            log.warn("更新VIP数据失败：hyid 为空");
            return;
        }
        // VipMapper.getVipsByHyid 返回 List<Vip>
        List<Vip> vips = vipMapper.getVipsByHyid(hyid);
        if (vips == null || vips.isEmpty()) {
            log.warn("在本地数据库中未找到 hyid 为 {} 的VIP记录，无法更新。", hyid);
            return;
        }
        // 假设我们更新找到的第一个匹配的VIP（如果hyid在本地不是唯一的）
        Vip vipToUpdate = vips.get(0);
        log.info("准备从外部API更新本地VIP记录，hyid: {}, 当前本地名称: {}", vipToUpdate.getHyid(), vipToUpdate.getName());

        try {
            // 1. 获取并更新会员详情信息
            JSONObject customerInfoResponse = hxApiClient.getCustomerInfo(new CustomerInfoRequest(vipToUpdate.getHyid()));
            if (customerInfoResponse != null && customerInfoResponse.getIntValue("STATUS") == 0) {
                JSONObject message = customerInfoResponse.getJSONObject("MESSAGE");
                if (message != null) {
                    JSONArray vipInfoJsonArray = message.getJSONArray("VIPINFO");
                    updateVipInfoFromResponse(vipToUpdate, vipInfoJsonArray); // 使用现有的私有方法
                } else {
                    log.warn("会员详情API响应的MESSAGE为空，hyid: {}", vipToUpdate.getHyid());
                }
            } else {
                log.warn("从外部API获取会员详情失败或响应无效，hyid: {}. 响应详情: {}",
                        vipToUpdate.getHyid(), customerInfoResponse != null ? customerInfoResponse.toJSONString() : "null response");
            }

            // 2. 获取并更新RFM信息
            JSONObject rfmResponse = hxApiClient.getRfmInfo(new RfmRequest(vipToUpdate.getHyid()));
            if (rfmResponse != null && rfmResponse.getIntValue("STATUS") == 0) {
                JSONArray messageArray = rfmResponse.getJSONArray("MESSAGE"); // MESSAGE is an array
                if (messageArray != null && !messageArray.isEmpty()) {
                    JSONObject rfmInfo = messageArray.getJSONObject(0); // Get the first object from the array
                    updateRfmInfoFromResponse(vipToUpdate, rfmInfo); // 使用现有的私有方法
                } else {
                    log.warn("会员RFM API响应的MESSAGE数组为空或null，hyid: {}", vipToUpdate.getHyid());
                }
            } else {
                log.warn("从外部API获取RFM信息失败或响应无效，hyid: {}. 响应详情: {}",
                        vipToUpdate.getHyid(), rfmResponse != null ? rfmResponse.toJSONString() : "null response");
            }

            // 3. 保存更新后的会员信息到数据库
            vipToUpdate.setLastExternalUpdateAt(LocalDateTime.now()); // 使用 LocalDateTime.now()
            vipMapper.updateVip(vipToUpdate);
            log.info("成功从外部API更新了 hyid 为 {} 的VIP信息到本地数据库。", vipToUpdate.getHyid());

        } catch (Exception e) {
            log.error("从外部API更新 hyid 为 {} 的VIP数据时发生异常: {}", vipToUpdate.getHyid(), e.getMessage(), e);
            // 根据业务需求，这里可以考虑抛出自定义异常，或者如果部分成功也算成功，则不抛出
            // throw new BusinessException("更新VIP数据时发生错误: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional // 确保对单个VIP的外部调用和本地更新是事务性的 (如果批处理中单条失败不希望影响其他，可移除此注解，并在循环中细粒度控制事务)
    public void synchronizeNextBatchOfVipsFromExternalApi() {
        log.info("开始执行VIP数据批量同步任务...");
        List<Vip> vipsToUpdate = vipMapper.findVipsForBatchUpdate(vipUpdateBatchSize);

        if (vipsToUpdate == null || vipsToUpdate.isEmpty()) {
            log.info("没有找到需要更新的VIP记录。");
            return;
        }

        log.info("准备更新 {} 条VIP记录 (批次大小: {}).", vipsToUpdate.size(), vipUpdateBatchSize);

        for (Vip vip : vipsToUpdate) {
            log.debug("正在处理 hyid: {} 的VIP更新...", vip.getHyid());
            try {
                // 1. 获取并更新会员详情信息
                JSONObject customerInfoResponse = hxApiClient.getCustomerInfo(new CustomerInfoRequest(vip.getHyid()));
                if (customerInfoResponse != null && customerInfoResponse.getIntValue("STATUS") == 0) {
                    JSONObject message = customerInfoResponse.getJSONObject("MESSAGE");
                    if (message != null) {
                        JSONArray vipInfoJsonArray = message.getJSONArray("VIPINFO");
                        updateVipInfoFromResponse(vip, vipInfoJsonArray);
                    } else {
                        log.warn("会员详情API响应的MESSAGE为空，hyid: {}", vip.getHyid());
                    }
                } else {
                    log.warn("从外部API获取会员详情失败或响应无效 (hyid: {}). 响应: {}",
                            vip.getHyid(), customerInfoResponse != null ? customerInfoResponse.toJSONString() : "null");
                }

                // 2. 获取并更新RFM信息
                JSONObject rfmResponse = hxApiClient.getRfmInfo(new RfmRequest(vip.getHyid()));
                if (rfmResponse != null && rfmResponse.getIntValue("STATUS") == 0) {
                    JSONArray messageArray = rfmResponse.getJSONArray("MESSAGE");
                    if (messageArray != null && !messageArray.isEmpty()) {
                        JSONObject rfmInfo = messageArray.getJSONObject(0);
                        updateRfmInfoFromResponse(vip, rfmInfo);
                    } else {
                        log.warn("会员RFM API响应的MESSAGE数组为空或null，hyid: {}", vip.getHyid());
                    }
                } else {
                    log.warn("从外部API获取RFM信息失败或响应无效 (hyid: {}). 响应: {}",
                            vip.getHyid(), rfmResponse != null ? rfmResponse.toJSONString() : "null");
                }

                // 3. 设置更新时间并保存到数据库
                vip.setLastExternalUpdateAt(LocalDateTime.now()); // 使用 LocalDateTime.now()
                vipMapper.updateVip(vip);
                log.info("成功同步并更新了 hyid: {} 的VIP信息。", vip.getHyid());

            } catch (Exception e) {
                log.error("处理 hyid: {} 的VIP更新时发生异常: {}. 跳过此VIP。", vip.getHyid(), e.getMessage(), e);
                // 可选择性地将此VIP标记为失败，或记录到特定列表以便后续重试
            }
        }
        log.info("VIP数据批量同步任务完成 {} 条记录的处理。", vipsToUpdate.size());
    }
}
