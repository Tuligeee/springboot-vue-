<template>
  <div class="app-container">
    <el-card shadow="hover" class="page-card">
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; font-size: 18px; color: #303133;">
          <i class="el-icon-s-order" style="color: #409EFF; margin-right: 8px;"></i>
          志愿管理
        </span>
      </div>
    <el-form :model="queryParams" ref="queryForm" v-show="showSearch" :inline="true">
      <el-form-item label="学号" prop="studentNo">
        <el-input
            v-model="queryParams.studentNo"
            placeholder="请输入学号"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="填报年份" prop="entranceYear">
        <el-input
            v-model="queryParams.entranceYear"
            placeholder="请输入填报年份"
            clearable
            size="small"
            style="width: 150px"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="aspirationList">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="ID" prop="id" width="100"/>
      <el-table-column label="昵称" prop="studentName" :show-overflow-tooltip="true" width="200"/>
      <el-table-column label="填报年份" prop="entranceYear" :show-overflow-tooltip="true" width="200"/>
      <el-table-column label="创建时间" align="center" prop="createdTime" width="200">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createdTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="getDetail(scope.row)"
              v-hasPermi="['entrance:aspiration:index']"
          >填报详情
          </el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              style="color: #F56C6C; margin-left: 10px;"
              @click="handleDelete(scope.row)"
              v-hasPermi="['entrance:aspiration:index']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
    />

    <el-dialog
        title="志愿填报详情摘要"
        :visible.sync="dialogVisible"
        width="450px"
        custom-class="aspiration-dialog">
      <div v-text="aspirationDetail" class="detail-summary"></div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>

      </el-card>
  </div>
</template>

<script>

import {aspirationDetail, listAspiration, delAspiration} from "@/api/entrance/aspiration";

export default {
  name: "Aspiration",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 志愿表格数据
      aspirationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        studentNo: undefined,
        studentName: undefined,
        entranceYear: undefined
      },
      // 表单参数
      form: {},
      dialogVisible: false,
      aspirationDetail:"",
      defaultProps: {
        children: "children",
        label: "label"
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询志愿列表 */
    getList() {
      this.loading = true;
      listAspiration(this.queryParams).then(
          response => {
            this.aspirationList = response.rows;
            this.total = response.total;
            this.loading = false;
          }
      );
    },
    //填报详情
    getDetail(row) {
      aspirationDetail(row.studentNo).then(
          response => {
            this.dialogVisible = true;
            this.aspirationDetail = response.data;
          }
      );
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 删除记录 */
    handleDelete(row) {
      this.$confirm('确定要永久删除该学生的志愿填报记录吗？删除后不可恢复！', "严重警告", {
        confirmButtonText: "确定删除",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.loading = true;
        return delAspiration(row.id);
      }).then(() => {
        this.$message.success("删除成功");
        this.getList();
      }).catch(() => {
        this.loading = false;
      });
    }
  }
};
</script>

<style scoped>
.detail-summary {
  white-space: pre-line;
  line-height: 1.8;
  font-size: 15px;
  color: #606266;
  padding: 15px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}
</style>
