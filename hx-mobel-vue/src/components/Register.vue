<template>
<div>
  <van-nav-bar
      title="注册"
      left-text="返回"
      left-arrow
      @click-left="onClickLeft"

  />
  <van-form @submit="onSubmit" style="margin-top:200px">
    <van-field
        v-model="data.name"
        name="姓名"
        label="姓名"
        placeholder="姓名"
        :rules=rules
    />
    <van-field
        v-model="data.password"
        type="password"
        name="密码"
        label="密码"
        placeholder="密码"
        :rules=rules
    />
    <van-field
        readonly
        clickable
        name="picker"
        :value="data.所属部门.name"
        label="所属部门"
        placeholder="点击选择部门"
        @click="showPicker = true"
        :rules=rules
    />
    <van-popup v-model="showPicker" position="bottom">
      <van-picker
          show-toolbar
          :columns="depts"
          value-key="name"
          @confirm="onConfirm"
          @cancel="showPicker = false"
      />
    </van-popup>
    <div style="margin: 16px;">
      <van-button round block type="info" native-type="submit">注册</van-button>
    </div>
  </van-form>
</div>
</template>

<script>
import {Dialog, Toast} from "vant";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Register",
  data(){
    return{
        data:{
          name:'',
          password:'',
          所属部门:{}
        },
      rules:[{ required: true, message: '不能为空' }],
      showPicker:false,
      depts:[]
    }
  },
  created() {
    this.getDepts()
  },
  methods:{
    onSubmit(){
      Dialog.confirm({
        title: '确认注册?',
      })
          .then(() => {
            // on confirm
            this.$http.post('addUser',this.data).then(res=>{
              if (res.data==true){
                Toast.success('注册成功,请返回登录页面');

              }else {
                Toast.fail('该用户名已使用');
              }

            })
          })
          .catch(() => {
            // on cancel
          });

    },
    getDepts(){
      this.$http.post('getDepts').then(res=>{
        this.depts=res.data
      })
    },
    onConfirm(value) {
      this.data.所属部门=value
      this.showPicker = false;
    },
    onClickLeft(){
      this.$router.back()
    }
  }
}
</script>

<style scoped>

</style>
