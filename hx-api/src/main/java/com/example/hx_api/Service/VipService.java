package com.example.hx_api.service;

import com.alibaba.fastjson.JSONObject; // Keep existing import for JSONObject
import com.example.hx_api.model.dto.VipDTO;
import com.example.hx_api.model.vo.VipVO;

import java.util.List;
import java.util.Map; // 导入 Map

/**
 * VIP会员服务接口
 */
public interface VipService {
    /**
     * 根据ID获取VIP信息
     *
     * @param id VIP ID
     * @return VIP信息
     */
    VipVO getVipById(Integer id);
    
    /**
     * 根据会员ID获取VIP信息
     *
     * @param hyid 会员ID
     * @return VIP信息列表
     */
    List<VipVO> getVipsByHyid(Integer hyid);
    
    /**
     * 添加VIP
     *
     * @param vipDTO VIP数据传输对象
     * @return 是否添加成功
     */
    boolean addVip(VipDTO vipDTO);
    
    /**
     * 更新VIP信息
     *
     * @param vipDTO VIP数据传输对象
     */
    void updateVip(VipDTO vipDTO);
    
    /**
     * 获取"念念不忘"会员列表
     *
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页数量
     * @param sortBy 排序字段
     * @param sortDir 排序方向 (asc/desc)
     * @return 包含会员列表和总数的Map
     */
    Map<String, Object> getNeverForgetVips(Integer userId, int page, int size, String sortBy, String sortDir);
    
    /**
     * 获取"好久不见"会员列表
     *
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页数量
     * @param sortBy 排序字段
     * @param sortDir 排序方向 (asc/desc)
     * @return 包含会员列表和总数的Map
     */
    Map<String, Object> getLongTimeNoSeeVips(Integer userId, int page, int size, String sortBy, String sortDir);
    
    /**
     * 获取"念念不忘"会员数量
     *
     * @param userId 用户ID
     * @return 会员数量
     */
    int getNeverForgetVipsCount(Integer userId);
    
    /**
     * 获取"好久不见"会员数量
     *
     * @param userId 用户ID
     * @return 会员数量
     */
    int getLongTimeNoSeeVipsCount(Integer userId);
    
    /**
     * 根据用户ID获取VIP列表
     *
     * @param userId 用户ID
     * @return 会员列表
     */
    List<VipVO> getVipsByUserId(Integer userId);
    
    /**
     * 根据用户ID获取VIP列表 (分页)
     *
     * @param userId 用户ID
     * @param page 页码
     * @param size 每页数量
     * @param sortBy 排序字段
     * @param sortDir 排序方向 (asc/desc)
     * @return 包含会员列表和总数的Map
     */
    Map<String, Object> getVipsByUserIdPaged(Integer userId, int page, int size, String sortBy, String sortDir);
    
    /**
     * 移动VIP到新的维护人
     *
     * @param vipId  VIP ID
     * @param userId 新的维护人ID
     * @return 是否移动成功
     */
    boolean moveVip(Integer vipId, Integer userId);
    
    /**
     * 模糊查询VIP
     *
     * @param name 会员名称
     * @return 会员列表
     */
    List<VipVO> findVipsByNameLike(String name);
    
    /**
     * 根据名称获取VIP列表
     *
     * @param name 会员名称
     * @return 会员列表
     */
    List<VipVO> getVipsByName(String name);
    
    /**
     * 删除VIP
     *
     * @param id VIP ID
     */
    void deleteVip(Integer id);
    
    /**
     * 获取所有VIP
     *
     * @return 会员列表
     */
    List<VipVO> getAllVips();
    
    /**
     * 获取所有VIP数量
     *
     * @return 会员数量
     */
    int getAllVipsCount();
    
    /**
     * 更新所有VIP的RFM信息 (定时任务调用)
     */
    void updateAllVipsRfmInfo();

    /**
     * 根据手机号码查找会员列表 (调用外部API)
     *
     * @param phoneNumber 手机号码
     * @return 调用外部API返回的JSONObject，可能包含会员列表或其他信息
     */
    JSONObject findVipsByPhoneNumber(String phoneNumber);

    /**
     * 根据会员ID获取VIP详细信息 (调用外部API)
     *
     * @param hyid 会员ID
     * @return 调用外部API返回的JSONObject，包含会员详情
     */
    JSONObject getVipDetailByHyid(Integer hyid);

    /**
     * 更新指定hyid的VIP的详情和RFM信息 (用于测试或单条更新)
     *
     * @param hyid 要更新的会员的hyid
     */
    void updateVipDataFromExternalApi(Integer hyid);

    /**
     * 处理下一批VIP数据的同步更新任务（由定时任务调用）。
     * 该方法会获取一批需要更新的VIP，并逐个从外部API同步它们的数据。
     */
    void synchronizeNextBatchOfVipsFromExternalApi();
}
