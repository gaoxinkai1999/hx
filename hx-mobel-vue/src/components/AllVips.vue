<template>
<div>
  <van-nav-bar
    title="欢聚一堂"
    left-text="返回"
    left-arrow
    @click-left="onClickLeft"
  />
  <van-field
      v-model="fieldValue"
      is-link
      readonly
      label="员工"
      placeholder="请选择员工"
      @click="show = true"
  />
  <van-popup v-model="show" round position="bottom">
    <van-cascader
        v-model="cascaderValue"
        :field-names="fieldNames"
        title="请选择员工"
        :options="options"
        @close="show = false"
        @finish="onFinish"
    />
  </van-popup>
  <div v-if="fieldValue!=''">
    <van-cell-group title="会员统计">
      <van-cell center title="念念不忘数量" :value="neverForgetCount" />
      <van-cell center title="好久不见数量" :value="longTimeNoSeeCount" />
    </van-cell-group>
    
    <van-cell-group title="会员列表">
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
          <van-cell 
            v-for="(item, index) in vips" 
            :key="item.id" 
            :title="(index+1+'、')+item.name" 
            :label="'积分:'+item.points+'\n'+item.nonConsumptionDays+'天未消费'+'\nRFM值 '+item.r+':'+item.f+':'+item.m" 
            size="large"
            is-link 
            @click="toVipInfo(item)" 
          />
        </van-list>
      </van-pull-refresh>
    </van-cell-group>
  </div>
</div>
</template>

<script>
import { Toast } from 'vant';
import { deptApi, vipApi } from '@/services/api.js'; // 导入新的api服务

