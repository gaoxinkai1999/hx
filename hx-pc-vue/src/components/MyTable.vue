<template>
  <div>
    <h2>会员维护系统</h2>

    <el-row :gutter="24">
      <!--部门数据-->
      <el-col :span="3" :xs="24">
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
              :allow-drop="allowDrop"
              :allow-drag="allowDrag"
              draggable
              @node-drop="nodeDrop"
          />
        </div>
      </el-col>

      <!--      各部门统计表-->
      <el-col :span="20" :xs="24">
        <el-divider></el-divider>
        <FindVip></FindVip>
      </el-col>
      <el-col :span="16" :xs="24" v-if="NodeUser==null&&this.deptUserOption.length>0">

        <el-table
            :data="deptUserOption"
            style="width: 100%"
            show-summary>
          <el-table-column
              prop="name"
              label="姓名"
              width="180">
          </el-table-column>
          <el-table-column
              prop="念念不忘"
              label="念念不忘"
              width="180">
          </el-table-column>
          <el-table-column
              prop="好久不见"
              label="好久不见"
              width="180">
          </el-table-column>
        </el-table>
      </el-col>


      <el-col :span="20" :xs="24" v-if="NodeUser!=null">

        <el-divider></el-divider>

        <el-col :span="4">
          <h3>{{ NodeUser.name }}</h3>
          <!-- Form -->

          <el-col :span="12">

            <el-button type="text" @click="UserFormShow = true">修改账号</el-button>

            <el-dialog title="员工账号变更" :visible.sync="UserFormShow">
              <el-form :model="user_form" style="width: 50%;text-align: center">
                <el-form-item label="用户名">
                  <el-input v-model="user_form.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="密码">
                  <el-input v-model="user_form.password" autocomplete="off"></el-input>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button @click="UserFormShow = false">取 消</el-button>
                <el-button type="primary" @click="setUser">确 定</el-button>
              </div>
            </el-dialog>
          </el-col>
          <el-col :span="12">

            <el-button type="text" @click="VipFindShow = true">添加会员</el-button>
            <!--            //输入手机号查找会员列表-->
            <el-dialog title="输入手机号查找会员" :visible.sync="VipFindShow" width="30%">

              <el-form :model="vip_form">
                <el-form-item label="手机号">
                  <el-input v-model="vip_form.phone" autocomplete="off"></el-input>
                </el-form-item>
              </el-form>

              <div slot="footer" class="dialog-footer">
                <el-button @click="VipFindShow = false">取 消</el-button>
                <el-button type="primary" @click="FindVip">搜索</el-button>
              </div>
            </el-dialog>

            <!--      会员转移窗口-->
            <el-dialog title="请选择转移到哪个员工账号" :visible.sync="VipMoveShow" width="30%">
              <el-cascader
                  v-model="VipMoveForm.UserId"
                  :props="defaultProps"
                  :options="deptOptions"
              ></el-cascader>
              <div slot="footer" class="dialog-footer" v-if="VipMoveForm.UserId!=''">
                <el-button @click="VipMoveShow = false">取 消</el-button>
                <el-button type="primary" @click="MoveVip">确 定</el-button>
              </div>
            </el-dialog>

            <!--            会员列表选择窗口-->
            <el-dialog title="找到的会员" :visible.sync="VipListShow" width="30%">
              <el-table
                  :data="FindVipList"
                  style="width: 100%">
                <el-table-column
                    prop="C_NAME"
                    label="姓名"
                    width="180">
                </el-table-column>
                <el-table-column
                    prop="INFO"
                    label="信息"
                >
                </el-table-column>
                <el-table-column label="操作">
                  <template slot-scope="scope">
                    <el-button
                        size="mini"
                        @click="choose_vip(scope.$index, scope.row)">选择
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-dialog>
            <!--            会员信息详情窗口-->
            <el-dialog title="添加会员" :visible.sync="VipFormShow" width="30%">

              <el-form :model="vip_form">
                <el-form-item label="会员名">
                  <el-input v-model="vip_form.name" autocomplete="off" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="手机号">
                  <el-input v-model="vip_form.phone" :disabled="true" autocomplete="off"></el-input>
                </el-form-item>

                <el-form-item label="年龄">
                  <el-select v-model="vip_form.age" placeholder="请选择">
                    <el-option
                        v-for="item in ageOptions"
                        :key="item"
                        :label="item"
                        :value="item">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="积分">
                  <el-input v-model="vip_form.积分" autocomplete="off" :disabled="true"></el-input>
                </el-form-item>
                <el-form-item label="未消费天数">
                  <el-input v-model="vip_form.未消费天数" :disabled="true" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="地址">
                  <el-input v-model="vip_form.adress" autocomplete="off"></el-input>
                </el-form-item>
              </el-form>


              <div slot="footer" class="dialog-footer">
                <el-button @click="VipFormShow = false">取 消</el-button>
                <el-button type="primary" @click="addVip">确 定</el-button>
              </div>
            </el-dialog>
            <!--            会员修改窗口-->
            <el-dialog title="会员修改" :visible.sync="VipSetShow" width="30%">

              <el-form :model="setVipInfo">
                <el-form-item label="姓名">
                  <el-input v-model="setVipInfo.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="年龄">
                  <el-select v-model="setVipInfo.age" placeholder="请选择">
                    <el-option
                        v-for="item in ageOptions"
                        :key="item"
                        :label="item"
                        :value="item">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="地址">
                  <el-input v-model="setVipInfo.adress" autocomplete="off"></el-input>
                </el-form-item>
              </el-form>

              <div slot="footer" class="dialog-footer">
                <el-button @click="VipSetShow = false">取 消</el-button>
                <el-button type="primary" @click="setVip">提交</el-button>
              </div>
            </el-dialog>
          </el-col>


        </el-col>
        <el-col :span="4">
          <el-statistic title="念念不忘数量">
            <template slot="formatter"> {{ 念念不忘数量 }}</template>
          </el-statistic>
        </el-col>
        <el-col :span="4">
          <el-statistic title="好久不见数量">
            <template slot="formatter"> {{ 好久不见数量 }}</template>
          </el-statistic>
        </el-col>

        <el-col :span="16">
          <el-divider></el-divider>
          <h3>{{ this.table_title }}
          </h3>
        </el-col>
        <el-col :span="4">
          <span>切换表格</span>
          <el-switch
              v-model="type"
              @change="type_change">
          </el-switch>
        </el-col>

        <el-table
            :data="vips"
            border
            style="width: 90%"
        >
          <el-table-column label="序号" width="80">
            <template slot-scope="scope">
              {{ scope.$index + 1 }}
            </template>
          </el-table-column>

          <el-table-column
              prop="name"
              label="会员名"
          >
          </el-table-column>
          <el-table-column
              prop="phone"

              label="手机号">
          </el-table-column>
          <el-table-column
              prop="age"
              label="年龄"
          >
          </el-table-column>
          <el-table-column
              sortable
              prop="积分"

              label="积分">
          </el-table-column>

          <el-table-column
              sortable
              prop="未消费天数"

              label="未消费天数">
          </el-table-column>
          <el-table-column
              sortable
              prop="R"

              label="R"
              :filters="[{text: 5, value: 5}, {text: 4, value: 4}, {text: 3, value: 3}, {text: 2, value: 2}, {text: 1, value: 1}]"
              :filter-method="filterHandler"
          >
          </el-table-column>
          <el-table-column
              sortable
              prop="F"

              label="F"
              :filters="[{text: 5, value: 5}, {text: 4, value: 4}, {text: 3, value: 3}, {text: 2, value: 2}, {text: 1, value: 1}]"
              :filter-method="filterHandler">
          </el-table-column>
          <el-table-column
              sortable
              prop="M"

              label="M"
              :filters="[{text: 5, value: 5}, {text: 4, value: 4}, {text: 3, value: 3}, {text: 2, value: 2}, {text: 1, value: 1}]"
              :filter-method="filterHandler">
          </el-table-column>
          <el-table-column
              sortable
              prop="adress"

              label="地址">
          </el-table-column>
          <el-table-column
              sortable
              prop="create_time"

              label="创建时间">
          </el-table-column>
          <el-table-column
              label="操作"
          >
            <template slot-scope="scope">
              <el-button
                  @click.native.prevent="OpenSetVip(scope.row)"
                  type="text"
                  size="small">
                修改
              </el-button>
              <el-button
                  @click.native.prevent="OpenMoveVip(scope.row)"
                  type="text"
                  size="small">
                转移
              </el-button>
              <el-button
                  @click.native.prevent="delVip(scope.row)"
                  type="text"
                  size="small">
                删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>

      </el-col>
    </el-row>

  </div>
