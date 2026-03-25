<template>
  <div class="app-container">
    <el-card shadow="hover" class="page-card">
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; font-size: 18px; color: #303133;">
          <i class="el-icon-school" style="color: #409EFF; margin-right: 8px;"></i>
          院校查询
        </span>
      </div>
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
            v-hasRole="['admin']"
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
        >新增院校</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 院校导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" style="margin-bottom: 10px;">
            <el-checkbox v-model="upload.updateSupport" /> 是否更新已经存在的院校数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <el-table v-loading="loading" :data="collegeList">
      <el-table-column label="ID" align="center" prop="id" width="60" />

      <el-table-column label="院校名称" align="center" prop="collegeName">
        <template slot-scope="scope">
          <span
              style="color: #409EFF; cursor: pointer; text-decoration: underline; font-weight: bold;"
              @click="handleDetail(scope.row.id)"
          >
            {{ scope.row.collegeName }}
          </span>
        </template>
      </el-table-column>

      <el-table-column label="院校代码" align="center" prop="collegeNo" />
      <el-table-column label="所在城市" align="center" prop="city" />
      <el-table-column label="全国排名" align="center" prop="ranking" sortable />
      <el-table-column label="招生人数" align="center" prop="personCount" />

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleDetail(scope.row.id)"
          >查看详情</el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-if="checkRole(['admin', 'school_admin'])"
          >编辑修改</el-button>
          <el-button
              v-hasRole="['admin']"
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

    <!-- 新增/修改院校对话框 -->
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
      </el-card>
  </div>
</template>

<script>
import request from '@/utils/request'
import { listCollege, getCollege, delCollege, addCollege, updateCollege } from "@/api/entrance/college";
import { checkRole } from "@/utils/permission";
import { getToken } from "@/utils/auth";

export default {
  name: "College",
  data() {
    return {
      loading: true,
      collegeList: [],
      total: 0,
      showSearch: true,
      open: false,
      title: "",
      upload: {
        open: false,
        title: "",
        isUploading: false,
        updateSupport: 0,
        headers: { Authorization: "Bearer " + getToken() },
        url: process.env.VUE_APP_BASE_API + "/college_entrance/college/importData"
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        collegeName: null,
        city: null
      },
      form: {},
      rules: {
        collegeName: [{ required: true, message: "院校名称不能为空", trigger: "blur" }],
        collegeNo: [{ required: true, message: "院校代码不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    checkRole,
    getList() {
      this.loading = true;
      listCollege(this.queryParams).then(response => {
        this.collegeList = response.rows;
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
    handleDetail(id) {
      this.$router.push('/college-view/detail/' + id);
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加院校";
    },
    handleUpdate(row) {
      this.reset();
      getCollege(row.id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改院校信息";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateCollege(this.form).then(() => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCollege(this.form).then(() => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      this.$confirm('是否确认删除该院校数据项？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        return delCollege(row.id);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      }).catch(() => {});
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    reset() {
      this.form = {
        id: null,
        collegeName: null,
        collegeNo: null,
        city: null,
        ranking: 1,
        personCount: 0,
        detailInfo: null
      };
      this.resetForm("form");
    },
    /** 导出按钮操作 - 修正版实现 */
    handleExport() {
      this.loading = true;
      request({
        url: '/college_entrance/college/export',
        method: 'get',
        params: this.queryParams
      }).then(res => {
        if (res.msg) {
          this.executeDownload(res.msg);
        } else {
          this.msgError("导出失败：未返回文件名");
        }
        this.loading = false;
      }).catch(() => {
        this.loading = false;
      });
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "院校数据导入";
      this.upload.open = true;
    },
    /** 下载模板操作 - 修正版实现 */
    importTemplate() {
      request({
        url: '/college_entrance/college/importTemplate',
        method: 'get'
      }).then(res => {
        if (res.msg) {
          this.executeDownload(res.msg);
        } else {
          this.msgError("获取模板失败");
        }
      });
    },
    /** 通用下载执行器 */
    executeDownload(fileName) {
      window.location.href = process.env.VUE_APP_BASE_API + "/common/download?fileName=" + encodeURIComponent(fileName) + "&delete=true";
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    }
  }
};
</script>
