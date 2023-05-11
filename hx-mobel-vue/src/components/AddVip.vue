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
    onSubmit() {
      Dialog.confirm({
        title: '确认提交',
      })
          .then(() => {
            // on confirm
            this.$http.post('addVip',this.VipForm).then(res=>{
             if (res.data){
               this.form_show=false
               Toast.success('添加成功');
             }else {
               this.form_show=false
               Toast.fail('该会员已被添加')
             }

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
    onSearch(){
      this.$http.post('FindVips',{phone:this.phone}).then(res=>{
        this.VipList=res.data.MESSAGE
        if (this.VipList.length==0){
          this.popup_title='找不到数据,检查手机号是否正确'
        }else {
          this.popup_title='选择是哪一个会员'
        }
        this.show=true
        console.log(res.data)
      })
    },
    onChoose(Vip){
      //检查是否重复
      this.$http.post('CheckRepeat',{userid:this.VipForm.维护人.id,hyid:Vip.HYID}).then(res=>{
        if (res.data==true){
          //重复
          this.show=false
          Toast.fail('该会员已被添加');

        }else {
          //未重复

          //获取vip详细信息
          this.$http.post('FindVipInfo',{hyid:Vip.HYID}).then(res=>{
            this.VipForm.hyid=Vip.HYID
            for (var resKey of res.data.MESSAGE.VIPINFO) {
              if (resKey.FIELD=='C_NAME'){
                this.VipForm.name=resKey.VALUE
              }if (resKey.FIELD=='D_LASTBUY'){
                this.VipForm.未消费天数=resKey.VALUE
              }if (resKey.FIELD=='C_MOBILE'){
                this.VipForm.phone=resKey.VALUE
              }
              if(resKey.FIELD=='N_ALLVALUE'){
                this.VipForm.积分=resKey.VALUE
              }
            }
            this.show=false
            this.form_show=true
          })
        }
      })


    }
  },
  created() {
    this.VipForm.维护人.id=window.localStorage.getItem('UserId');
  }

}
</script>

<style scoped>

</style>
