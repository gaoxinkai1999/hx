<template>
  <div>
    查找会员
    <el-autocomplete
        v-model="name"
        :fetch-suggestions="querySearchAsync"
        placeholder="请输入内容"
        value-key="name"
    ></el-autocomplete>
    <el-button type="primary" @click="getVipsByName">查找</el-button>

    <el-dialog
        title="提示"
        :visible.sync="dialogVisible">

      <el-table
          :data="result"
          style="width: 100%">
        <el-table-column
            prop="name"
            label="姓名"
            width="180">
        </el-table-column>
        <el-table-column
            prop="phone"
            label="手机号"
            width="180">
        </el-table-column>
        <el-table-column
            prop="维护人.name"
            label="维护人">
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "FindVip",
  data() {
    return {
      dialogVisible:false,
      restaurants: [],
      name: '',
      result:[]
    }
  },
  methods: {
    querySearchAsync(queryString, cb) {
      this.$http.post('FindVipLikeName',{name:this.name}).then(res=>{
        this.restaurants=res.data
        cb(this.restaurants);
      })
    },
    getVipsByName(){
      this.$http.post('getVipsByName',{name:this.name}).then(res=>{
        console.log(res.data)
        this.result=res.data
        this.dialogVisible=true
      })
    }
  },
}
</script>

<style scoped>

</style>
