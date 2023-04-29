<template>
  <div>
    <van-nav-bar
        title="新增会员"
        left-text="返回"
        left-arrow
        @click-left="onClickLeft"
    />
    <van-form @submit="onSubmit">
      <van-field
          v-model="VipForm.vipname"
          name="会员名称"
          label="会员名称"
          placeholder="会员名称"
          :rules=rules
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
          readonly
          clickable
          name="picker"
          :value="VipForm.积分"
          label="积分"
          placeholder="点击选择积分"
          @click="积分.showPicker = true"
          :rules=rules
      />
      <van-popup v-model="积分.showPicker" position="bottom">
        <van-picker
            show-toolbar
            :columns="积分.columns"
            @confirm="jf_onConfirm"
            @cancel="积分.showPicker = false"
        />
      </van-popup>

      <van-field
          v-model="VipForm.phone"
          type="tel"
          name="手机号"
          label="手机号"
          placeholder="手机号"
          :rules=rules
      />
      <van-field
          v-model="VipForm.未消费天数"
          type="digit"
          name="未消费天数"
          label="未消费天数"
          placeholder="未消费天数"
          :rules=rules
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
  </div>
</template>

<script>
import {Dialog} from "vant";

export default {
  name: "AddVip",
  data() {
    return {
      VipForm: {
        vipname: '',
        age: '',
        积分: '',
        phone: '',
        未消费天数: '',
        adress: '',
        维护人id: Number
      },
      age: {
        columns: ['20-30岁', '30-40岁', '40-50岁', '大于50岁'],
        showPicker: false,
      },
      积分:{
        columns: ['1000以下', '1000-3000', '3000-6000', '6000-10000','10000以上'],
        showPicker: false,
      },
      rules:[{ required: true, message: '不能为空' }]
    }
  },
  methods: {
    onClickLeft() {
      this.$router.back()
    },
    onSubmit() {
      Dialog.confirm({
        title: '确认提交',
      })
          .then(() => {
            // on confirm
            this.$http.post('addVip',this.VipForm).then(res=>{
              console.log(res.data)
            })
          })
          .catch(() => {
            // on cancel
          });

    },
    age_onConfirm(value) {
      this.VipForm.age = value;
      this.age.showPicker = false;
    },
    jf_onConfirm(value){
      this.VipForm.积分 = value;
      this.积分.showPicker = false;
    }
  },
  created() {
    this.VipForm.维护人id=window.localStorage.getItem('UserId');
  }

}
</script>

<style scoped>

</style>