export default {
  name: "AllVips",
  data(){
    return{
      options: [],
      show: false,
      cascaderValue: '',
      fieldValue: '',
      fieldNames: {
        text: 'name',
        value: 'id',
        children: 'users',
      },
      vips: [],
      neverForgetCount: 0,
      longTimeNoSeeCount: 0,
      selectedUserId: null,
      // List组件相关
      loading: false,
      finished: true,
      error: false,
      isRefreshing: false,
      // 分页相关
      currentPage: 1,
      pageSize: 10,
      totalItems: 0,
      // 排序相关
      sortBy: 'm',
      sortOrder: 'desc'
    }
  },
  created() {
    this.getCascader()
  },
  methods:{
    async getCascader(){ // 标记为 async
      try {
        const data = await deptApi.getCascadeDept();
        this.options = data;
      } catch (error) {
        console.error("获取级联部门失败:", error);
        Toast.fail("获取部门数据失败");
      }
    },
    async onFinish({ value, selectedOptions }){ // 标记为 async
      this.selectedUserId = value;
      this.show = false;
      this.fieldValue = selectedOptions.map((option) => option.name).join('/');
      
      // 重置列表状态
      this.vips = [];
      this.finished = false;
      this.error = false;
      this.currentPage = 1;
      this.loading = false;
      
      try {
        // 加载会员统计信息
        const [neverForgetCount, longTimeNoSeeCount] = await Promise.all([
          vipApi.getNeverForgetVipsCount(value),
          vipApi.getLongTimeNoSeeVipsCount(value)
        ]);
        
        this.neverForgetCount = neverForgetCount;
        this.longTimeNoSeeCount = longTimeNoSeeCount;
        
        // 手动启动加载过程
        this.loading = true;
      } catch (error) {
        console.error("获取会员统计数据失败:", error);
        Toast.fail("获取会员统计失败");
        this.error = true;
        this.loading = false;
      }
    },
    async onLoad() {
      // 打印调试信息
      console.log('onLoad被触发', { 
        selectedUserId: this.selectedUserId, 
        loading: this.loading,
        finished: this.finished,
        currentPage: this.currentPage
      });
      
      if (!this.selectedUserId) {
        console.log('没有选择用户，终止加载');
        this.loading = false;
        this.finished = true;
        return;
      }
      
      try {
        console.log(`开始加载第${this.currentPage}页数据`);
        
        // 尝试使用常规的getVipsByUserId方法
        // 如果后端未实现getVipsByUserIdPaged接口，可能需要退回到非分页方式
        let response;
        try {
          // 尝试使用分页API
          response = await vipApi.getVipsByUserIdPaged(
            this.selectedUserId,
            this.currentPage,
            this.pageSize,
            this.sortBy,
            this.sortOrder
          );
          console.log('分页API响应:', response);
        } catch (apiError) {
          console.warn('分页API调用失败，尝试使用非分页API:', apiError);
          // 如果分页API失败，回退到非分页API
          if (this.currentPage === 1) { // 只在第一页时尝试
            response = await vipApi.getVipsByUserId(this.selectedUserId);
            console.log('非分页API响应:', response);
          } else {
            // 第二页及以后，如果分页API不可用，就标记为加载完成
            this.finished = true;
            this.loading = false;
            return;
          }
        }
        
        // 处理返回的数据
        let newVips = [];
        let total = 0;
        
        if (response && typeof response === 'object') {
          if (Array.isArray(response.list)) {
            // 处理分页返回的数据结构 {list: [...], total: 100}
            newVips = response.list;
            total = response.total || 0;
          } else if (Array.isArray(response)) {
            // 处理直接返回数组的情况
            newVips = response;
            total = response.length;
          }
        }
        
        console.log(`获取到${newVips.length}条数据，总计${total}条`);
        
        // 检查是否有数据返回
        if (newVips.length === 0 && this.currentPage === 1) {
          Toast('没有会员数据');
        }
        
        // 更新总数量
        this.totalItems = total;
        
        // 标准化字段
        newVips = newVips.map(vip => ({
          ...vip,
          points: vip.points !== undefined ? vip.points : 0,
          nonConsumptionDays: vip.nonConsumptionDays !== undefined ? vip.nonConsumptionDays : 0,
          r: vip.r !== undefined ? vip.r : 0,
          f: vip.f !== undefined ? vip.f : 0,
          m: vip.m !== undefined ? vip.m : 0
        }));
        
        // 添加新加载的数据到列表中
        this.vips = [...this.vips, ...newVips];
        
        // 更新加载状态
        this.loading = false;
        
        // 判断是否已加载完全部数据
        const reachedEnd = newVips.length < this.pageSize || this.vips.length >= this.totalItems;
        this.finished = reachedEnd;
        console.log(`加载状态: finished=${this.finished}, 当前数据量=${this.vips.length}, 总数据量=${this.totalItems}`);
        
        // 如果数据未加载完，增加页码
        if (!this.finished) {
          this.currentPage++;
        }
        
      } catch (error) {
        console.error("获取会员列表失败:", error);
        Toast.fail("获取会员列表失败");
        this.error = true;
        this.loading = false;
      }
    },
    async onRefresh() {
      // 下拉刷新时，重置状态并重新加载数据
      if (!this.selectedUserId) {
        this.isRefreshing = false;
        return;
      }
      
      this.error = false;
      this.finished = false;
      this.vips = [];
      this.currentPage = 1;
      
      try {
        // 重新加载会员统计和列表
        const [neverForgetCount, longTimeNoSeeCount] = await Promise.all([
          vipApi.getNeverForgetVipsCount(this.selectedUserId),
          vipApi.getLongTimeNoSeeVipsCount(this.selectedUserId)
        ]);
        
        this.neverForgetCount = neverForgetCount;
        this.longTimeNoSeeCount = longTimeNoSeeCount;
        
        // 触发列表加载
        this.loading = true;
        await this.onLoad();
        
        Toast.success('刷新成功');
      } catch (error) {
        console.error("刷新数据失败:", error);
        Toast.fail("刷新数据失败");
        this.error = true;
      } finally {
        this.isRefreshing = false;
      }
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
    },
    onClickLeft() {
      this.$router.back()
    },
  }
}
</script>

<style scoped>
/* 确保列表有足够的高度占满剩余空间 */
.van-list {
  min-height: calc(100vh - 250px);
}
</style>
