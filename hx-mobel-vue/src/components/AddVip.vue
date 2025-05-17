<template>
  <div>
    <van-nav-bar
        title="新增会员"
        left-text="返回"
        left-arrow
        @click-left="onClickLeft"
    />

    <form action="/">
      <van-search
          v-model="phone"
          placeholder="请输入会员手机号"
          @search="onSearch"
      />
    </form>
    <van-form @submit="onSubmit" v-if="form_show">
      <van-field
          v-model="VipForm.name"
          name="会员名称"
          label="会员名称"
          placeholder="会员名称"
          :rules=rules
          disabled
      />
      <van-field
          readonly
          clickable
          name="picker"
          :value="VipForm.age"
          label="年龄"
          placeholder="点击选择年龄"
          @click="age.showPicker = true"
          :rules=rules
      />
      <van-popup v-model="age.showPicker" position="bottom">
        <van-picker
            show-toolbar
            :columns="age.columns"
            @confirm="age_onConfirm"
            @cancel="age.showPicker = false"
        />
      </van-popup>
      <van-field
          v-model="VipForm.积分"
          name="积分"
          label="积分"
          placeholder="积分"
          :rules=rules
          disabled
      />

      <van-field
          v-model="VipForm.phone"
          type="tel"
          name="手机号"
          label="手机号"
          placeholder="手机号"
          :rules=rules
          disabled
      />
      <van-field
          v-model="VipForm.未消费天数"
          type="digit"
          name="未消费天数"
          label="未消费天数"
          placeholder="未消费天数"
          :rules=rules
          disabled
      />

      <van-field
          v-model="VipForm.adress"
          name="地址"
          label="地址"
          placeholder="地址"
          :rules=rules
      />

      <div style="margin: 16px;">
        <van-button round block type="info" native-type="submit">提交</van-button>
      </div>
    </van-form>
    <van-popup v-model="show" style="white-space: pre-wrap;">
      <h3>{{this.popup_title}}</h3>
        <van-cell center v-for="item in VipList" :key="item.HYID" :title="item.C_NAME" :value="item.INFO" style="width: 100%" is-link @click="onChoose(item)"></van-cell>
    </van-popup>
  </div>
</template>

<script>
import {Dialog, Toast} from "vant";
import { vipApi } from '@/services/api.js'; // 导入新的api服务

