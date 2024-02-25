<template>
  <div class="login">
    <el-form ref="loginForm" :model="loginForm"  class="login-form">
      <h3 class="title">和信后台管理系统</h3>
      <el-form-item prop="username">
        <el-input
            v-model="loginForm.username"
            type="text"
            auto-complete="off"
            placeholder="账号"
        >

        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
            v-model="loginForm.password"
            type="password"
            auto-complete="off"
            placeholder="密码"
        >
        </el-input>
      </el-form-item>

<!--      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>-->
      <el-form-item style="width:100%;">
        <el-button
            :loading="loading"
            size="medium"
            type="primary"
            style="width:100%;"
            @click="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
<!--        <div style="float: right;" v-if="register">-->
<!--          <router-link class="link-type" :to="'/register'">立即注册</router-link>-->
<!--        </div>-->
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'Login',
  props: {
    msg: String
  },
  data(){
    return{
      loginForm:{
        username:'admin',
        password:'admin',
        // rememberMe:false
      },
      loading:false
    }
  },
  methods:{
    handleLogin(){
      if (this.loginForm.username=='admin'&&this.loginForm.password=='admin'){
        //设置Vuex登录标志为true，默认userLogin为false
        this.$store.dispatch("userLogin", true);
        //Vuex在用户刷新的时候userLogin会回到默认值false，所以我们需要用到HTML5储存
        //我们设置一个名为Flag，值为isLogin的字段，作用是如果Flag有值且为isLogin的时候，证明用户已经登录了。
        localStorage.setItem("Flag", "isLogin");
        //iViewUi的友好提示
        //登录成功后跳转到指定页面
        this.$router.push("/index");
      }



    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  /*background-image: url("../assets/images/login-background.jpg");*/
  background-size: cover;
}
.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
}
</style>
