<template>
<div>
  <div v-if="active==0">
    首页
  </div>
  <div v-if="active==1" >
    <van-grid clickable :column-num="1" >
      <van-grid-item icon="home-o" text="我的会员" to="/myvip" />

    </van-grid>
  </div>

  <div v-if="active==2" >
我的
  </div>
  <div style="padding-bottom:100px">

  </div>

  <van-tabbar v-model="active">
    <van-tabbar-item icon="home-o">首页</van-tabbar-item>
    <van-tabbar-item icon="search">功能</van-tabbar-item>
    <van-tabbar-item icon="friends-o">我的</van-tabbar-item>
  </van-tabbar>
</div>
</template>

<script>
/* eslint-disable vue/multi-word-component-names */
export default {
  name: "Index",
  data(){
    return{
      active:0
    }
  }
}
</script>

<style scoped>

</style>
