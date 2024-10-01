import Vue from 'vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from './App.vue'
import store from '../vuex/store'
import router from '../route/router.js'
Vue.use(ElementUI);
Vue.config.productionTip = false
import axios from "axios";

// axios.defaults.baseURL = 'http://101.42.104.145:8090'
axios.defaults.baseURL = 'http://127.0.0.1:8090'
Vue.prototype.$http = axios
router.beforeEach((to, from, next) => {


  if (to.meta.isLogin) {  // 判断该路由是否需要登录权限
    if (window.localStorage.getItem('Flag')) {  // 通过vuex state获取当前的token是否存在
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
new Vue({
  store,
  router,
  render: h => h(App),
}).$mount('#app')




