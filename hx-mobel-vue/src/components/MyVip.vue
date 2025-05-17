<template>
<div>
  <van-nav-bar
      :title="this.$route.query.type"
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"
  />
  
  <van-pull-refresh v-model="isRefreshing" @refresh="onRefresh">
    <van-list
      v-model="loading"
      :finished="finished"
      :error.sync="error"
      error-text="请求失败，点击重新加载"
      :offset="300"
      finished-text="没有更多了"
      @load="onLoad"
    >
      <van-cell-group style="white-space: pre-line;">
        <van-cell 
          v-for="(item, index) in list" 
          :key="item.id" 
          :title="(index+1+'、')+item.name" 
          :label="'积分:'+item.points+'\n'+item.nonConsumptionDays+'天未消费'+'\nRFM值 '+item.r+':'+item.f+':'+item.m" 
          size="large" 
          is-link 
          @click="toVipInfo(item)" 
        />
      </van-cell-group>
    </van-list>
  </van-pull-refresh>
</div>
</template>

<script>
import { vipApi } from '@/services/api.js'; // 导入新的api服务
import { Toast } from 'vant'; // 导入 Toast

export default {
  name: "MyVip",
  data() {
    return {
      list: [],
      loading: false,
      finished: false,
      error: false,
      isRefreshing: false,
      type: '',
      // 分页相关
      page: 1,
      pageSize: 10,
      totalItems: 0,
      // 排序相关
      sortBy: 'm',
      sortOrder: 'desc'
    };
  },
  created() {
    this.type = this.$route.query.type;
    // 列表数据将由onLoad加载
  },
  methods: {
    async onLoad() {
      const type = this.type;
      const userId = localStorage.getItem("UserId");

      if (!userId && (type === '念念不忘' || type === '好久不见')) {
        Toast.fail("用户ID获取失败"); 
        this.error = true;
        this.loading = false;
        return;
      }

      try {
        let response = null;
        
        if (type === '念念不忘'){
          response = await vipApi.getNeverForgetVips(
            userId, 
            this.page, 
            this.pageSize,
            this.sortBy,
            this.sortOrder
          );
        } else if (type === '好久不见'){
          response = await vipApi.getLongTimeNoSeeVips(
            userId, 
            this.page, 
            this.pageSize,
            this.sortBy,
            this.sortOrder
          );
        } else if (type === '欢聚一堂'){
          response = await vipApi.getAllVips();
        } else {
          Toast.fail("未知的VIP列表类型");
          this.list = [];
          this.loading = false;
          this.finished = true;
          return;
        }
        
        // 根据API返回结构处理数据
        let vipList = [];
        let total = 0;
        
        if (type === '念念不忘' || type === '好久不见') {
          // 处理分页返回的数据结构 {list: [...], total: 100}
          if (response && typeof response === 'object') {
            if (Array.isArray(response.list)) {
              vipList = response.list;
              total = response.total || 0;
            } else if (Array.isArray(response)) {
              vipList = response;
              total = response.length;
            }
          }
        } else {
          // 处理非分页API的返回
          if (Array.isArray(response)) {
            vipList = response;
            total = response.length;
          } else if (response && response.list && Array.isArray(response.list)) {
            vipList = response.list;
            total = response.total || vipList.length;
          }
        }
        
        // 更新总数量
        this.totalItems = total;
        
        // 确保所有必要字段都有合法值
        const formattedVips = vipList.map(vip => ({
          ...vip,
          points: vip.points !== undefined ? vip.points : 0,
          nonConsumptionDays: vip.nonConsumptionDays !== undefined ? vip.nonConsumptionDays : 0,
          r: vip.r !== undefined ? vip.r : 0,
          f: vip.f !== undefined ? vip.f : 0,
          m: vip.m !== undefined ? vip.m : 0
        }));
        
        // 添加新加载的数据到列表中
        this.list = [...this.list, ...formattedVips];
        
        // 更新加载状态
        this.loading = false;
        
        // 判断是否已加载完全部数据
        if (type === '念念不忘' || type === '好久不见') {
          // 使用分页的API
          this.finished = this.list.length >= this.totalItems;
          if (!this.finished) {
            this.page++; // 增加页码，准备加载下一页
          }
        } else {
          // 非分页API一次性加载全部数据
          this.finished = true;
        }
        
      } catch (error) {
        console.error(`获取VIP列表 (${type}) 失败:`, error);
        Toast.fail(`获取 "${type}" 列表失败`);
        this.error = true;
        this.loading = false;
      }
    },
    
    async onRefresh() {
      // 下拉刷新，重置数据状态
      this.error = false;
      this.finished = false;
      this.page = 1;
      this.list = [];
      this.loading = true;
      
      await this.onLoad();
      this.isRefreshing = false;
      Toast.success('刷新成功');
    },
    
    onClickLeft() {
      this.$router.back();
    },

    async toVipInfo(vip){
      try {
        // 显示加载提示
        Toast.loading({
          message: '加载会员详情...',
          forbidClick: true,
        });
        
        // 调用API获取详细信息，使用 vip.hyid
        await vipApi.getVipDetailByHyid(vip.hyid);
        
        // 关闭加载提示
        Toast.clear();
        
        // 如果获取详情成功，跳转到会员详情页面
        this.$router.push({
          path:'/vipinfo',
          query:{
            id: vip.id,
          }
        });
      } catch (error) {
        // 关闭加载提示
        Toast.clear();
        
        console.error('获取会员详情失败:', error);
        Toast.fail('获取会员详情失败，请重试');
        
        // 即使获取详情失败，仍然跳转到会员详情页面，显示基本信息
        this.$router.push({
          path:'/vipinfo',
          query:{
            id: vip.id,
          }
        });
      }
    }
  },
}
</script>

<style scoped>
/* 确保列表有足够的高度占满剩余空间 */
.van-list {
  min-height: calc(100vh - 46px);
}
</style>
