<template>
  <div class="vip-system">
    <h2 class="system-title">会员维护系统</h2>

    <div class="main-layout">
      <!-- 左侧部门树 -->
      <div class="sidebar">
        <div class="panel-container">
          <div class="panel-header">
            <i class="el-icon-s-fold"></i>
            <span>部门组织</span>
          </div>
          <div v-loading="loadingDept" class="panel-body">
            <el-tree
                ref="tree"
                :allow-drag="allowDrag"
                :allow-drop="allowDrop"
                :data="deptOptions"
                :expand-on-click-node="false"
                :props="defaultProps"
                default-expand-all
                draggable
                empty-text="暂无部门数据"
                highlight-current
                node-key="id"
                @node-click="handleNodeClick"
                @node-drop="nodeDrop"
            />
          </div>
        </div>
      </div>

      <!-- 右侧内容区 -->
      <div class="main-content">
        <!-- 会员查找组件 -->
        <div class="panel-container">
          <div class="panel-header">
            <i class="el-icon-search"></i>
            <span>会员查找</span>
          </div>
          <div class="panel-body">
            <FindVip></FindVip>
          </div>
        </div>

        <!-- 部门统计表 -->
        <div v-if="NodeUser==null && deptUserOption.length>0" class="panel-container">
          <div class="panel-header">
            <i class="el-icon-s-data"></i>
            <span>部门统计</span>
          </div>
          <div v-loading="loadingDeptStats" class="panel-body">
            <el-table
                :data="deptUserOption"
                border
                highlight-current-row
                show-summary
                stripe
                style="width: 100%">
              <el-table-column
                  label="姓名"
                  prop="name"
                  width="180">
              </el-table-column>
              <el-table-column
                  label="念念不忘"
                  prop="念念不忘"
                  width="180">
              </el-table-column>
              <el-table-column
                  label="好久不见"
                  prop="好久不见"
                  width="180">
              </el-table-column>
            </el-table>
          </div>
        </div>

        <!-- 会员列表区域 -->
        <div v-if="NodeUser!=null" class="member-area">
          <!-- 员工信息面板 -->
          <div class="employee-info-container">
            <!-- 员工信息卡片 -->
            <div class="employee-card">
              <div class="employee-header">
                <i class="el-icon-user"></i>
                <span>员工信息</span>
              </div>
              <div class="employee-content">
                <div class="employee-name">{{ NodeUser.name }}</div>
                <div class="employee-actions">
                  <el-button icon="el-icon-edit" size="small" type="primary" @click="UserFormShow = true">修改账号
                  </el-button>
                  <el-button icon="el-icon-plus" size="small" type="success" @click="VipFindShow = true">添加会员
                  </el-button>
                </div>
              </div>
            </div>

            <!-- 念念不忘统计卡片 -->
            <div :class="{ 'active': type }" class="stat-card">
              <div class="stat-header">
                <span>念念不忘</span>
                <el-tag v-if="type" effect="dark" type="danger">当前</el-tag>
              </div>
              <div class="stat-value">{{ 念念不忘数量 }}</div>
            </div>

            <!-- 好久不见统计卡片 -->
            <div :class="{ 'active': !type }" class="stat-card">
              <div class="stat-header">
                <span>好久不见</span>
                <el-tag v-if="!type" effect="dark" type="primary">当前</el-tag>
              </div>
              <div class="stat-value">{{ 好久不见数量 }}</div>
            </div>
          </div>

          <!-- 会员列表 -->
          <div class="vip-list-panel panel-container">
            <div class="panel-header">
              <div class="panel-title">
                <i class="el-icon-s-custom"></i>
                <span>{{ table_title }}列表</span>
              </div>
              <div class="panel-actions">
                <span>切换表格：</span>
                <el-switch
                    v-model="type"
                    active-color="#ff4949"
                    active-text="念念不忘"
                    inactive-color="#409EFF"
                    inactive-text="好久不见"
                    @change="type_change">
                </el-switch>
              </div>
            </div>
            <div v-loading="loadingVips" class="panel-body">
              <el-table
                  :data="vips"
                  :default-sort="{prop: 'm', order: 'descending'}"
                  border
                  empty-text="暂无数据"
                  highlight-current-row
                  stripe
                  style="width: 100%"
                  @sort-change="handleSortChange"
              >
                <el-table-column align="center" label="序号" width="80">
                  <template slot-scope="scope">
                    {{ (currentPage - 1) * pageSize + scope.$index + 1 }}
                  </template>
                </el-table-column>
                <el-table-column align="center" label="会员名" prop="name" sortable="custom"></el-table-column>
                <el-table-column align="center" label="手机号" prop="phone"></el-table-column>
                <el-table-column align="center" label="年龄" prop="age" sortable="custom" width="100"></el-table-column>
                <el-table-column align="center" label="积分" prop="points" sortable="custom"
                                 width="100"></el-table-column>
                <el-table-column align="center" label="未消费天数" prop="nonConsumptionDays" sortable="custom"
                                 width="120"></el-table-column>
                <el-table-column :filter-method="filterHandler"
                                 :filters="[{text: 5, value: 5}, {text: 4, value: 4}, {text: 3, value: 3}, {text: 2, value: 2}, {text: 1, value: 1}]"
                                 align="center" label="R"
                                 prop="r"
                                 sortable="custom"
                                 width="80">
                  <template slot-scope="scope">
                    <el-tag :type="getTagType(scope.row.r)" size="medium">{{ scope.row.r }}</el-tag>
                  </template>
                </el-table-column>
                <el-table-column :filter-method="filterHandler"
                                 :filters="[{text: 5, value: 5}, {text: 4, value: 4}, {text: 3, value: 3}, {text: 2, value: 2}, {text: 1, value: 1}]"
                                 align="center" label="F"
                                 prop="f"
                                 sortable="custom"
                                 width="80">
                  <template slot-scope="scope">
                    <el-tag :type="getTagType(scope.row.f)" size="medium">{{ scope.row.f }}</el-tag>
                  </template>
                </el-table-column>
                <el-table-column :filter-method="filterHandler"
                                 :filters="[{text: 5, value: 5}, {text: 4, value: 4}, {text: 3, value: 3}, {text: 2, value: 2}, {text: 1, value: 1}]"
                                 align="center" label="M"
                                 prop="m"
                                 sortable="custom"
                                 width="80">
                  <template slot-scope="scope">
                    <el-tag :type="getTagType(scope.row.m)" size="medium">{{ scope.row.m }}</el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="地址" prop="address" show-overflow-tooltip sortable="custom"></el-table-column>
                <el-table-column :formatter="formatDateOnly" align="center" label="创建时间" prop="createTime"
                                 sortable="custom" width="120"></el-table-column>
                <el-table-column align="center" fixed="right" label="操作" width="200">
                  <template slot-scope="scope">
                    <el-button-group>
                      <el-button icon="el-icon-edit" size="small" type="primary"
                                 @click.native.prevent="OpenSetVip(scope.row)">修改
                      </el-button>
                      <el-button icon="el-icon-share" size="small" type="warning"
                                 @click.native.prevent="OpenMoveVip(scope.row)">转移
                      </el-button>
                      <el-button icon="el-icon-delete" size="small" type="danger"
                                 @click.native.prevent="confirmDelVip(scope.row)">删除
                      </el-button>
                    </el-button-group>
                  </template>
                </el-table-column>
              </el-table>

              <!-- 分页组件 -->
              <el-pagination
                  :current-page="currentPage"
                  :page-size="pageSize"
                  :page-sizes="[10, 20, 50, 100]"
                  :total="totalItems"
                  background
                  class="pagination"
                  layout="total, sizes, prev, pager, next, jumper"
                  @size-change="handleSizeChange"
                  @current-change="handleCurrentChange">
              </el-pagination>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 弹窗组件 -->
    <!-- 员工账号变更 -->
    <el-dialog :visible.sync="UserFormShow" center title="员工账号变更" width="30%">
      <el-form :model="user_form" label-position="right" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="user_form.name" autocomplete="off" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="user_form.password" autocomplete="off" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="UserFormShow = false">取 消</el-button>
        <el-button :loading="submittingUser" type="primary" @click="setUser">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 输入手机号查找会员 -->
    <el-dialog :visible.sync="VipFindShow" center title="输入手机号查找会员" width="30%">
      <el-form :model="vip_form" label-position="right" label-width="80px">
        <el-form-item label="手机号">
          <el-input v-model="vip_form.phone_search" autocomplete="off" maxlength="11"
                    placeholder="请输入手机号"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="VipFindShow = false">取 消</el-button>
        <el-button :loading="searchingVip" type="primary" @click="FindVipByPhone">搜 索</el-button>
      </div>
    </el-dialog>

    <!-- 会员转移窗口 -->
    <el-dialog :visible.sync="VipMoveShow" center title="请选择转移到哪个员工账号" width="30%">
      <div class="move-vip-form">
        <p class="move-vip-info">会员：
          <el-tag>{{ VipMoveForm.Vip.name }}</el-tag>
        </p>
        <el-cascader
            v-model="VipMoveForm.targetUserId"
            :options="deptOptions"
            :props="defaultProps"
            placeholder="请选择目标员工"
            style="width: 100%"
        ></el-cascader>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="VipMoveShow = false">取 消</el-button>
        <el-button :disabled="!VipMoveForm.targetUserId || VipMoveForm.targetUserId.length < 2" :loading="movingVip"
                   type="primary"
                   @click="confirmMoveVip">
          确 定
        </el-button>
      </div>
    </el-dialog>

    <!-- 会员列表选择窗口 -->
    <el-dialog :visible.sync="VipListShow" center title="找到的会员" width="40%">
      <el-table
          v-loading="loadingVipList"
          :data="FindVipList"
          border
          highlight-current-row
          stripe
          style="width: 100%">
        <el-table-column
            label="姓名"
            prop="C_NAME"
            width="120">
        </el-table-column>
        <el-table-column
            label="手机号"
            prop="C_MOBILE"
            width="120">
        </el-table-column>
        <el-table-column
            label="信息"
            prop="INFO"
            show-overflow-tooltip>
        </el-table-column>
        <el-table-column align="center" label="操作" width="100">
          <template slot-scope="scope">
            <el-button
                icon="el-icon-check"
                size="small"
                type="primary"
                @click="choose_vip_from_search(scope.row)">选择
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div v-if="FindVipList.length === 0" slot="footer" class="dialog-footer">
        <span class="text-muted">未找到会员信息</span>
      </div>
    </el-dialog>

    <!-- 会员信息详情窗口 (用于添加) -->
    <el-dialog :visible.sync="VipFormShow" center title="添加会员" width="40%">
      <el-form :model="vip_form_detail" label-position="right" label-width="100px">
        <div class="form-row">
          <el-form-item class="form-item" label="会员名">
            <el-input v-model="vip_form_detail.name" :disabled="true" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item class="form-item" label="手机号">
            <el-input v-model="vip_form_detail.phone" :disabled="true" autocomplete="off"></el-input>
          </el-form-item>
        </div>
        <div class="form-row">
          <el-form-item class="form-item" label="年龄">
            <el-select v-model="vip_form_detail.age" placeholder="请选择" style="width: 100%">
              <el-option
                  v-for="item in ageOptions"
                  :key="item"
                  :label="item"
                  :value="item">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item class="form-item" label="积分">
            <el-input v-model="vip_form_detail.points" :disabled="true" autocomplete="off"></el-input>
          </el-form-item>
        </div>
        <el-form-item label="未消费天数">
          <el-input v-model="vip_form_detail.nonConsumptionDays" :disabled="true" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="vip_form_detail.address" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="VipFormShow = false">取 消</el-button>
        <el-button :loading="addingVip" type="primary" @click="confirmAddVip">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 会员修改窗口 -->
    <el-dialog :visible.sync="VipSetShow" center title="会员修改" width="40%">
      <el-form :model="setVipInfoForm" label-position="right" label-width="80px">
        <div class="form-row">
          <el-form-item class="form-item" label="姓名">
            <el-input v-model="setVipInfoForm.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item class="form-item" label="年龄">
            <el-select v-model="setVipInfoForm.age" placeholder="请选择" style="width: 100%">
              <el-option
                  v-for="item in ageOptions"
                  :key="item"
                  :label="item"
                  :value="item">
              </el-option>
            </el-select>
          </el-form-item>
        </div>
        <el-form-item label="地址">
          <el-input v-model="setVipInfoForm.address" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="VipSetShow = false">取 消</el-button>
        <el-button :loading="updatingVip" type="primary" @click="confirmSetVip">提 交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import FindVip from "@/components/FindVip.vue";
