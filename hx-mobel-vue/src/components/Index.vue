<template>
<div>

  <div v-if="active==0" >
    <van-nav-bar
        title="会员维护列表"
        right-text="添加会员"
        @click-right="onClickRight"></van-nav-bar>
    <van-cell-group inset>
      <van-cell center title="念念不忘"  is-link @click="toMyVip('念念不忘')"  :value="念念不忘数量+'位'" size="large" />
      <van-cell center title="好久不见"  is-link @click="toMyVip('好久不见')"   :value="好久不见数量+'位'" size="large"/>
      <van-cell center title="欢聚一堂"  is-link to="AllVips" :value="欢聚一堂数量+'位'"  size="large" />
    </van-cell-group>

  </div>

  <div v-if="active==1" >
    <Mine></Mine>
  </div>
  <div style="padding-bottom:100px">

  </div>

  <van-tabbar v-model="active">
    <van-tabbar-item icon="search">功能</van-tabbar-item>
    <van-tabbar-item icon="friends-o">我的</van-tabbar-item>
  </van-tabbar>
</div>
</template>

<script>
/* eslint-disable vue/multi-word-component-names */
import Mine from "@/components/Mine.vue";
import { vipApi } from '@/services/api.js'; // 导入新的api服务

export default {
  name: "Index",
  components: {Mine},
  data(){
    return{
      active:0,
      念念不忘数量:0,
      好久不见数量:0,
      欢聚一堂数量:0,
    }
  },
  created() {
    this.getNum()
  },
  methods:{
    toMyVip(type){
      this.$router.push({
        path: '/myvip',
        query: {
         type:type
        }
      })
    },
    async getNum(){ // 标记为 async
      const userId = localStorage.getItem('UserId');
      if (!userId) {
        console.error("UserId not found in localStorage");
        // 可以选择性地显示错误提示给用户
        // Toast.fail("无法获取用户信息，请重新登录");
        return;
      }
      try {
        const neverForgetCount = await vipApi.getNeverForgetVipsCount(userId);
        this.念念不忘数量 = neverForgetCount;

        const longTimeNoSeeCount = await vipApi.getLongTimeNoSeeVipsCount(userId);
        this.好久不见数量 = longTimeNoSeeCount;

        const allVipsCount = await vipApi.getAllVipsCount();
        this.欢聚一堂数量 = allVipsCount;

      } catch (error) {
        console.error("获取数量统计失败:", error);
        // Toast.fail("获取统计数据失败");
      }
    },
    onClickRight() {
      this.$router.push('/addvip')
    },
  }

}
</script>

<style scoped>

</style>
