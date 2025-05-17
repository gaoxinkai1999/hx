<template>
<div>
  <van-nav-bar
      title="会员详情"
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
  />
  <van-cell-group>
    <van-cell center title="姓名" :value="vip.name" size="large" />
    <van-cell center title="年龄" :value="vip.age" size="large" />
    <van-cell center title="积分" :value="vip.points" size="large" />
    <van-cell center title="手机号" :value="vip.phone" size="large" />
    <van-cell center title="地址" :value="vip.address" size="large" />
    <van-cell center title="未消费天数" :value="vip.nonConsumptionDays + '天'" size="large" />
    <van-cell center title="RFM值" :value="vip.r + ':' + vip.f + ':' + vip.m" size="large" />
  </van-cell-group>
</div>
</template>

<script>
import { vipApi } from '@/services/api.js'; // 导入新的api服务
import { Toast } from 'vant'; // 导入 Toast

export default {
  name: "VipInfo",
  data(){
    return{
      vip: {
        id: '',
        hyid: '',
        name: '',
        age: '',
        points: 0,
        phone: '',
        address: '',
        nonConsumptionDays: 0,
        r: 0,
        f: 0,
        m: 0
      }
    }
  },
  methods:{
    onClickLeft() {
      this.$router.back()
    },
    async fetchVipInfo() {
      const vipId = this.$route.query.id;
      if (!vipId) {
        Toast.fail("未找到VIP ID");
        this.$router.back(); // 无ID则返回
        return;
      }
      
      try {
        // 加载提示
        Toast.loading({
          message: '加载会员详情...',
          forbidClick: true,
        });
        
        // 首先尝试获取基本会员信息
        const basicVipData = await vipApi.getVipById(vipId);
        
        if (basicVipData) {
          // 基础信息映射
          this.vip = {
            id: basicVipData.id,
            hyid: basicVipData.hyid,
            name: basicVipData.name || '',
            age: basicVipData.age || '未知',
            points: basicVipData.points || 0,
            phone: basicVipData.phone || '未知',
            address: basicVipData.address || '未知',
            nonConsumptionDays: basicVipData.nonConsumptionDays || 0,
            r: basicVipData.r || 0,
            f: basicVipData.f || 0,
            m: basicVipData.m || 0
          };
          
          // 尝试获取详细信息
          if (basicVipData.hyid) {
            try {
              const detailResponse = await vipApi.getVipDetailByHyid(basicVipData.hyid);
              
              // 添加调试信息
              console.log('获取会员详情响应:', detailResponse);
              
              // 如果有详细信息，进一步更新vip对象
              if (detailResponse && detailResponse.STATUS === 0) {
                const message = detailResponse.MESSAGE;
                
                // 添加调试信息
                console.log('详情MESSAGE内容:', message);
                
                // 确保message是对象（有些API可能直接返回对象而不是数组）
                if (message) {
                  // 针对VIPINFO字段进行处理
                  if (message.VIPINFO && Array.isArray(message.VIPINFO)) {
                    message.VIPINFO.forEach(item => {
                      if (item.FIELD === "D_LASTBUY") {
                        this.vip.nonConsumptionDays = parseInt(item.VALUE) || 0;
                      } else if (item.FIELD === "N_ALLVALUE") {
                        this.vip.points = parseInt(item.VALUE) || 0;
                      }
                      // 可以添加更多的字段映射
                    });
                  }
                  
                  // 更新其他可能的字段
                  if (message.NAME) this.vip.name = message.NAME;
                  if (message.PHONE) this.vip.phone = message.PHONE;
                  if (message.ADDRESS) this.vip.address = message.ADDRESS;
                  
                  // 如果有RFM信息，也进行更新
                  if (message.RFM && Array.isArray(message.RFM) && message.RFM.length > 0) {
                    const rfm = message.RFM[0];
                    this.vip.r = parseInt(rfm.rvalue) || 0;
                    this.vip.f = parseInt(rfm.fvalue) || 0;
                    this.vip.m = parseInt(rfm.mvalue) || 0;
                  }
                }
              } else {
                console.warn('获取会员详情失败或返回错误:', detailResponse);
              }
            } catch (detailError) {
              console.error("获取会员详情失败:", detailError);
              // 不抛出异常，因为已经有基本信息
            }
          } else {
            console.warn("basicVipData 中 hyid 不存在，无法获取外部API会员详情。");
          }
        } else {
          Toast.fail("获取会员信息失败");
        }
      } catch (error) {
        console.error("获取会员信息失败:", error);
        Toast.fail("获取会员信息失败，请重试");
      } finally {
        // 关闭加载提示
        Toast.clear();
      }
    }
  },
  created() {
    this.fetchVipInfo(); // 调用新方法
  }
}
</script>

<style scoped>

</style>
