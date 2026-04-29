<template>
  <div class="app-container">
    <el-card shadow="hover" class="page-card">
      <template slot="header">
        <span class="page-title">
          <i class="el-icon-guide" style="color: #409EFF; margin-right: 8px;"></i>
          省控批次线管理
        </span>
      </template>

      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="年份" prop="year">
          <el-input
            v-model="queryParams.year"
            placeholder="请输入年份"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="省份" prop="province">
          <el-input
            v-model="queryParams.province"
            placeholder="请输入省份"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="批次" prop="batchName">
          <el-input
            v-model="queryParams.batchName"
            placeholder="请输入批次名称"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['entrance:provinceLine:add']"
          >新增</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="success"
            plain
            icon="el-icon-edit"
            size="mini"
            :disabled="single"
            @click="handleUpdate"
            v-hasPermi="['entrance:provinceLine:edit']"
          >修改</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="danger"
            plain
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['entrance:provinceLine:remove']"
          >删除</el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="provinceLineList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="ID" align="center" prop="id" />
        <el-table-column label="年份" align="center" prop="year" />
        <el-table-column label="省份" align="center" prop="province" />
        <el-table-column label="批次" align="center" prop="batchName" />
        <el-table-column label="控制分数线" align="center" prop="score" />
        <el-table-column label="科类" align="center" prop="category" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['entrance:provinceLine:edit']"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['entrance:provinceLine:remove']"
            >删除</el-button>
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
    </el-card>

    <!-- 添加或修改档线对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="年份" prop="year">
          <el-input v-model="form.year" placeholder="请输入年份" />
        </el-form-item>
        <el-form-item label="省份" prop="province">
          <el-input v-model="form.province" placeholder="请输入省份" />
        </el-form-item>
        <el-form-item label="批次" prop="batchName">
          <el-input v-model="form.batchName" placeholder="请输入批次" />
        </el-form-item>
        <el-form-item label="分数线" prop="score">
          <el-input-number v-model="form.score" placeholder="请输入分数" :min="0" :max="900" />
        </el-form-item>
        <el-form-item label="科类" prop="category">
          <el-select v-model="form.category" placeholder="请选择科类">
            <el-option label="理科" value="理科" />
            <el-option label="文科" value="文科" />
            <el-option label="综合" value="综合" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listProvinceLine, getProvinceLine, delProvinceLine, addProvinceLine, updateProvinceLine } from "@/api/entrance/provinceLine";

export default {
  name: "ProvinceLine",
  data() {
    return {
      // 遮罩层
      loading: true,
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
      // 档线表格数据
      provinceLineList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        year: null,
        province: null,
        batchName: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        year: [{ required: true, message: "年份不能为空", trigger: "blur" }],
        province: [{ required: true, message: "省份不能为空", trigger: "blur" }],
        batchName: [{ required: true, message: "批次集不能为空", trigger: "blur" }],
        score: [{ required: true, message: "分数线不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询档线列表 */
    getList() {
      this.loading = true;
      listProvinceLine(this.queryParams).then(response => {
        this.provinceLineList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        year: new Date().getFullYear(),
        province: null,
        batchName: null,
        score: null,
        category: '理科'
      };
      this.resetForm("form");
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加省控档线";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getProvinceLine(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改省控档线";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateProvinceLine(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProvinceLine(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除档线编号为"' + ids + '"的数据项？').then(function() {
        return delProvinceLine(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    }
  }
};
</script>
