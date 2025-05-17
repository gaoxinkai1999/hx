package com.example.hx_api.controller;

import com.example.hx_api.service.VipService;
import com.example.hx_api.common.response.Result;
import com.example.hx_api.model.dto.VipDTO;
import com.example.hx_api.model.vo.VipVO;
// 引入 PageInfo 或自定义分页VO的包，如果service层使用特定分页组件
// import com.github.pagehelper.PageInfo; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map; // 用于包装分页结果

// 导入 Result 和 JSONObject
import com.example.hx_api.common.response.Result;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray; // 新增导入

/**
 * VIP会员接口
 */
@RestController
@RequestMapping("/api/vips")
@CrossOrigin // 允许跨域请求
public class VipController {
    @Autowired
    private VipService vipService;

    /**
     * 根据ID获取VIP信息
     */
    @GetMapping("/{id}")
    public Result<VipVO> getVipById(@PathVariable Integer id) {
        return Result.success(vipService.getVipById(id));
    }
    
    /**
     * 添加VIP
     */
    @PostMapping
    public Result<Boolean> addVip(@RequestBody VipDTO vipDTO) {
        return Result.success(vipService.addVip(vipDTO));
    }
    
    /**
     * 更新VIP信息
     */
    @PutMapping
    public Result<Void> updateVip(@RequestBody VipDTO vipDTO) {
        vipService.updateVip(vipDTO);
        return Result.success();
    }
    
    /**
     * 获取"念念不忘"会员列表 (分页)
     */
    @GetMapping("/never-forget/{userId}")
    public Result<Map<String, Object>> getNeverForgetVips(@PathVariable Integer userId,
                                                       @RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(required = false) String sortBy,
                                                       @RequestParam(required = false) String sortDir) {
        // 假设 vipService.getNeverForgetVips 返回 Map<String, Object> 包含 "list" 和 "total"
        // 或者返回 PageInfo<VipVO> 等分页对象
        return Result.success(vipService.getNeverForgetVips(userId, page, size, sortBy, sortDir));
    }
    
    /**
     * 获取"好久不见"会员列表 (分页)
     */
    @GetMapping("/long-time-no-see/{userId}")
    public Result<Map<String, Object>> getLongTimeNoSeeVips(@PathVariable Integer userId,
                                                         @RequestParam(defaultValue = "1") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(required = false) String sortBy,
                                                         @RequestParam(required = false) String sortDir) {
        // 假设 vipService.getLongTimeNoSeeVips 返回 Map<String, Object> 包含 "list" 和 "total"
        // 或者返回 PageInfo<VipVO> 等分页对象
        return Result.success(vipService.getLongTimeNoSeeVips(userId, page, size, sortBy, sortDir));
    }
    
    /**
     * 获取"念念不忘"会员数量
     */
    @GetMapping("/never-forget-count/{userId}")
    public Result<Integer> getNeverForgetVipsCount(@PathVariable Integer userId) {
        return Result.success(vipService.getNeverForgetVipsCount(userId));
    }
    
    /**
     * 获取"好久不见"会员数量
     */
    @GetMapping("/long-time-no-see-count/{userId}")
    public Result<Integer> getLongTimeNoSeeVipsCount(@PathVariable Integer userId) {
        return Result.success(vipService.getLongTimeNoSeeVipsCount(userId));
    }
    
    /**
     * 根据用户ID获取VIP列表
     */
    @GetMapping("/user/{userId}")
    public Result<List<VipVO>> getVipsByUserId(@PathVariable Integer userId) {
        return Result.success(vipService.getVipsByUserId(userId));
    }
    
    /**
     * 根据用户ID获取VIP列表 (分页)
     */
    @GetMapping("/user/{userId}/paged")
    public Result<Map<String, Object>> getVipsByUserIdPaged(@PathVariable Integer userId,
                                                       @RequestParam(defaultValue = "1") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       @RequestParam(required = false) String sortBy,
                                                       @RequestParam(required = false) String sortDir) {
        return Result.success(vipService.getVipsByUserIdPaged(userId, page, size, sortBy, sortDir));
    }
    
    /**
     * 移动VIP到新的维护人
     */
    @PutMapping("/move/{vipId}/{userId}")
    public Result<Boolean> moveVip(@PathVariable Integer vipId, @PathVariable Integer userId) {
        return Result.success(vipService.moveVip(vipId, userId));
    }
    
