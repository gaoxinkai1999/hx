<template>
<div>
  <van-nav-bar
      :title="this.$route.query.type"
      left-text="返回"

      left-arrow
      @click-left="onClickLeft"
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
    <van-cell v-for="(item,index) in list" :key="item.id" :title="(index+1+'、')+item.name" :label="'积分:'+item.积分+'\n'+item.未消费天数+'天未消费'+'\nRFM值 '+item.R+':'+item.F+':'+item.M" size="large" is-link @click="toVipInfo(item)" />
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
     getVips(){
       if (this.$route.query.type=='念念不忘'){
         this.$http.post('念念不忘',{UserId:localStorage.getItem("UserId")}).then(res=>{
           this.list=res.data
         })
       }else if (this.$route.query.type=='好久不见'){
         this.$http.post('好久不见',{UserId:localStorage.getItem("UserId")}).then(res=>{
           this.list=res.data
         })

       }else if (this.$route.query.type=='欢聚一堂'){
         this.$http.post('getAllVips').then(res=>{
           this.list=res.data
         })
       }

    },
    onClickLeft() {
      this.$router.back()
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