export default {
  name: "AddVip",
  data() {
    return {
      VipForm: {
        hyid:'',
        name: '',
        age: '',
        积分: '',
        phone: '',
        未消费天数: '',
        adress: '',
        维护人:{
          id:''
        }
      },
      phone:'',
      popup_title:'选择是哪一个会员',
      age: {
        columns: ['60后', '70后', '80后', '90后','00后','未知'],
        showPicker: false,
      },
      rules:[{ required: true, message: '不能为空' }],
      show:false,
      form_show:false,
      VipList:[]
    }
  },
  methods: {
    onClickLeft() {
      this.$router.back()
    },
    async onSubmit() { // 标记为 async
      Dialog.confirm({
        title: '确认提交',
      })
          .then(async () => { // 内部的 then 也标记为 async
            try {
              const vipPayload = {
                hyid: parseInt(this.VipForm.hyid, 10), // 确保是数字
                name: this.VipForm.name,
                age: this.VipForm.age,
                points: parseInt(this.VipForm.积分, 10) || 0, // 添加默认值0
                phone: this.VipForm.phone,
                nonConsumptionDays: parseInt(this.VipForm.未消费天数, 10) || 0, // 添加默认值0
                address: this.VipForm.adress,
                maintainerId: parseInt(this.VipForm.维护人.id, 10),
                // 添加默认的RFM值
                r: 3,
                f: 3,
                m: 3
              };
              
              // 验证必要字段
              if (isNaN(vipPayload.hyid)) {
                Toast.fail('会员ID必须是有效数字');
                return;
              }
              
              if (isNaN(vipPayload.maintainerId)) {
                Toast.fail('维护人ID必须是有效数字');
                return;
              }

              const success = await vipApi.addVip(vipPayload);
              if (success) { // 后端返回 boolean
                this.form_show = false;
                Toast.success('添加成功');
                this.phone = ''; // 清空搜索框
                this.VipList = [];
                // 可以选择清空VipForm或部分清空
                Object.keys(this.VipForm).forEach(key => {
                  if (key !== '维护人') {
                    this.VipForm[key] = '';
                  }
                });
              } else {
                // this.form_show = false; // 失败时表单可能需要保留
                Toast.fail('添加失败，会员可能已被添加或不符合校验规则');
              }
            } catch (error) {
              console.error("添加VIP失败:", error);
              Toast.fail('添加VIP请求失败，请检查网络或联系管理员');
            }
          })
          .catch(() => {
            // on cancel - 用户点击了取消
            Toast('已取消提交');
          });
    },
    age_onConfirm(value) {
      this.VipForm.age = value;
      this.age.showPicker = false;
    },
    async onSearch(){ // 标记为 async
      if (!this.phone || this.phone.trim() === '') {
        Toast('请输入手机号');
        return;
      }
      try {
        const resultJson = await vipApi.findVipsByPhoneNumber(this.phone.trim());
        // 外部API成功时 STATUS 为 0，且 MESSAGE 应为数组
        if (resultJson && resultJson.STATUS === 0 && resultJson.MESSAGE && Array.isArray(resultJson.MESSAGE)) {
          this.VipList = resultJson.MESSAGE;
          if (this.VipList.length === 0) {
            this.popup_title = '未找到相关会员, 请检查手机号是否正确';
            Toast.fail(this.popup_title); // 对于未找到的情况也明确提示
          } else {
            this.popup_title = '请选择要添加的会员';
          }
        } else {
          // 处理查找失败或外部API返回错误状态的情况
          this.VipList = [];
          let errorMsg = '查找会员失败，请稍后再试'; // 默认错误消息
          if (resultJson && resultJson.MESSAGE) {
            if (typeof resultJson.MESSAGE === 'string') {
              errorMsg = resultJson.MESSAGE;
            } else if (Array.isArray(resultJson.MESSAGE) && resultJson.MESSAGE.length > 0) {
              const firstMessageItem = resultJson.MESSAGE[0];
              if (typeof firstMessageItem === 'object' && firstMessageItem !== null) {
                if (firstMessageItem.INFO) {
                  errorMsg = firstMessageItem.INFO;
                } else if (firstMessageItem.ERROR_INFO) {
                  errorMsg = firstMessageItem.ERROR_INFO;
                } else {
                  // errorMsg = JSON.stringify(firstMessageItem); // 可选
                }
              }
            }
          } else if (resultJson && resultJson.STATUS !== undefined) {
             errorMsg = `查找失败，状态码: ${resultJson.STATUS}`;
          }
          this.popup_title = errorMsg;
          Toast.fail(errorMsg); // 对于错误情况也明确提示
        }
        this.show = true; // 无论成功失败，都显示选择/提示弹窗
      } catch (error) {
        console.error("查找VIP失败:", error);
        Toast.fail('查找VIP请求失败，请检查网络或联系管理员');
        this.VipList = [];
        this.popup_title = '查找请求异常，请稍后再试';
        this.show = true;
      }
    },
    async onChoose(Vip){ // Vip参数是从搜索结果选择的会员
      try {
        // 保存基本信息
        this.VipForm.hyid = Vip.HYID;
        this.VipForm.name = Vip.C_NAME;
        this.VipForm.phone = Vip.C_MOBILE;
        
        // 显示加载状态
        Toast.loading({
          message: '加载会员详情...',
          forbidClick: true,
        });
        
        // 调用API获取会员详情
        const detailResult = await vipApi.getVipDetailByHyid(Vip.HYID);
        Toast.clear();
        
        // 处理详情信息
        if (detailResult && detailResult.STATUS === 0 && detailResult.MESSAGE) {
          // MESSAGE直接是对象，不是数组
          const vipDetail = detailResult.MESSAGE;
          
          // 解析VIPINFO数组以获取具体字段值
          if (vipDetail.VIPINFO && Array.isArray(vipDetail.VIPINFO)) {
            for (const info of vipDetail.VIPINFO) {
              if (info.FIELD === 'D_LASTBUY') {
                this.VipForm.未消费天数 = info.VALUE || '';
              } else if (info.FIELD === 'N_ALLVALUE') {
                this.VipForm.积分 = info.VALUE || '';
              }
            }
          }
        } else {
          // 如果详情获取失败，则使用搜索结果中的基本信息
          this.VipForm.未消费天数 = Vip.D_LASTBUY !== undefined ? Vip.D_LASTBUY : (Vip.NON_CONSUMPTION_DAYS || '');
          this.VipForm.积分 = Vip.N_ALLVALUE !== undefined ? Vip.N_ALLVALUE : (Vip.POINTS || '');
          console.warn("获取会员详情失败，使用搜索结果中的基本信息");
        }
        
        // 清空需要用户填写的字段
        this.VipForm.age = '';
        this.VipForm.adress = '';
        
        this.show = false; // 关闭选择弹窗
        this.form_show = true; // 显示会员信息表单
      } catch (error) {
        Toast.clear();
        console.error("获取会员详情失败:", error);
        Toast.fail('获取会员详情失败');
      }
    }
  },
  created() {
    this.VipForm.维护人.id=window.localStorage.getItem('UserId');
  }

}
// 已删除残留代码
</script>

<style scoped>

</style>
