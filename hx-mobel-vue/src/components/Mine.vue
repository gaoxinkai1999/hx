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
import { userApi } from '@/services/api.js'; // 导入新的api服务

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: "Mine",
  data(){
    return{
        user:{
          name: '', // 初始化，避免模板渲染时出错
          所属部门: { name: '' } // 初始化嵌套对象
        }
    }
  },
  methods:{
    async getUserInfo(){ // 标记为 async
      const userId = localStorage.getItem("UserId");
      if (!userId) {
        console.error("UserId not found in localStorage for Mine component");
        // Toast.fail("无法获取用户ID");
        return;
      }
      try {
        const userData = await userApi.getUserInfo(userId);
        if (userData) {
          this.user = { // 确保user对象结构与模板期望一致
            name: userData.name,
            所属部门: { // UserVO 中的 deptName 和 deptType 需要映射
              name: userData.deptName || 'N/A' // 如果没有部门名称，显示 N/A
            }
            // 如果 UserVO 还有其他字段需要在模板中使用，也应在此处映射
          };
        } else {
          // Toast.fail("获取用户信息失败");
          this.user = { name: '加载失败', 所属部门: { name: '' } };
        }
      } catch (error) {
        console.error("获取用户信息失败 (Mine.vue):", error);
        // Toast.fail("请求用户信息接口失败");
        this.user = { name: '请求失败', 所属部门: { name: '' } };
      }
    }
  },
  created() {
    this.getUserInfo()
  }
}
</script>

<style scoped>

</style>