import {deptApi, vipApi, userApi} from '@/services/api.js';

export default {
  name: "MyTable",
  components: {FindVip},
  created() {
    this.getCascadeDept();
    // 设置默认排序，并初次加载数据
    this.sortProp = 'm'; // 对应 prop="m"
    this.sortOrder = 'desc'; // descending
    // this.loadCurrentVips(); // handleNodeClick 或 type_change 会在 NodeUser 确定后调用
  },
  data() {
    return {
      // 加载状态
      loadingDept: false,
      loadingDeptStats: false,
      loadingVips: false,
      loadingVipList: false,
      submittingUser: false,
      searchingVip: false,
      addingVip: false,
      updatingVip: false,
      movingVip: false,

      vips: [],
      currentPage: 1,
      pageSize: 10,
      totalItems: 0,
      sortProp: 'm', // 默认排序列
      sortOrder: 'desc', // 默认排序顺序 asc/desc
      user_form: {id: '', name: '', password: ''},
      vip_form_detail: {
        hyid: '',
        phone: '',
        name: '',
        age: '',
        points: '',
        nonConsumptionDays: '',
        address: '',
      },
      vip_form: {
        phone_search: ''
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
      defaultProps: {label: 'name', value: 'id', children: 'users'},
      type: true,
      table_title: '念念不忘',
      NodeUser: null,
      念念不忘数量: '',
      好久不见数量: '',
      deptUserOption: [],
      setVipInfoForm: {},
      VipMoveForm: {Vip: {}, targetUserId: null}
    }
  },
  methods: {
    // 获取标签类型
    getTagType(value) {
      switch (value) {
        case 5:
          return 'success';
        case 4:
          return 'info';
        case 3:
          return '';
        case 2:
          return 'warning';
        case 1:
          return 'danger';
        default:
          return '';
      }
    },
    handleSortChange({prop, order}) {
      this.sortProp = prop;
      if (order === 'ascending') {
        this.sortOrder = 'asc';
      } else if (order === 'descending') {
        this.sortOrder = 'desc';
      } else { // 取消排序或未知
        this.sortProp = 'm'; // 恢复默认排序或置空
        this.sortOrder = 'desc';
      }
      this.currentPage = 1; // 排序改变，回到第一页
      this.loadCurrentVips();
    },
    formatDateOnly(row, column, cellValue) {
      if (!cellValue) {
        return '';
      }
      const date = new Date(cellValue);
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    filterHandler(value, row, column) {
      const property = column['property'];
      return row[property] === value;
    },
    async confirmMoveVip() {
      if (!this.VipMoveForm.Vip || !this.VipMoveForm.targetUserId || this.VipMoveForm.targetUserId.length < 2) {
        this.$message.error('请选择要转移的会员和目标员工');
        return;
      }
      const vipId = this.VipMoveForm.Vip.id;
      const targetMaintainerId = this.VipMoveForm.targetUserId[1];

      this.$confirm('确定转移该会员?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          this.movingVip = true;
          const success = await vipApi.moveVip(vipId, targetMaintainerId);
          this.movingVip = false;
          if (success) {
            this.VipMoveShow = false;
            this.VipMoveForm.targetUserId = null;
            this.$message.success('转移成功!');
            this.refreshCurrentNodeVips();
          } else {
            this.$message.warning('转移失败，该会员可能已被目标员工维护');
          }
        } catch (error) {
          this.movingVip = false;
          console.error("转移VIP失败:", error);
          this.$message.error('转移VIP操作失败');
        }
      }).catch(() => {
        this.$message.info('已取消转移');
      });
    },
    OpenMoveVip(vip) {
      this.VipMoveForm.Vip = vip;
      this.VipMoveForm.targetUserId = null;
      this.VipMoveShow = true;
    },
    async confirmAddVip() {
      this.$confirm('是否确认添加该会员?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        // 构建会员数据对象，确保数值字段正确转换为数字类型
        const vipDto = {
          hyid: parseInt(this.vip_form_detail.hyid, 10),
          name: this.vip_form_detail.name,
          age: this.vip_form_detail.age,
          points: parseInt(this.vip_form_detail.points, 10) || 0,
          phone: this.vip_form_detail.phone,
          nonConsumptionDays: parseInt(this.vip_form_detail.nonConsumptionDays, 10) || 0,
          address: this.vip_form_detail.address,
          maintainerId: this.NodeUser.id,
          // 添加默认的RFM值
          r: 3,
          f: 3,
          m: 3
        };

        // 验证必要字段
        if (isNaN(vipDto.hyid)) {
          this.$message.error('会员ID必须是有效数字');
          return;
        }

        try {
          this.addingVip = true;
          const success = await vipApi.addVip(vipDto);
          this.addingVip = false;
          this.VipFormShow = false;
          if (success) {
            this.$message.success('添加成功!');
            this.refreshCurrentNodeVips();
          } else {
            this.$message.warning('添加失败，会员可能已被添加或不符合校验规则');
          }
        } catch (error) {
          this.addingVip = false;
          console.error("添加VIP失败:", error);
          this.$message.error('添加VIP操作失败');
        }
      }).catch(() => {
        this.$message.info('已取消添加');
      });
    },
    async choose_vip_from_search(selectedVip) {
      try {
        // 保存基本信息
        this.vip_form_detail.hyid = selectedVip.HYID;
        this.vip_form_detail.name = selectedVip.C_NAME;
        this.vip_form_detail.phone = selectedVip.C_MOBILE;

        // 关闭会员列表弹窗，显示加载状态
        this.VipListShow = false;
        this.loadingVipList = true;

        // 调用API获取会员详情
        const detailResult = await vipApi.getVipDetailByHyid(selectedVip.HYID);
        this.loadingVipList = false;

        // 处理详情信息
        if (detailResult && detailResult.STATUS === 0 && detailResult.MESSAGE) {
          // MESSAGE直接是对象，不是数组
          const vipDetail = detailResult.MESSAGE;

          // 解析VIPINFO数组以获取具体字段值
          if (vipDetail.VIPINFO && Array.isArray(vipDetail.VIPINFO)) {
            for (const info of vipDetail.VIPINFO) {
              if (info.FIELD === 'D_LASTBUY') {
                this.vip_form_detail.nonConsumptionDays = info.VALUE || '';
              } else if (info.FIELD === 'N_ALLVALUE') {
                this.vip_form_detail.points = info.VALUE || '';
              }
            }
          }
        } else {
          // 如果详情获取失败，则使用搜索结果中的基本信息
          this.vip_form_detail.nonConsumptionDays = selectedVip.D_LASTBUY !== undefined ? selectedVip.D_LASTBUY : '';
          this.vip_form_detail.points = selectedVip.N_ALLVALUE !== undefined ? selectedVip.N_ALLVALUE : '';
          console.warn("获取会员详情失败，使用搜索结果中的基本信息");
        }

        // 清空需要用户填写的字段
        this.vip_form_detail.age = '';
        this.vip_form_detail.address = '';

        // 显示会员添加表单
        this.VipFormShow = true;
      } catch (error) {
        this.loadingVipList = false;
        console.error("获取会员详情失败:", error);
        this.$message.error('获取会员详情失败，请重试');
      }
    },
    async FindVipByPhone() {
      if (!this.vip_form.phone_search || this.vip_form.phone_search.trim() === '') {
        this.$message.warning('请输入手机号');
        return;
      }
      try {
        this.searchingVip = true;
        const resultJson = await vipApi.findVipsByPhoneNumber(this.vip_form.phone_search.trim());
        this.searchingVip = false;

        // 外部API成功时 STATUS 为 0
        if (resultJson && resultJson.STATUS === 0 && resultJson.MESSAGE && Array.isArray(resultJson.MESSAGE)) {
          if (resultJson.MESSAGE.length === 0) {
            this.$message.warning('没有找到会员, 请检查手机号是否正确');
            this.FindVipList = [];
          } else {
            this.FindVipList = resultJson.MESSAGE;
            this.VipFindShow = false; // 关闭查找输入框
            this.VipListShow = true;  // 显示会员列表弹窗
          }
        } else {
          // 处理查找失败或外部API返回错误状态的情况
          let errorMsg = '查找会员失败，请稍后再试'; // 默认错误消息
          if (resultJson && resultJson.MESSAGE) {
            if (typeof resultJson.MESSAGE === 'string') {
              errorMsg = resultJson.MESSAGE;
            } else if (Array.isArray(resultJson.MESSAGE) && resultJson.MESSAGE.length > 0) {
              // 尝试从 MESSAGE 数组的第一个对象中提取 INFO 或 ERROR_INFO
              const firstMessageItem = resultJson.MESSAGE[0];
              if (typeof firstMessageItem === 'object' && firstMessageItem !== null) {
                if (firstMessageItem.INFO) {
                  errorMsg = firstMessageItem.INFO;
                } else if (firstMessageItem.ERROR_INFO) {
                  errorMsg = firstMessageItem.ERROR_INFO;
                } else if (Object.keys(firstMessageItem).length > 0) {
                  // 如果有其他字段，可以考虑显示，或者用一个通用错误
                  // errorMsg = JSON.stringify(firstMessageItem); // 作为最后的手段
                }
              }
            }
          } else if (resultJson && resultJson.STATUS !== undefined) {
            errorMsg = `查找失败，状态码: ${resultJson.STATUS}`;
          }
          this.$message.error(errorMsg);
          this.FindVipList = [];
        }
      } catch (error) {
        this.searchingVip = false;
        console.error("FindVipByPhone 失败:", error);
        this.$message.error('搜索会员请求失败');
      }
    },
    async setUser() {
      const userDto = {
        id: this.NodeUser.id,
        name: this.user_form.name,
        password: this.user_form.password,
      };
      try {
        this.submittingUser = true;
        await userApi.updateUser(userDto);
        this.submittingUser = false;
        this.UserFormShow = false;
        this.$message.success('员工账户修改成功');
        this.getCascadeDept();
      } catch (error) {
        this.submittingUser = false;
        console.error("修改用户失败:", error);
        this.$message.error('修改用户操作失败');
      }
    },
    handleSizeChange(newSize) {
      this.pageSize = newSize;
      this.currentPage = 1;
      this.loadCurrentVips();
    },
    handleCurrentChange(newPage) {
      this.currentPage = newPage;
      this.loadCurrentVips();
    },
    loadCurrentVips() {
      if (!this.NodeUser || !this.NodeUser.id) {
        this.vips = [];
        this.totalItems = 0;
        return;
      }
      if (this.type) {
        this.get念念不忘();
      } else {
        this.get好久不见();
      }
    },
    type_change() {
      this.currentPage = 1;
      if (this.type) {
        this.table_title = '念念不忘';
      } else {
        this.table_title = '好久不见';
      }
      this.loadCurrentVips();
    },
    async getCascadeDept() {
      try {
        this.loadingDept = true;
        const data = await deptApi.getCascadeDept();
        this.deptOptions = data;
        this.loadingDept = false;
      } catch (error) {
        this.loadingDept = false;
        console.error("获取级联部门数据失败:", error);
        this.$message.error('获取部门数据失败');
        this.deptOptions = [];
      }
    },
    async getNum() {
      if (!this.NodeUser || !this.NodeUser.id) {
        this.念念不忘数量 = 0;
        this.好久不见数量 = 0;
        return;
      }
      try {
        this.念念不忘数量 = await vipApi.getNeverForgetVipsCount(this.NodeUser.id);
        this.好久不见数量 = await vipApi.getLongTimeNoSeeVipsCount(this.NodeUser.id);
      } catch (error) {
        console.error("获取VIP数量统计失败:", error);
        this.$message.error('获取VIP数量统计失败');
        this.念念不忘数量 = 0;
        this.好久不见数量 = 0;
      }
    },
    async handleNodeClick(data) {
      this.currentPage = 1;
      if (data.users !== undefined) {
        this.NodeUser = null;
        this.vips = [];
        this.totalItems = 0;
        try {
          this.loadingDeptStats = true;
          this.deptUserOption = await deptApi.getDeptStatsById(data.id);
          this.loadingDeptStats = false;
        } catch (error) {
          this.loadingDeptStats = false;
          console.error("获取部门统计数据失败:", error);
          this.$message.error('获取部门统计数据失败');
          this.deptUserOption = [];
        }
      } else {
        this.NodeUser = data;
        this.deptUserOption = [];
        this.type_change();
        await this.getNum();
      }
    },
    async get念念不忘() {
      if (!this.NodeUser || !this.NodeUser.id) {
        this.vips = [];
        this.totalItems = 0;
        return;
      }
      try {
        this.loadingVips = true;
        const response = await vipApi.getNeverForgetVips(this.NodeUser.id, this.currentPage, this.pageSize, this.sortProp, this.sortOrder);
        this.loadingVips = false;

        if (response && typeof response.total !== 'undefined' && Array.isArray(response.list)) {
          this.vips = response.list;
          this.totalItems = response.total;
        } else {
          this.vips = [];
          this.totalItems = 0;
          this.$message.error('获取念念不忘列表数据格式不正确');
          console.error("获取念念不忘列表数据格式不正确, response:", response);
        }
      } catch (error) {
        this.loadingVips = false;
        console.error("获取念念不忘列表失败:", error);
        this.$message.error('获取念念不忘列表失败');
        this.vips = [];
        this.totalItems = 0;
      }
    },
    async get好久不见() {
      if (!this.NodeUser || !this.NodeUser.id) {
        this.vips = [];
        this.totalItems = 0;
        return;
      }
      try {
        this.loadingVips = true;
        const response = await vipApi.getLongTimeNoSeeVips(this.NodeUser.id, this.currentPage, this.pageSize, this.sortProp, this.sortOrder);
        this.loadingVips = false;

        if (response && typeof response.total !== 'undefined' && Array.isArray(response.list)) {
          this.vips = response.list;
          this.totalItems = response.total;
        } else {
          this.vips = [];
          this.totalItems = 0;
          this.$message.error('获取好久不见列表数据格式不正确');
          console.error("获取好久不见列表数据格式不正确, response:", response);
        }
      } catch (error) {
        this.loadingVips = false;
        console.error("获取好久不见列表失败:", error);
        this.$message.error('获取好久不见列表失败');
        this.vips = [];
        this.totalItems = 0;
      }
    },
    OpenSetVip(vip) {
      this.setVipInfoForm = {...vip};
      this.VipSetShow = true;
    },
    async confirmSetVip() {
      try {
        this.updatingVip = true;
        await vipApi.updateVip(this.setVipInfoForm);
        this.updatingVip = false;
        this.VipSetShow = false;
        this.$message.success('会员信息修改成功!');
        this.loadCurrentVips();
      } catch (error) {
        this.updatingVip = false;
        console.error("修改VIP信息失败:", error);
        this.$message.error('修改VIP信息操作失败');
      }
    },
    async confirmDelVip(vip) {
      this.$confirm('确定删除该会员?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          this.loadingVips = true;
          await vipApi.deleteVip(vip.id);
          this.loadingVips = false;
          this.$message.success('删除成功!');
          if (this.vips.length === 1 && this.currentPage > 1) {
            this.currentPage--;
          }
          this.loadCurrentVips();
          await this.getNum();
        } catch (error) {
          this.loadingVips = false;
          console.error("删除VIP失败:", error);
          this.$message.error('删除VIP操作失败');
        }
      }).catch(() => {
        this.$message.info('已取消删除');
      });
    },
    allowDrop(draggingNode, dropNode, type) {
      return type === 'inner' && dropNode.data.users !== undefined;
    },
    allowDrag(draggingNode) {
      return draggingNode.data.users === undefined;
    },
    async nodeDrop(draggingNode, dropNode) {
      const user = draggingNode.data;
      const dept = dropNode.data;
      try {
        this.loadingDept = true;
        await userApi.moveUser(user.id, dept.id);
        this.$message.success(`已成功将 [${user.name}] 转移到 [${dept.name}]`);
        await this.getCascadeDept();
        this.loadingDept = false;
        if (this.NodeUser && this.NodeUser.id === user.id) {
          this.NodeUser = null;
          this.vips = [];
          this.totalItems = 0;
        }
      } catch (error) {
        this.loadingDept = false;
        console.error("移动用户失败:", error);
        this.$message.error('移动用户操作失败');
      }
    },
    refreshCurrentNodeVips() {
      if (this.NodeUser && this.NodeUser.id) {
        this.loadCurrentVips();
        this.getNum();
      }
    }
  }
}
</script>

