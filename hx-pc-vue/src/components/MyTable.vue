<template>
  <div>
    <h1>会员维护表</h1>

    <el-row :gutter="20">
      <!--部门数据-->
      <el-col :span="4" :xs="24">
        <!--        <div class="head-container">-->
        <!--          <el-input-->
        <!--              v-model="deptName"-->
        <!--              placeholder="请输入员工名称"-->
        <!--              clearable-->
        <!--              size="small"-->
        <!--              prefix-icon="el-icon-search"-->
        <!--              style="margin-bottom: 20px"-->
        <!--          />-->
        <!--        </div>-->
        <div class="head-container">
          <el-tree
              :data="deptOptions"
              :props="defaultProps"
              :expand-on-click-node="false"
              ref="tree"
              node-key="id"
              default-expand-all
              highlight-current
              @node-click="handleNodeClick"
          />
        </div>
      </el-col>


      <el-col :span="20" :xs="24">
        <!--        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="68px">-->
        <!--          <el-form-item label="用户名称" prop="userName">-->
        <!--            <el-input-->
        <!--                v-model="queryParams.userName"-->
        <!--                placeholder="请输入用户名称"-->
        <!--                clearable-->
        <!--                style="width: 240px"-->
        <!--                @keyup.enter.native="handleQuery"-->
        <!--            />-->
        <!--          </el-form-item>-->
        <!--          <el-form-item label="手机号码" prop="phonenumber">-->
        <!--            <el-input-->
        <!--                v-model="queryParams.phonenumber"-->
        <!--                placeholder="请输入手机号码"-->
        <!--                clearable-->
        <!--                style="width: 240px"-->
        <!--                @keyup.enter.native="handleQuery"-->
        <!--            />-->
        <!--          </el-form-item>-->

        <!--          <el-form-item>-->
        <!--            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>-->
        <!--            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>-->
        <!--          </el-form-item>-->
        <!--        </el-form>-->
        <el-row :gutter="10" class="mb8">
          <!--          <el-col :span="1.5">-->
          <!--            <el-button-->
          <!--                type="primary"-->
          <!--                plain-->
          <!--                icon="el-icon-plus"-->
          <!--                size="mini"-->
          <!--                @click="handleAdd"-->
          <!--            >新增-->
          <!--            </el-button>-->
          <!--          </el-col>-->
          <!--          <el-col :span="1.5">-->
          <!--            <el-button-->
          <!--                type="success"-->
          <!--                plain-->
          <!--                icon="el-icon-edit"-->
          <!--                size="mini"-->
          <!--                :disabled="single"-->
          <!--                @click="handleUpdate"-->
          <!--            >修改</el-button>-->
          <!--          </el-col>-->
          <!--          <el-col :span="1.5">-->
          <!--            <el-button-->
          <!--                type="danger"-->
          <!--                plain-->
          <!--                icon="el-icon-delete"-->
          <!--                size="mini"-->
          <!--                :disabled="multiple"-->
          <!--                @click="handleDelete"-->
          <!--            >删除-->
          <!--            </el-button>-->
          <!--          </el-col>-->


        </el-row>
        <el-table
            :data="vips"
            border>

          <el-table-column
              prop="name"
              label="会员名"
              width="180">
          </el-table-column>
          <el-table-column
              prop="age"
              label="年龄"
              width="180">
          </el-table-column>
          <el-table-column
              prop="积分"
              label="积分">
          </el-table-column>
          <el-table-column
              prop="phone"
              label="手机号">
          </el-table-column>
          <el-table-column
              sortable
              prop="未消费天数"
              label="未消费天数">
          </el-table-column>
          <el-table-column
              sortable
              prop="adress"
              label="地址">
          </el-table-column>
        </el-table>

      </el-col>
    </el-row>

  </div>
</template>

<script>
export default {
  name: "MyTable",
  created() {
    this.getCascadeDept()
  },
  data() {
    return {
      vips: [],
      deptOptions: [
        {
          name: '兽霸',
          children: [{
            name: '郜晓敏'
          }
          ]
        },
        {
          name: '和信',
          children: [{
            name: '测试员工'
          }
          ]
        }
      ],
      defaultProps: {
        label: 'name',
        children: 'users'
      },
      queryParams: {}
    }
  },

  methods: {
    handleAdd() {

    },
    handleDelete() {

    },
    getCascadeDept() {
      this.$http.post('getCascadeDept').then(res => {
        this.deptOptions = res.data
      })
    },
    handleNodeClick(data) {
      if (data.type == undefined) {
        this.$http.post('getVipByUserID', {id: data.id, start: 0, end: 100000}).then(res => {
          this.vips = res.data
        })

      }
    }
  }
}
</script>

<style scoped>

</style>
