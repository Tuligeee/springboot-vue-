<template>
  <div class="app-container">
    <el-card shadow="hover" class="page-card">
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; font-size: 18px; color: #303133;">
          <i class="el-icon-collection" style="color: #409EFF; margin-right: 8px;"></i>
          专业查询中心
        </span>
      </div>

      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="院校名称" prop="collegeNo">
          <el-select
            v-model="queryParams.collegeNo"
            filterable
            remote
            clearable
            reserve-keyword
            placeholder="输入院校名称搜索"
            :remote-method="searchCollege"
            :loading="collegeSearchLoading"
            size="small"
            style="width: 240px;"
            @clear="handleQuery"
          >
            <el-option
              v-for="c in collegeOptions"
              :key="c.collegeNo"
              :label="c.collegeName"
              :value="c.collegeNo"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="专业名称" prop="professionName">
          <el-input v-model="queryParams.professionName" placeholder="请输入专业名称" clearable size="small" @keyup.enter.native="handleQuery" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
            v-if="checkRole(['admin', 'school_admin'])"
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
          >新增专业</el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="professionList">
        <el-table-column label="ID" align="center" prop="id" width="80" />
        <el-table-column label="院校名称" align="center" prop="collegeName" />
        <el-table-column label="专业编号" align="center" prop="professionNo" width="120" />
        <el-table-column label="专业名称" align="center" prop="professionName" />
        <el-table-column label="修业年限" align="center" prop="studyYear" width="100">
          <template slot-scope="scope">
            <el-tag size="small">{{ scope.row.studyYear }}年</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              v-if="checkRole(['admin', 'school_admin'])"
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >修改</el-button>
            <el-button
              v-if="checkRole(['admin', 'school_admin'])"
              size="mini"
              type="text"
              icon="el-icon-delete"
              class="text-danger"
              @click="handleDelete(scope.row)"
            >删除</el-button>
            <el-button
              v-if="checkRole(['student', 'common'])"
              size="mini"
              type="text"
              icon="el-icon-edit-outline"
              @click="handleQuickApply(scope.row)"
            >立即填报</el-button>
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

    <!-- 添加或修改专业对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="所属院校" prop="collegeNo">
          <el-input v-model="form.collegeNo" placeholder="请输入院校代码" :disabled="checkRole(['school_admin'])" />
        </el-form-item>
        <el-form-item label="专业编号" prop="professionNo">
          <el-input v-model="form.professionNo" placeholder="请输入专业编号" />
        </el-form-item>
        <el-form-item label="专业名称" prop="professionName">
          <el-input v-model="form.professionName" placeholder="请输入专业名称" />
        </el-form-item>
        <el-form-item label="修业年限" prop="studyYear">
          <el-input-number v-model="form.studyYear" :min="1" :max="10" />
        </el-form-item>
        <el-form-item label="详细介绍" prop="detailInfo">
          <el-input v-model="form.detailInfo" type="textarea" :rows="4" placeholder="请输入专业介绍" />
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
import { listProfession, getProfession, delProfession, addProfession, updateProfession } from "@/api/entrance/profession";
import { listCollege } from "@/api/entrance/college";
import { checkRole } from "@/utils/permission";

export default {
  name: "Profession",
  data() {
    return {
      loading: true,
      professionList: [],
      total: 0,
      showSearch: true,
      open: false,
      title: "",
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        collegeNo: null,
        professionName: null
      },
      form: {},
      rules: {
        collegeNo: [{ required: true, message: "院校编号不能为空", trigger: "blur" }],
        professionNo: [{ required: true, message: "专业编号不能为空", trigger: "blur" }],
        professionName: [{ required: true, message: "专业名称不能为空", trigger: "blur" }]
      },
      collegeOptions: [],
      collegeSearchLoading: false
    };
  },
  created() {
    this.getList();
  },
  methods: {
    checkRole,
    /** 远程搜索院校 */
    searchCollege(query) {
      if (query.length < 1) return;
      this.collegeSearchLoading = true;
      listCollege({ collegeName: query, pageSize: 20 }).then(res => {
        this.collegeOptions = res.rows || [];
        this.collegeSearchLoading = false;
      }).catch(() => { this.collegeSearchLoading = false; });
    },
    getList() {
      this.loading = true;
      listProfession(this.queryParams).then(response => {
        this.professionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    handleQuickApply(row) {
      this.$router.push({
        path: '/filling-view/apply',
        query: { collegeNo: row.collegeNo, professionNo: row.professionNo }
      });
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加专业计划";
    },
    handleUpdate(row) {
      this.reset();
      getProfession(row.id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改专业信息";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateProfession(this.form).then(() => {
              this.$message.success("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addProfession(this.form).then(() => {
              this.$message.success("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      this.$confirm('是否确认删除该专业数据？', "警告", {
        type: "warning"
      }).then(() => {
        return delProfession(row.id);
      }).then(() => {
        this.getList();
        this.$message.success("删除成功");
      }).catch(() => {});
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        id: null,
        collegeNo: null,
        professionNo: null,
        professionName: null,
        studyYear: 4,
        detailInfo: null
      };
      this.resetForm("form");
    }
  }
};
</script>
