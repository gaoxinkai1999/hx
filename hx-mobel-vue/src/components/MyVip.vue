<template>
<div>
  <van-nav-bar
      title="我的会员"
      left-text="返回"
      right-text="添加会员"
      left-arrow
      @click-left="onClickLeft"
      @click-right="onClickRight"
  />
<!--  <van-list-->
<!--      v-model="loading"-->
<!--      :finished="finished"-->
<!--      finished-text="没有更多了"-->
<!--      @load="onLoad"-->
<!--  >-->
<!--    <van-cell v-for="item in list" :key="item" :title="item" size="large" />-->
<!--  </van-list>-->
  <van-cell-group  style ="white-space: pre-line;">
    <van-cell v-for="item in list" :key="item.id" :title="item.name" :label="'积分:'+item.积分+'\n'+item.未消费天数+'天未消费'" size="large" is-link @click="toVipInfo(item)" />
  </van-cell-group>

</div>
</template>

<script>


export default {
  name: "MyVip",
  data() {
    return {
      list: [],
      loading: false,
      finished: false,
    };
  },
  created() {

      this.getVips(this.$route.query.start,this.$route.query.end)
  },
  methods: {
     getVips(start,end){
       this.$http.post('getVipByUserID',{id:localStorage.getItem("UserId"),start:start,end:end}).then(res=>{
        this.list=res.data
      })
    },
    onLoad() {
      // 异步更新数据
      // setTimeout 仅做示例，真实场景中一般为 ajax 请求
      setTimeout(() => {
        for (let i = 0; i < 10; i++) {
          this.list.push(this.list.length + 1);
        }

        // 加载状态结束
        this.loading = false;

        // 数据全部加载完成
        if (this.list.length >= 40) {
          this.finished = true;
        }
      }, 1000);
    },
    onClickLeft() {
      this.$router.back()
    },
    onClickRight() {
      this.$router.push('/addvip')
    },
    toVipInfo(vip){
      this.$router.push({  //核心语句
        path:'/vipinfo',   //跳转的路径
        query:{           //路由传参时push和query搭配使用 ，作用时传递参数
          id:vip.id ,
        }
      })
    }
  },
}
</script>

<style scoped>

</style>
