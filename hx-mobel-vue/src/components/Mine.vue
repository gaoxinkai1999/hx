<template>
  <div>
    <van-nav-bar
        title="我的信息"

    />
    <van-cell-group inset>
      <van-cell title="姓名" :value="user.name" size="large"/>
      <van-cell title="所属部门" :value="user.所属部门.name" size="large"/>
    </van-cell-group>
  </div>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Mine",
  data(){
    return{
        user:{}
    }
  },
  methods:{
    getUserInfo(){
      this.$http.post('getUserInfo',{id:localStorage.getItem("UserId")}).then(res=>{
        this.user=res.data
      })
    }
  },
  created() {
    this.getUserInfo()
  }
}
</script>

<style scoped>

</style>
