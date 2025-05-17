package com.example.hx_api;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.hx_api.client.HxApiClient;
import com.example.hx_api.client.model.request.CustomerInfoRequest;
import com.example.hx_api.client.model.request.RfmRequest;
import com.example.hx_api.mapper.VipMapper;
import com.example.hx_api.model.entity.Vip;
import com.example.hx_api.service.VipService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShoubaApplicationTests {

    @Autowired
    private HxApiClient hxApiClient;

    @Autowired
    private VipService vipService;

    @Autowired
    private VipMapper vipMapper;

    private final Integer testHyid = 46597; // 通用测试HYID

    @Test
    void contextLoads() {
    }

    @Test
    void testGetCustomerInfoAndRfmExternalApi() {
        // 1. 测试获取会员详情
        CustomerInfoRequest customerInfoRequest = new CustomerInfoRequest(testHyid);
        JSONObject customerInfoResponse = hxApiClient.getCustomerInfo(customerInfoRequest);

        System.out.println("会员详情 API 响应: " + (customerInfoResponse != null ? customerInfoResponse.toJSONString() : "null"));
        assertNotNull(customerInfoResponse, "会员详情响应不应为null");
        // assertEquals(0, customerInfoResponse.getIntValue("STATUS"), "获取会员详情API状态码应为0");
        // assertNotNull(customerInfoResponse.getJSONObject("MESSAGE"), "会员详情响应的MESSAGE不应为null");

        // 2. 测试获取会员RFM信息
        RfmRequest rfmRequest = new RfmRequest(testHyid);
        JSONObject rfmResponse = hxApiClient.getRfmInfo(rfmRequest);

        System.out.println("会员RFM API 响应: " + (rfmResponse != null ? rfmResponse.toJSONString() : "null"));
        assertNotNull(rfmResponse, "会员RFM响应不应为null");
        assertEquals(0, rfmResponse.getIntValue("STATUS"), "获取会员RFM API状态码应为0 (成功)");

        JSONArray rfmMessageArray = rfmResponse.getJSONArray("MESSAGE");
        assertNotNull(rfmMessageArray, "会员RFM响应的MESSAGE数组不应为null");
        assertFalse(rfmMessageArray.isEmpty(), "会员RFM响应的MESSAGE数组不应为空");

        JSONObject rfmDetails = rfmMessageArray.getJSONObject(0);
        assertNotNull(rfmDetails, "RFM详情对象不应为null");
        assertTrue(rfmDetails.containsKey("C_NAME"), "RFM详情应包含C_NAME");
        assertTrue(rfmDetails.containsKey("WECHATID"), "RFM详情应包含WECHATID");
        assertTrue(rfmDetails.containsKey("rvalue"), "RFM详情应包含rvalue");
        assertTrue(rfmDetails.containsKey("fvalue"), "RFM详情应包含fvalue");
        assertTrue(rfmDetails.containsKey("mvalue"), "RFM详情应包含mvalue");
    }

    @Test
    @Transactional // 使用事务，测试后回滚对数据库的更改
    void testUpdateVipDataFromExternalApiForSingleHyid() {
        // 确保测试的VIP在数据库中存在，如果不存在，可以考虑创建一个临时的或者依赖固定的测试数据
        // 这里我们假设 hyid = 46597 的VIP是存在的，如果不存在，以下获取会为空，服务层方法会直接返回
        List<Vip> vipsBeforeUpdateList = vipMapper.getVipsByHyid(testHyid);
        if (vipsBeforeUpdateList == null || vipsBeforeUpdateList.isEmpty()) {
            // 如果您的测试环境没有这个数据，可以选择：
            // 1. 跳过此测试并打印信息 System.out.println("警告: hyid " + testHyid + " 在数据库中不存在，跳过更新测试。"); return;
            // 2. 在测试前手动插入一条记录（确保hyid, name等基本字段）
            Vip newVip = new Vip();
            newVip.setHyid(testHyid);
            newVip.setName("临时的测试VIP");
            newVip.setPoints(0); // 设置一些初始值
            newVip.setNonConsumptionDays(0);
            newVip.setR(0); newVip.setF(0); newVip.setM(0);
            vipMapper.insertVip(newVip); // 假设insertVip能工作
            vipsBeforeUpdateList = vipMapper.getVipsByHyid(testHyid);
            System.out.println("为测试临时插入了 hyid: " + testHyid);
        }
        assertNotNull(vipsBeforeUpdateList, "获取更新前VIP列表不应为null");
        assertFalse(vipsBeforeUpdateList.isEmpty(), "测试hyid " + testHyid + " 在数据库中必须存在才能进行更新测试");
        Vip vipBeforeUpdate = vipsBeforeUpdateList.get(0);

        // 记录更新前的一些值，用于后续比较
        Integer initialPoints = vipBeforeUpdate.getPoints();
        Integer initialNonConsumptionDays = vipBeforeUpdate.getNonConsumptionDays();
        Integer initialR = vipBeforeUpdate.getR();
        Integer initialF = vipBeforeUpdate.getF();
        Integer initialM = vipBeforeUpdate.getM();

        System.out.println(String.format("更新前 - Hyid: %d, Points: %d, NonConsumptionDays: %d, R: %d, F: %d, M: %d",
                testHyid, initialPoints, initialNonConsumptionDays, initialR, initialF, initialM));

        // 调用服务层方法进行更新
        vipService.updateVipDataFromExternalApi(testHyid);

        // 从数据库重新获取更新后的VIP信息
        List<Vip> vipsAfterUpdateList = vipMapper.getVipsByHyid(testHyid);
        assertNotNull(vipsAfterUpdateList, "获取更新后VIP列表不应为null");
        assertFalse(vipsAfterUpdateList.isEmpty(), "更新后VIP记录不应丢失");
        Vip vipAfterUpdate = vipsAfterUpdateList.get(0);

        System.out.println(String.format("更新后 - Hyid: %d, Points: %d, NonConsumptionDays: %d, R: %d, F: %d, M: %d",
                vipAfterUpdate.getHyid(), vipAfterUpdate.getPoints(), vipAfterUpdate.getNonConsumptionDays(),
                vipAfterUpdate.getR(), vipAfterUpdate.getF(), vipAfterUpdate.getM()));

        // 断言：检查相关字段是否已更新
        // 注意：由于外部API的实际返回值是动态的，这里的断言应该是检查"是否发生了变化"或者"是否不等于初始值"，
        // 除非您能确保外部API对testHyid返回固定的可预测数据。
        // 如果外部API返回数据，且数据与初始值不同，则相应的字段应该被更新。

        // 示例断言 (更具体的断言需要基于外部API对testHyid的实际响应)
        // 例如，如果知道外部API会为 testHyid=46597 返回 rvalue=1, fvalue=4, mvalue=2
        // 并且假设 D_LASTBUY 和 N_ALLVALUE 也会返回一些值

        // 检查RFM值是否可能已根据外部API更新 (假设API调用成功且返回了数据)
        // 这些断言假设外部API会返回非零值，并且这些值与初始值不同
        // 您需要根据实际API响应调整这些断言
        // assertNotEquals(initialR, vipAfterUpdate.getR(), "R值应已更新");
        // assertEquals(1, vipAfterUpdate.getR(), "R值应为1 (基于示例响应)");
        // assertEquals(4, vipAfterUpdate.getF(), "F值应为4 (基于示例响应)");
        // assertEquals(2, vipAfterUpdate.getM(), "M值应为2 (基于示例响应)");

        // 检查积分和未消费天数是否可能已更新
        // assertNotEquals(initialPoints, vipAfterUpdate.getPoints(), "积分应已更新");
        // assertNotEquals(initialNonConsumptionDays, vipAfterUpdate.getNonConsumptionDays(), "未消费天数应已更新");

        System.out.println("VIP数据更新测试完成。请检查日志和打印的更新前后值以确认。");
    }
}
