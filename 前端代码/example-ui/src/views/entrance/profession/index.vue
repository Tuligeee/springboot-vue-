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
        <el-form-item label="院校名称" prop="collegeName">
          <el-select
            v-model="queryParams.collegeName"
            filterable
            remote
            clearable
            reserve-keyword
            placeholder="输入院校名称搜索并选择"
            :remote-method="searchCollegeQuery"
            :loading="collegeQuerySearchLoading"
            size="small"
            style="width: 260px;"
            @clear="handleQuery"
          >
            <el-option
              v-for="c in collegeQueryOptions"
              :key="c.id"
              :label="c.collegeName"
              :value="c.collegeName"
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
        <el-table-column label="院校名称" align="center" prop="collegeName" min-width="160" />
        <el-table-column label="专业名称" align="center" prop="professionName" min-width="200" />
        <el-table-column label="修业年限" align="center" prop="studyYear" width="100">
          <template slot-scope="scope">
            <el-tag size="small">{{ scope.row.studyYear }}年</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="选科要求" align="center" prop="subjectRequirement" min-width="150">
          <template slot-scope="scope">
            <el-tag :type="scope.row.subjectRequirement === '不提科目要求' || !scope.row.subjectRequirement ? 'info' : 'warning'" size="small">
              {{ scope.row.subjectRequirement || '不提科目要求' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
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

    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="dynamicRules" label-width="100px">
        <el-form-item v-if="!checkRole(['school_admin'])" label="所属院校" prop="collegeId">
          <el-select
            v-model="form.collegeId"
            filterable
            remote
            clearable
            reserve-keyword
            placeholder="输入院校名称搜索"
            :remote-method="searchCollegeForm"
            :loading="collegeFormSearchLoading"
            style="width: 100%"
          >
            <el-option
              v-for="c in collegeFormOptions"
              :key="c.id"
              :label="c.collegeName"
              :value="c.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="专业名称" prop="professionName">
          <el-input v-model="form.professionName" placeholder="请输入专业名称" />
        </el-form-item>
        <el-form-item label="修业年限" prop="studyYear">
          <el-input-number v-model="form.studyYear" :min="1" :max="10" />
        </el-form-item>
        <el-form-item label="选科要求" prop="subjectRequirement">
          <el-input v-model="form.subjectRequirement" placeholder="如：物理,化学,生物 或 不提科目要求" />
          <div style="font-size:12px;color:#999;line-height:14px;margin-top:5px;">请用逗号(英文或中文均可但不建议只用中文逗号，请后台格式统一)将限制隔开，若无限制可填【不提科目要求】</div>
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
import { listCollege, getCollege } from "@/api/entrance/college";
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
        collegeName: null,
        professionName: null
      },
      form: {},
      collegeQueryOptions: [],
      collegeQuerySearchLoading: false,
      collegeFormOptions: [],
      collegeFormSearchLoading: false
    };
  },
  created() {
    if (this.$route.query.collegeName) {
      this.queryParams.collegeName = this.$route.query.collegeName;
    }
    this.getList();
  },
  computed: {
    dynamicRules() {
      const r = {
        professionName: [{ required: true, message: "专业名称不能为空", trigger: "blur" }]
      };
      if (!checkRole(["school_admin"])) {
        r.collegeId = [{ required: true, message: "请选择所属院校", trigger: "change" }];
      }
      return r;
    }
  },
  methods: {
    checkRole,
    searchCollegeQuery(query) {
      if (!query || query.length < 1) return;
      this.collegeQuerySearchLoading = true;
      listCollege({ collegeName: query, pageSize: 20 }).then(res => {
        this.collegeQueryOptions = res.rows || [];
        this.collegeQuerySearchLoading = false;
      }).catch(() => { this.collegeQuerySearchLoading = false; });
    },
    searchCollegeForm(query) {
      if (!query || query.length < 1) return;
      this.collegeFormSearchLoading = true;
      listCollege({ collegeName: query, pageSize: 20 }).then(res => {
        this.collegeFormOptions = res.rows || [];
        this.collegeFormSearchLoading = false;
      }).catch(() => { this.collegeFormSearchLoading = false; });
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
      this.collegeQueryOptions = [];
      this.handleQuery();
    },
    handleQuickApply(row) {
      this.$router.push({
        path: "/filling-view/apply",
        query: { professionId: row.id, sheetNo: this.$route.query.sheetNo || 1 }
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
        if (this.form.collegeId) {
          getCollege(this.form.collegeId).then(res => {
            const c = res.data;
            if (c) {
              this.collegeFormOptions = [{ id: c.id, collegeName: c.collegeName }];
            }
          });
        }
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
      this.$confirm("是否确认删除该专业数据？", "警告", {
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
        collegeId: null,
        professionName: null,
        studyYear: 4,
        subjectRequirement: '不提科目要求',
        detailInfo: null,
        personCount: null
      };
      this.collegeFormOptions = [];
      this.resetForm("form");
    }
  }
};
</script>
