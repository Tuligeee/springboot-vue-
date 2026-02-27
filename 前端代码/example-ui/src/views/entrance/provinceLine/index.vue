<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="年份" prop="year">
        <el-input
            v-model="queryParams.year"
            placeholder="请输入年份"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="省份" prop="province">
        <el-input
            v-model="queryParams.province"
            placeholder="请输入省份"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="批次" prop="batchName">
        <el-select v-model="queryParams.batchName" placeholder="请选择批次" clearable size="small">
          <el-option label="本科批" value="本科批" />
          <el-option label="本科一批" value="本科一批" />
          <el-option label="本科二批" value="本科二批" />
          <el-option label="专科批" value="专科批" />
          <el-option label="特控线" value="特控线" />
        </el-select>
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
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="provinceLineList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" width="60" />

      <el-table-column label="年份" align="center" prop="year">
        <template slot-scope="scope">
          <el-tag effect="plain">{{ scope.row.year }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="省份" align="center" prop="province" />

      <el-table-column label="批次名称" align="center" prop="batchName">
        <template slot-scope="scope">
          <el-tag type="success">{{ scope.row.batchName }}</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="分数线" align="center" prop="score">
        <template slot-scope="scope">
          <span style="font-weight: bold; color: #ff4949; font-size: 16px;">{{ scope.row.score }}</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              class="text-danger"
              @click="handleDelete(scope.row)"
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

    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="年份" prop="year">
          <el-input-number v-model="form.year" :min="2000" :max="2030" label="年份" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="省份" prop="province">
          <el-input v-model="form.province" placeholder="例如：湖北" />
        </el-form-item>
        <el-form-item label="批次" prop="batchName">
          <el-select v-model="form.batchName" placeholder="请选择批次" style="width: 100%">
            <el-option label="本科批" value="本科批" />
            <el-option label="本科一批" value="本科一批" />
            <el-option label="本科二批" value="本科二批" />
            <el-option label="专科批" value="专科批" />
            <el-option label="特控线" value="特控线" />
          </el-select>
        </el-form-item>
        <el-form-item label="分数线" prop="score">
          <el-input-number v-model="form.score" :min="100" :max="750" label="分数" style="width: 100%"></el-input-number>
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
        year: [
          { required: true, message: "年份不能为空", trigger: "blur" }
        ],
        province: [
          { required: true, message: "省份不能为空", trigger: "blur" }
        ],
        batchName: [
          { required: true, message: "批次名称不能为空", trigger: "change" }
        ],
        score: [
          { required: true, message: "分数线不能为空", trigger: "blur" }
        ]
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
        // 若依标准返回是 rows 和 total
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
        year: new Date().getFullYear(), // 默认当前年份
        province: '湖北', // 默认湖北
        batchName: null,
        score: null
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
      this.title = "添加档线信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getProvinceLine(id).then(response => {
        // 如果后端返回的是 Response<T>，数据通常在 response.data 里
        // 若依标准生成代码通常直接返回 data
        this.form = response.data;
        this.open = true;
        this.title = "修改档线信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateProvinceLine(this.form).then(response => {
              this.$message.success("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProvinceLine(this.form).then(response => {
              this.$message.success("新增成功");
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
      this.$confirm('是否确认删除数据项？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return delProvinceLine(ids);
      }).then(() => {
        this.getList();
        this.$message.success("删除成功");
      }).catch(() => {});
    }
  }
};
</script>
