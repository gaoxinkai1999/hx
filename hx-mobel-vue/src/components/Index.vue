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
    getNum(){
      this.$http.post('念念不忘数量',{UserId:localStorage.getItem('UserId')}).then(res=>{
        this.念念不忘数量=res.data
      })
      this.$http.post('好久不见数量',{UserId:localStorage.getItem('UserId')}).then(res=>{
        this.好久不见数量=res.data
      })
      this.$http.post('欢聚一堂数量').then(res=>{
        this.欢聚一堂数量=res.data
      })
    },
    onClickRight() {
      this.$router.push('/addvip')
    },
  }

}
</script>

<style scoped>

</style>