    /**
     * 模糊查询VIP
     */
    @GetMapping("/search")
    public Result<List<VipVO>> findVipsByNameLike(@RequestParam String name) {
        return Result.success(vipService.findVipsByNameLike(name));
    }
    
    /**
     * 根据名称获取VIP列表
     */
    @GetMapping("/name/{name}")
    public Result<List<VipVO>> getVipsByName(@PathVariable String name) {
        return Result.success(vipService.getVipsByName(name));
    }
    
    /**
     * 删除VIP
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteVip(@PathVariable Integer id) {
        vipService.deleteVip(id);
        return Result.success();
    }
    
    /**
     * 获取所有VIP
     */
    @GetMapping
    public Result<List<VipVO>> getAllVips() {
        return Result.success(vipService.getAllVips());
    }
    
    /**
     * 获取所有VIP数量
     */
    @GetMapping("/count")
    public Result<Integer> getAllVipsCount() {
        return Result.success(vipService.getAllVipsCount());
    }

    /**
     * 根据手机号码查找会员列表 (调用外部API)
     *
     * @param phoneNumber 手机号码
     * @return 外部API返回的原始JSON数据，包装在Result中
     */
    @GetMapping("/by-phone")
    public Result<JSONObject> findVipsByPhoneNumber(@RequestParam String phoneNumber) {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            return Result.fail(400, "手机号不能为空");
        }
        
        JSONObject result = vipService.findVipsByPhoneNumber(phoneNumber);
        
        // 外部API返回的STATUS为0表示成功
        if (result != null && result.getIntValue("STATUS") == 0) { 
            return Result.success(result);
        } else {
            // 从外部API错误响应中提取错误信息
            String errorMessage = "查找会员失败";
            int errorCode = 500;

            if (result != null) {
                errorCode = result.getIntValue("STATUS");
                
                // 尝试提取错误消息
                Object messageObj = result.get("MESSAGE");
                if (messageObj instanceof String) {
                    errorMessage = (String) messageObj;
                } else if (messageObj instanceof JSONArray) {
                    JSONArray messageArray = (JSONArray) messageObj;
                    if (!messageArray.isEmpty()) {
                        Object firstItem = messageArray.get(0);
                        if (firstItem instanceof JSONObject) {
                            JSONObject firstMessageObj = (JSONObject) firstItem;
                            if (firstMessageObj.containsKey("ERROR_INFO")) {
                                errorMessage = firstMessageObj.getString("ERROR_INFO");
                            } else if (firstMessageObj.containsKey("INFO")) {
                                errorMessage = firstMessageObj.getString("INFO");
                            }
                        }
                    }
                }
            }
            
            // 返回友好的错误结果
            return Result.fail(errorCode, errorMessage);
        }
    }

    /**
     * 根据会员ID获取VIP详细信息 (调用外部API)
     *
     * @param hyid 会员ID
     * @return 外部API返回的会员详情，包装在Result中
     */
    @GetMapping("/detail/{hyid}")
    public Result<JSONObject> getVipDetailByHyid(@PathVariable Integer hyid) {
        if (hyid == null) {
            return Result.fail(400, "会员ID不能为空");
        }
        
        JSONObject result = vipService.getVipDetailByHyid(hyid);
        
        // 外部API返回的STATUS为0表示成功
        if (result != null && result.getIntValue("STATUS") == 0) { 
            return Result.success(result);
        } else {
            // 从外部API错误响应中提取错误信息
            String errorMessage = "获取会员详情失败";
            int errorCode = 500;
            
            if (result != null) {
                errorCode = result.getIntValue("STATUS");
                
                // 尝试提取错误消息
                Object messageObj = result.get("MESSAGE");
                if (messageObj instanceof String) {
                    errorMessage = (String) messageObj;
                } else if (messageObj instanceof JSONArray) {
                    JSONArray messageArray = (JSONArray) messageObj;
                    if (!messageArray.isEmpty()) {
                        Object firstItem = messageArray.get(0);
                        if (firstItem instanceof JSONObject) {
                            JSONObject firstMessageObj = (JSONObject) firstItem;
                            if (firstMessageObj.containsKey("ERROR_INFO")) {
                                errorMessage = firstMessageObj.getString("ERROR_INFO");
                            } else if (firstMessageObj.containsKey("INFO")) {
                                errorMessage = firstMessageObj.getString("INFO");
                            }
                            }
                    }
                        }
                    }
            
            // 返回友好的错误结果
            return Result.fail(errorCode, errorMessage);
        }
    }
}
