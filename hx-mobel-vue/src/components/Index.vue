<template>
<div>
  <div v-if="active==0" >
    <van-cell-group inset title="会员维护列表" >
      <van-cell center title="念念不忘"  is-link @click="toMyVip(0,180)" label="未消费天数0-180天" size="large" />
      <van-cell center title="好久未见"  is-link @click="toMyVip(180,360)"  label="未消费天数180-360天" size="large"/>
      <van-cell center  title="反观自省"  is-link @click="toMyVip(360,100000)" label="未消费天数360天以上" size="large"/>
      <van-cell center title="欢聚一堂"  is-link @click="toMyVip(0,100000)" label="全部会员" size="large" />
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
      active:0
    }
  },
  methods:{
    toMyVip(start,end){
      this.$router.push({
        path: '/myvip',
        query: {
         start: start,
          end:end
        }
      })
    }
  }

}
</script>

<style scoped>

</style>
