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
    <van-cell-group title="会员统计" style ="white-space: pre-line;">
      <van-cell center title="念念不忘数量"  :value="念念不忘数量"  />
      <van-cell center title="好久不见数量"  :value="好久不见数量"  />
    </van-cell-group>
    <van-cell-group title="会员列表" style ="white-space: pre-line;">
      <van-cell center :title="(index+1+'、')+item.name"  v-for="(item,index) in vips" :key="item.id" :label="'积分:'+item.积分+'\n'+item.未消费天数+'天未消费'+'\nRFM值 '+item.R+':'+item.F+':'+item.M" is-link @click="toVipInfo(item)" />
    </van-cell-group>
  </div>


</div>
</template>

<script>


export default {
  name: "AllVips",
  data(){
    return{
      options:{},
      show:false,
      cascaderValue:'',
      fieldValue:'',
      fieldNames: {
        text: 'name',
        value: 'id',
        children: 'users',
      },
      vips:[],
      念念不忘数量:'',
      好久不见数量:''
    }
  },
  created() {
    this.getCascader()
  },
  methods:{
    getCascader(){
      this.$http.post('getCascadeDept').then(res=>{
        this.options=res.data
        console.log(res.data)
      })
    },
    onFinish({ value,selectedOptions }){
      this.$http.post('getVipsByUserId',{UserId:value}).then(res=>{
        this.vips=res.data
        this.show = false;
        this.fieldValue = selectedOptions.map((option) => option.name).join('/');
      })
      this.$http.post('念念不忘数量',{UserId:value}).then(res=>{
        this.念念不忘数量=res.data
      })
      this.$http.post('好久不见数量',{UserId:value}).then(res=>{
        this.好久不见数量=res.data
      })

    },
    toVipInfo(vip){
      this.$router.push({  //核心语句
        path:'/vipinfo',   //跳转的路径
        query:{           //路由传参时push和query搭配使用 ，作用时传递参数
          id:vip.id ,
        }
      })
    },
    onClickLeft() {
      this.$router.back()
    },
  }
}
</script>

<style scoped>

</style>
