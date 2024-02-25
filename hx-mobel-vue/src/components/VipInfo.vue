<template>
<div>
  <van-nav-bar
      title="会员详情"
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"

  />
  <van-cell-group >
    <van-cell center title="姓名" :value="vip.name"  size="large" />
    <van-cell center title="年龄" :value="vip.age"  size="large" />
    <van-cell center title="积分" :value="vip.积分"  size="large" />
    <van-cell center title="手机号" :value="vip.phone"  size="large" />
    <van-cell center title="地址" :value="vip.adress"  size="large" />
    <van-cell center title="未消费天数" :value="vip.未消费天数+'天'"  size="large" />
  </van-cell-group>
</div>
</template>

<script>


export default {
  name: "VipInfo",
  data(){
    return{
      vip:{}
    }
  },
  methods:{
    onClickLeft() {
      this.$router.back()
    },
  },
  created() {
    this.$http.post('getVipById',{id:this.$route.query.id}).then(res=>{
      this.vip=res.data
    })

  }
}
</script>

<style scoped>

</style>