<style scoped>
.vip-system {
  padding: 20px;
}

.system-title {
  margin-bottom: 20px;
  color: #303133;
  font-weight: bold;
  text-align: center;
  font-size: 24px;
}

.main-layout {
  display: flex;
  gap: 20px;
}

.sidebar {
  width: 250px;
  flex-shrink: 0;
}

.main-content {
  flex: 1;
  min-width: 0; /* 防止flex子项溢出 */
}

.panel-container {
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin-bottom: 20px;
  overflow: hidden;
}

.panel-header {
  padding: 12px 20px;
  background-color: #f5f7fa;
  border-bottom: 1px solid #e6ebf5;
  font-weight: bold;
  color: #303133;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.panel-title {
  display: flex;
  align-items: center;
}

.panel-title i {
  margin-right: 8px;
}

.panel-body {
  padding: 20px;
  min-height: 100px;
}

/* 员工卡片布局 */
.employee-info-container {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.employee-card {
  flex: 1;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.employee-header {
  background-color: #f5f7fa;
  padding: 12px 15px;
  border-bottom: 1px solid #e6ebf5;
  font-weight: bold;
  font-size: 16px;
  display: flex;
  align-items: center;
}

.employee-header i {
  margin-right: 8px;
}

.employee-content {
  padding: 15px;
}

.employee-name {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 15px;
  color: #303133;
}

.employee-actions {
  display: flex;
  gap: 10px;
}

.stat-card {
  flex: 1;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;
  min-width: 150px;
  transition: all 0.3s;
}

.stat-card.active {
  transform: translateY(-3px);
  box-shadow: 0 4px 18px 0 rgba(0, 0, 0, 0.15);
  border: 1px solid #dcdfe6;
}

.stat-header {
  background-color: #f5f7fa;
  padding: 12px 15px;
  border-bottom: 1px solid #e6ebf5;
  font-weight: bold;
  font-size: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  text-align: center;
  padding: 15px;
  color: #303133;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

.move-vip-form {
  padding: 0 20px;
}

.move-vip-info {
  margin-bottom: 15px;
}

.member-area {
  margin-top: 20px;
}

.text-muted {
  color: #909399;
  font-size: 14px;
}

.form-row {
  display: flex;
  gap: 20px;
  margin-bottom: 0;
}

.form-item {
  flex: 1;
  min-width: 0;
}

/* 响应式设计 */
@media screen and (max-width: 1200px) {
  .employee-info-container {
    flex-wrap: wrap;
  }

  .employee-card {
    min-width: 100%;
  }

  .stat-card {
    min-width: 45%;
  }
}

@media screen and (max-width: 992px) {
  .main-layout {
    flex-direction: column;
  }

  .sidebar {
    width: 100%;
  }
}

@media screen and (max-width: 768px) {
  .form-row {
    flex-direction: column;
    gap: 0;
  }

  .stat-card {
    min-width: 100%;
  }
}
</style>