</template>

<script>
import FindVip from "@/components/FindVip.vue";

export default {
  name: "MyTable",
  components: {FindVip},
  created() {
    this.getCascadeDept()
  },
  data() {
    return {
      vips: [],
      // 修改用户表单
      user_form: {
        id: '',
        name: '',
        password: '',
      },
      vip_form: {
        hyid: '',
        phone: '',
        name: '123',
        age: '',
        积分: '',
        未消费天数: '',
        adress: '',
        维护人: {
          id: ''
        }
      },
      UserFormShow: false,
      VipFormShow: false,
      VipFindShow: false,
      VipListShow: false,
      VipSetShow: false,
      VipMoveShow: false,
      FindVipList: [],
      deptOptions: [],
      ageOptions: ['60后', '70后', '80后', '90后', '00后', '未知'],
      defaultProps: {
        label: 'name',
        value: 'id',
        children: 'users',
      },
      queryParams: {},
      type: true,
      table_title: '念念不忘',
      NodeUser: null,
      念念不忘数量: '',
      好久不见数量: '',
      deptUserOption: [],
      setVipInfo: {},
      VipMoveForm: {
        Vip: {},
        UserId: ''
      }
    }
  },

  methods: {
    filterHandler(value, row, column) {
      // console.log(value)
      // console.log(row)
      // console.log(column)
      const property = column['property'];
      return row[property] === value;
    },
    MoveVip() {
      this.$confirm('确定转移该会员?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 确定转移
        this.VipMoveForm.UserId = this.VipMoveForm.UserId[1]
        this.$http.post('MoveVip', this.VipMoveForm).then(res => {
          if (res.data == true) {
            this.VipMoveShow = false
            this.VipMoveForm.UserId = ''
            this.$message({
              type: 'success',
              message: '添加成功!'
            });
          } else {
            this.$message({
              message: '当前员工账号已添加该会员,无法转移',
              type: 'warning'
            });
          }
        })

      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消转移'
        });
      });

    },
    OpenMoveVip(vip) {
      this.VipMoveForm.Vip = vip
      this.VipMoveShow = true
    },
    // // 级联选择器改变事件
    // handleChange(value){
    //   this.VipMoveForm.UserId=value
    // },
    addVip() {
      this.$confirm('是否确认添加该会员?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.vip_form.维护人.id = this.NodeUser.id
        this.$http.post('addVipPlus', this.vip_form).then(res => {
          this.VipFormShow = false
          if (res.data) {
            this.$message({
              type: 'success',
              message: '添加成功!'
            });
          } else {
            this.$message({
              message: '当前员工账号已添加该会员',
              type: 'warning'
            });
          }


        })

      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消添加'
        });
      });
    },
    choose_vip(index, row) {
      //检查是否重复
      this.$http.post('CheckRepeat_plus', {UserId: this.NodeUser.id, hyid: row.HYID}).then(res => {
        console.log(res.data)
        if (res.data == true) {
          this.$message({
            message: '当前员工账号已添加该会员',
            type: 'warning'
          });
        } else {
          //获取vip详细信息
          this.$http.post('FindVipInfo', {hyid: row.HYID}).then(res => {
            this.vip_form.hyid = row.HYID
            for (var resKey of res.data.MESSAGE.VIPINFO) {
              if (resKey.FIELD == 'C_NAME') {
                this.vip_form.name = resKey.VALUE
              }
              if (resKey.FIELD == 'D_LASTBUY') {
                this.vip_form.未消费天数 = resKey.VALUE
              }
              if (resKey.FIELD == 'C_MOBILE') {
                this.vip_form.phone = resKey.VALUE
              }
              if (resKey.FIELD == 'N_ALLVALUE') {
                this.vip_form.积分 = resKey.VALUE
              }
            }
            this.VipListShow = false
            this.VipFormShow = true
          })
        }
      })

    },
    FindVip() {
      this.$http.post('FindVips', {phone: this.vip_form.phone}).then(res => {
        if (res.data.MESSAGE.length == 0) {
          this.$message({
            message: '没有找到会员,检查手机号是否正确',
            type: 'warning'
          });
        } else {
          this.FindVipList = res.data.MESSAGE
          this.VipFindShow = false
          this.VipListShow = true
        }

      })
    },
    setUser() {
      this.user_form.id = this.NodeUser.id
      this.$http.post('setUser', this.user_form).then(res => {
        console.log(res.data)
        this.UserFormShow = false
        this.$message({
          message: '员工账户修改成功',
          type: 'success'
        });
      })
    },
    type_change() {
      if (this.type == true) {
        this.get念念不忘()
        this.table_title = '念念不忘'
      } else if (this.type == false) {
        this.get好久不见()
        this.table_title = '好久不见'

      }
    },
    getCascadeDept() {
      this.$http.post('getCascadeDept').then(res => {
        this.deptOptions = res.data
      })
    },
    getNum() {
      this.$http.post('念念不忘数量', {UserId: this.NodeUser.id}).then(res => {
        this.念念不忘数量 = res.data
      })
      this.$http.post('好久不见数量', {UserId: this.NodeUser.id}).then(res => {
        this.好久不见数量 = res.data
      })
    },
    handleNodeClick(data) {
      if (data.type == undefined) {
        this.NodeUser = data
        this.type_change()
        this.getNum()
      } else {
        this.NodeUser = null
        this.$http.post('getDeptCountById', {id: data.id}).then(res => {
          this.deptUserOption = res.data
        })
      }

    },
    get念念不忘() {
      this.$http.post('念念不忘', {UserId: this.NodeUser.id}).then(res => {
        this.vips = res.data
      })

    },
    get好久不见() {
      this.$http.post('好久不见', {UserId: this.NodeUser.id}).then(res => {
        this.vips = res.data
      })
    },
    OpenSetVip(vip) {
      this.setVipInfo = vip
      this.VipSetShow = true
    },
    setVip() {
      this.$http.post('setVip', this.setVipInfo).then(res => {
        console.log(res)
        this.VipSetShow = false
        this.$message({
          message: '会员信息修改成功,请刷新重新获取',
          type: 'success'
        });
      })
    },
    delVip(vip) {
      this.$confirm('确定删除该会员?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$http.post('delVip', {id: vip.id}).then(res => {
          console.log(res)
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
          this.handleNodeClick()
        })

      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    //判断能否放置
    allowDrop(draggingNode, dropNode, type) {
      if (type == 'inner' && dropNode.data.type != undefined) {
        return true;
      } else {
        return false;
      }
    },
    //判断节点能否被拖拽
    allowDrag(draggingNode) {
      return draggingNode.data.type == undefined;
    },
    //拖拽成功后回调
    nodeDrop(draggingNode, dropNode) {
      var user = draggingNode.data;
      var dept = dropNode.data;
      this.$http.post('MoveUser', {DeptId: dept.id, UserId: user.id}).then(res => {
        console.log(res.data)
        this.$message({
          message: '已成功将  [' + user.name + ']  转移到  [' + dept.name + ']',
          type: 'success'
        });
      })

    },

  }
}
</script>

<style scoped>

</style>
