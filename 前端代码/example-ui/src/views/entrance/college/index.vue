<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="院校名称" prop="collegeName">
        <el-input
            v-model="queryParams.collegeName"
            placeholder="请输入院校名称"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所在城市" prop="city">
        <el-input
            v-model="queryParams.city"
            placeholder="请输入城市"
            clearable
            size="small"
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

    <el-table v-loading="loading" :data="collegeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="ID" align="center" prop="id" width="60" />

      <el-table-column label="院校名称" align="center" prop="collegeName">
        <template slot-scope="scope">
          <span
              style="color: #409EFF; cursor: pointer; text-decoration: underline;"
              @click="handleDetail(scope.row.id)"
          >
            {{ scope.row.collegeName }}
          </span>
        </template>
      </el-table-column>

      <el-table-column label="院校代码" align="center" prop="collegeNo" />
      <el-table-column label="所在城市" align="center" prop="city" />
      <el-table-column label="排名" align="center" prop="ranking" />
      <el-table-column label="招生人数" align="center" prop="personCount" />

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleDetail(scope.row.id)"
          >详情</el-button>
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

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="院校名称" prop="collegeName">
          <el-input v-model="form.collegeName" placeholder="请输入院校名称" />
        </el-form-item>
        <el-form-item label="院校代码" prop="collegeNo">
          <el-input v-model="form.collegeNo" placeholder="请输入院校代码" />
        </el-form-item>
        <el-form-item label="所在城市" prop="city">
          <el-input v-model="form.city" placeholder="例如：北京市" />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="排名" prop="ranking">
              <el-input-number v-model="form.ranking" :min="1" label="排名"></el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="招生人数" prop="personCount">
              <el-input-number v-model="form.personCount" :min="0" label="人数"></el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="详细信息" prop="detailInfo">
          <el-input v-model="form.detailInfo" type="textarea" :rows="4" placeholder="请输入院校简介" />
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
import { listCollege, getCollege, delCollege, addCollege, updateCollege } from "@/api/entrance/college";

export default {
  name: "College",
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
      // 院校表格数据
      collegeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        collegeName: null, // 修正为 collegeName
        city: null         // 修正为 city
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        collegeName: [
          { required: true, message: "院校名称不能为空", trigger: "blur" }
        ],
        collegeNo: [
          { required: true, message: "院校代码不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询院校列表 */
    getList() {
      this.loading = true;
      listCollege(this.queryParams).then(response => {
        this.collegeList = response.rows;
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
        collegeName: null, // 修正
        collegeNo: null,   // 修正
        city: null,        // 修正
        ranking: 0,        // 新增
        personCount: 0,    // 新增
        detailInfo: null   // 修正
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
      this.title = "添加院校";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getCollege(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改院校";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCollege(this.form).then(response => {
              this.$message.success("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCollege(this.form).then(response => {
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
      this.$confirm('是否确认删除院校ID为"' + ids + '"的数据项？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return delCollege(ids);
      }).then(() => {
        this.getList();
        this.$message.success("删除成功");
      }).catch(() => {});
    },
    /** 跳转到详情页 */
    handleDetail(id) {
      this.$router.push('/college/detail/' + id);
    }
  }
};
</script>
