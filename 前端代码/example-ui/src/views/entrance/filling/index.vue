<template>
  <div class="app-container">
    <!-- ================= 管理员视图：汇总管理列表 ================= -->
    <template v-if="checkRole(['admin', 'school_admin'])">
      <el-card shadow="hover" class="page-card">
        <div slot="header" class="clearfix">
          <span class="management-title">
            <i class="el-icon-s-management" style="color: #409EFF; margin-right: 8px;"></i>
            全员志愿填报管理中心
          </span>
        </div>
        
        <!-- 搜索栏 -->
        <el-form :model="queryParams" ref="queryForm" :inline="true" class="management-query">
          <el-form-item label="学号" prop="studentNo">
            <el-input v-model="queryParams.studentNo" placeholder="请输入学号" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="填报年份" prop="entranceYear">
            <el-input v-model="queryParams.entranceYear" placeholder="年份" clearable size="small" @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <!-- 管理表格 -->
        <el-table v-loading="loading" :data="aspirationList" border stripe class="modern-table">
          <el-table-column label="昵称" prop="studentName" align="center">
            <template slot-scope="scope">
              <span class="student-name-tag">{{ scope.row.studentName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="填报年份" prop="entranceYear" align="center" width="120" />
          <el-table-column label="最后修改时间" align="center" width="200">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.updatedTime || scope.row.createdTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center" width="100">
            <template slot-scope="">
              <el-tag size="mini" type="success">已提交</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="150">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-view" @click="handleShowDetail(scope.row)">方案内容</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" style="color: #F56C6C; margin-left: 10px;" @click="handleRemove(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getListAdmins" />
      </el-card>

      <!-- 详情弹窗 -->
      <el-dialog title="志愿填报内容摘要" :visible.sync="detailVisible" width="500px">
        <div class="summary-container" v-text="aspirationDetail"></div>
        <div slot="footer">
          <el-button type="primary" @click="detailVisible = false">确 定</el-button>
        </div>
      </el-dialog>
    </template>

    <!-- ================= 学生视图：模拟填报方案 ================= -->
    <template v-else>
      <el-card shadow="hover" class="page-card">
        <div slot="header" class="clearfix">
          <span style="font-weight: bold; font-size: 18px; color: #303133;">
            <i class="el-icon-document-copy" style="color: #409EFF; margin-right: 8px;"></i>
            填报中心 - 我的模拟志愿表 (共{{ sheetList.length }}个方案)
          </span>
        </div>

        <div v-loading="loading" class="sheets-list">
          <el-row :gutter="20">
            <el-col :span="24" v-for="sheet in sheetList" :key="sheet.sheetNo" style="margin-bottom: 25px;">
              <el-card shadow="never" :class="['sheet-item-card', sheet.hasData ? 'has-data' : 'no-data']">
                <div class="sheet-header">
                  <div class="header-left">
                    <span class="sheet-no">方案单 {{ sheet.sheetNo }}</span>
                    <el-tag v-if="sheet.hasData" size="mini" type="success" effect="dark" style="margin-left: 10px;">已填报</el-tag>
                  </div>
                  <div class="sheet-actions">
                    <el-button 
                      size="mini" 
                      :type="sheet.hasData ? 'primary' : 'success'" 
                      icon="el-icon-edit"
                      @click="$router.push({path: '/filling-view/apply', query: {sheetNo: sheet.sheetNo}})"
                    >
                      {{ sheet.hasData ? '修改方案' : '立即填报' }}
                    </el-button>
                    <el-button v-if="sheet.hasData" size="mini" type="warning" plain icon="el-icon-download" @click="handleExport(sheet.sheetNo)" style="margin-left: 10px;">导出</el-button>
                    <el-button v-if="sheet.hasData" size="mini" type="danger" plain icon="el-icon-delete" @click="handleRemoveSheet(sheet.sheetNo)" style="margin-left: 10px;">清空</el-button>
                  </div>
                </div>
                
                <div class="sheet-body">
                  <template v-if="sheet.hasData">
                    <div class="college-group-summary">
                      <div v-for="(group, idx) in sheet.details" :key="idx" class="summary-item">
                        <div class="college-name-row"><i class="el-icon-school"></i> {{ group.collegeName }}</div>
                        <div class="professions-list">
                          <el-tag v-for="(pName, pIdx) in group.professions" :key="pIdx" size="mini" type="info" style="margin-right: 8px; margin-bottom: 5px;">{{ pName }}</el-tag>
                        </div>
                      </div>
                    </div>
                  </template>
                  <div v-else class="empty-text">
                    <i class="el-icon-warning-outline"></i> 您在该方案下暂无数据，点击右侧按钮开启模拟填报。
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-card>
    </template>
  </div>
</template>

<script>
import { listSheets, delSheet, listAspiration, aspirationDetail, delAspiration } from "@/api/entrance/aspiration";
import { checkRole } from "@/utils/permission";
import request from '@/utils/request';

export default {
  name: "FillingCenter",
  data() {
    return {
      loading: true,
      // 学生数据
      sheetList: [],
      // 管理员数据
      aspirationList: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        studentNo: undefined,
        entranceYear: undefined
      },
      detailVisible: false,
      aspirationDetail: ""
    };
  },
  created() {
    if (this.checkRole(['admin', 'school_admin'])) {
      this.getListAdmins();
    } else {
      this.getListStudents();
    }
  },
  activated() {
    if (this.checkRole(['admin', 'school_admin'])) {
      this.getListAdmins();
    } else {
      this.getListStudents();
    }
  },
  methods: {
    checkRole,
    /** 管理员获取全量列表 */
    getListAdmins() {
      this.loading = true;
      listAspiration(this.queryParams).then(res => {
        this.aspirationList = res.rows;
        this.total = res.total;
        this.loading = false;
      }).catch(() => { this.loading = false; });
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getListAdmins();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleShowDetail(row) {
      this.loading = true;
      aspirationDetail(row.studentNo).then(res => {
        this.aspirationDetail = res.data;
        this.detailVisible = true;
        this.loading = false;
      }).catch(() => { this.loading = false; });
    },
    /** 管理员删除记录 */
    handleRemove(row) {
      this.$confirm(`确定要彻底删除昵称为 "${row.studentName || '该用户'}" 的填报内容吗？此操作不可撤销！`, "危险警告", {
        confirmButtonText: "确定删除",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.loading = true;
        return delAspiration(row.id);
      }).then(() => {
        this.$message.success("志愿记录已成功删除");
        this.getListAdmins();
      }).catch(() => { this.loading = false; });
    },
    /** 学生获取方案单 */
    getListStudents() {
      this.loading = true;
      listSheets().then(res => {
        this.sheetList = res.data;
        this.loading = false;
      }).catch(() => { this.loading = false; });
    },
    handleExport(sheetNo) {
      window.location.href = process.env.VUE_APP_BASE_API + "/college_entrance/aspiration/export/" + sheetNo;
    },
    handleRemoveSheet(sheetNo) {
      this.$confirm(`确定清空“方案 ${sheetNo}”吗？`, '提示', { type: 'warning' }).then(() => {
        this.loading = true;
        return delSheet(sheetNo);
      }).then(() => {
        this.$message.success("清空成功");
        this.getListStudents();
      }).catch(() => { this.loading = false; });
    }
  }
};
</script>

<style scoped>
/* 管理端样式 */
.management-title { font-weight: bold; font-size: 20px; color: #1f2d3d; }
.management-query { margin-bottom: 20px; background: #fdfdfd; padding: 15px; border-radius: 8px; border: 1px solid #ebeef5; }
.modern-table { border-radius: 8px; overflow: hidden; }
.student-name-tag { font-weight: bold; color: #409EFF; }
.summary-container { white-space: pre-line; line-height: 1.8; color: #606266; padding: 15px; background: #f8f9fa; border-radius: 8px; border: 1px solid #e9ecef; }

/* 学生端样式 */
.sheet-item-card { border-radius: 12px; transition: all 0.3s; margin-bottom: 10px; }
.sheet-item-card.has-data { background: #fcfdfe; border-left: 6px solid #0974e7; }
.sheet-item-card.no-data { background: #fafafa; border-left: 6px solid #e6ebf1; opacity: 0.8; }
.sheet-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 15px; padding-bottom: 12px; border-bottom: 1px solid #eff2f7; }
.sheet-no { font-weight: bold; font-size: 18px; color: #1f2d3d; }
.college-group-summary { display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 15px; }
.summary-item { background: #fff; border: 1px solid #f0f2f5; border-radius: 8px; padding: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.02); }
.college-name-row { font-weight: bold; color: #333; margin-bottom: 10px; font-size: 15px; }
.professions-list { display: flex; flex-wrap: wrap; }
.empty-text { color: #adb5bd; font-size: 14px; padding: 30px 0; text-align: center; font-style: italic; }
</style>
