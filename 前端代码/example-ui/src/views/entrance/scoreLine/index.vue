<template>
  <div class="app-container">
    <el-card shadow="hover" class="page-card">
      <template slot="header">
        <span class="page-title">
          <i class="el-icon-data-line" style="color: #409EFF; margin-right: 8px;"></i>
          专业录取分数线查询
        </span>
      </template>

      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="院校代码" prop="collegeNo" v-if="isAdmin">
          <el-input
            v-model="queryParams.collegeNo"
            placeholder="请输入院校代码"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="专业代码" prop="professionNo">
          <el-input
            v-model="queryParams.professionNo"
            placeholder="请输入专业代码"
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item label="年份" prop="year">
          <el-input
            v-model="queryParams.year"
            placeholder="请输入年份"
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
            v-hasPermi="['entrance:scoreLine:add']"
          >新增</el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="scoreLineList">
        <el-table-column label="ID" align="center" prop="id" />
        <el-table-column label="院校代码" align="center" prop="collegeNo" />
        <el-table-column label="专业代码" align="center" prop="professionNo" />
        <el-table-column label="分数线" align="center" prop="score" />
        <el-table-column label="年份" align="center" prop="year" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['entrance:scoreLine:edit']"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['entrance:scoreLine:remove']"
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

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="院校代码" prop="collegeNo" v-if="isAdmin">
          <el-input v-model="form.collegeNo" placeholder="请输入院校代码" />
        </el-form-item>
        <el-form-item label="专业代码" prop="professionNo">
          <el-input v-model="form.professionNo" placeholder="请输入专业代码" />
        </el-form-item>
        <el-form-item label="分数线" prop="score">
          <el-input-number v-model="form.score" :min="0" :max="750" />
        </el-form-item>
        <el-form-item label="年份" prop="year">
          <el-input v-model="form.year" placeholder="请输入年份" />
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
import { listScoreLine, getScoreLine, delScoreLine, addScoreLine, updateScoreLine } from "@/api/entrance/scoreLine";
import { mapGetters } from 'vuex'

export default {
  name: "ScoreLine",
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      scoreLineList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        collegeNo: undefined,
        professionNo: undefined,
        year: undefined
      },
      form: {},
      rules: {
        collegeNo: [{ required: true, message: "院校代码不能为空", trigger: "blur" }],
        professionNo: [{ required: true, message: "专业代码不能为空", trigger: "blur" }],
        score: [{ required: true, message: "分数线不能为空", trigger: "blur" }],
        year: [{ required: true, message: "年份不能为空", trigger: "blur" }]
      }
    };
  },
  computed: {
    ...mapGetters(['roles']),
    isAdmin() {
      return this.roles.includes('admin')
    }
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listScoreLine(this.queryParams).then(response => {
        this.scoreLineList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        id: undefined,
        collegeNo: undefined,
        professionNo: undefined,
        score: undefined,
        year: new Date().getFullYear()
      };
      this.resetForm("form");
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加专业录取分数线";
    },
    handleUpdate(row) {
      this.reset();
      const id = row.id;
      getScoreLine(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改专业录取分数线";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateScoreLine(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addScoreLine(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const ids = row.id;
      this.$modal.confirm('是否确认删除录取分数线编号为"' + ids + '"的数据项？').then(function() {
        return delScoreLine(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    }
  }
};
</script>
