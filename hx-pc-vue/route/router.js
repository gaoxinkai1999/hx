import Vue from 'vue';
import VueRouter from 'vue-router';
import Index from "@/components/Index.vue";
import Login from "@/components/Login.vue";
//第三方库需要use一下才能用
Vue.use(VueRouter)
//引入组件


//定义routes路由的集合，数组类型
const routes=[

//单个路由均为对象类型，path代表的是路径，component代表组件
    {
        // 登录
        path: '/login',
        component: Login,
        meta: {
            isLogin: false
        }
    },
    {
        // 登录
        path: '/',
        component: Login,
        meta: {
            isLogin: false
        }
    },
    {

        path: '/index',
        component: Index,
        meta: {
            isLogin: true
        }
    }
//     {path:'/page1',component:page1},
//     {path:"/page2",component:page2},
//     //可以配置重定向
//     // {path:'',redirect:"page1"}
//     //或者重新写个路径为空的路由
//     {path:"",component:page1}
]
//实例化VueRouter并将routes添加进去
const router=new VueRouter({
    mode: 'history',
//ES6简写，等于routes：routes
    routes
});
//抛出这个这个实例对象方便外部读取以及访问
export default router
