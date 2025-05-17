import Vue from 'vue'

import App from './App.vue'
import store from '../vuex/store'
import router from '../route/router.js'
import Vant from 'vant';
import 'vant/lib/index.css';

Vue.use(Vant);
Vue.config.productionTip = false
import axios from "axios";

// 设置API基础URL - 使用相对路径
axios.defaults.baseURL = '';

// 配置请求拦截器
axios.interceptors.request.use(config => {
  // 如果有token，添加到请求头
  const token = window.localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, error => {
  return Promise.reject(error);
});

// 配置响应拦截器
axios.interceptors.response.use(response => {
  // 直接返回响应数据
  return response;
}, error => {
  // 处理错误响应
  if (error.response && error.response.status === 401) {
    // 未授权，跳转到登录页
    router.push('/login');
  }
  return Promise.reject(error);
});

// 将axios挂载到Vue原型上
Vue.prototype.$http = axios

// 路由守卫，检查登录状态
router.beforeEach((to, from, next) => {
  if (to.meta.isLogin) {  // 判断该路由是否需要登录权限
    if (window.localStorage.getItem('Flag')) {  // 通过localStorage获取当前的登录状态
      next();
    }
    else {
      next({
        path: '/login',
      })
    }
  }
  else {
    next();
  }
});

// 挂载Vue实例
new Vue({
  store,
  router,
  render: h => h(App),
}).$mount('#app')




