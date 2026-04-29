<template>
  <div class="app-container">
    <el-card shadow="hover" class="page-card">
      <template slot="header">
        <span class="page-title">
          <i class="el-icon-school" style="color: #409EFF; margin-right: 8px;"></i>
          院校查询
        </span>
      </template>
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
      <el-form-item label="办学层次" prop="educationLevel">
        <el-select v-model="queryParams.educationLevel" placeholder="请选择层级" clearable size="small" style="width: 130px;">
          <el-option
            v-for="item in uniqueEducationLevels"
            :key="item"
            :label="item"
            :value="item"
          />
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
        :on-error="handleFileError"
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
      
      <!-- 进度条展示 -->
      <div v-if="upload.isUploading" style="margin-top: 20px;">
        <div style="margin-bottom: 10px; font-size: 14px; color: #606266;">
          正在处理数据，请勿关闭窗口... ({{ upload.progress }}%)
        </div>
        <el-progress :percentage="upload.progress" :stroke-width="16" striped striped-flow :duration="10" />
      </div>

      <!-- 异常情况详情展示 -->
      <div v-if="upload.showErrors && upload.errorList && upload.errorList.length > 0" style="margin-top: 20px;">
        <el-divider content-position="left">错误详情 (前50条)</el-divider>
        <div style="max-height: 200px; overflow-y: auto; background: #FFF5F5; border: 1px solid #FFD1D1; padding: 10px; border-radius: 4px;">
          <div v-for="(err, index) in upload.errorList.slice(0, 50)" :key="index" style="color: #F56C6C; font-size: 12px; margin-bottom: 5px;">
             {{ index + 1 }}. {{ err }}
          </div>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm" :loading="upload.isUploading">确 定</el-button>
        <el-button @click="closeUploadDialog" :disabled="upload.isUploading">取 消</el-button>
      </div>
    </el-dialog>

    <el-table v-loading="loading" :data="collegeList">
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

      <el-table-column label="所在城市" align="center" prop="city" />
      <el-table-column label="办学层次" align="center" prop="educationLevel">
        <template slot-scope="scope">
          <el-tag :type="scope.row.educationLevel === '本科' ? 'success' : 'warning'" v-if="scope.row.educationLevel">
            {{ scope.row.educationLevel }}
          </el-tag>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="全国排名" align="center" prop="ranking" sortable />

      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleDetail(scope.row.id)"
          >查看详情</el-button>
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
        <el-form-item label="所在城市" prop="city">
          <el-input v-model="form.city" placeholder="例如：北京市" />
        </el-form-item>
        <el-form-item label="办学层次" prop="educationLevel">
          <el-select
            v-model="form.educationLevel"
            placeholder="请选择或输入"
            filterable
            allow-create
            default-first-option
            style="width: 100%"
          >
            <el-option
              v-for="item in uniqueEducationLevels"
              :key="item"
              :label="item"
              :value="item"
            />
          </el-select>
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
import { listCollege, getCollege, delCollege, addCollege, updateCollege, getUniqueEducationLevels } from "@/api/entrance/college";
import { checkRole } from "@/utils/permission";
import { getToken } from "@/utils/auth";

export default {
  name: "College",
  data() {
    return {
      // 数据库中实际存在的办学层次列表
      uniqueEducationLevels: [],
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
        url: process.env.VUE_APP_BASE_API + "/college_entrance/college/importData",
        progress: 0,
        taskId: null,
        timer: null,
        errorList: [],
        showErrors: false
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        collegeName: null,
        city: null,
        educationLevel: null
      },
      form: {},
      rules: {
        collegeName: [{ required: true, message: "院校名称不能为空", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
    this.getUniqueLevels();
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
      this.getUniqueLevels();
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
        city: null,
        educationLevel: '本科',
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
      if (response.code === 200) {
        this.upload.taskId = response.data; // 这里的 data 是 taskId
        this.upload.progress = 0;
        this.upload.isUploading = true;
        // 开始轮询进度
        this.startPollingProgress();
      } else {
        this.upload.isUploading = false;
        this.$message.error(response.msg || "文件解析失败");
        this.$refs.upload.clearFiles();
      }
    },
    /** 轮询任务进度 */
    startPollingProgress() {
      this.upload.timer = setInterval(() => {
        request({
          url: '/college_entrance/college/importProgress/' + this.upload.taskId,
          method: 'get'
        }).then(res => {
          const { progress, result, finished, errorList } = res.data;
          this.upload.progress = progress || 0;
          this.upload.errorList = errorList || [];
          
          if (finished) {
            this.stopPollingProgress();
            this.upload.isUploading = false;
            if (this.upload.errorList.length > 0) {
                this.upload.showErrors = true;
            }
            this.$refs.upload.clearFiles();
            
            this.$confirm("导入任务处理完成，是否立即查看结果详情？", "处理完毕", {
                confirmButtonText: '查看结果',
                cancelButtonText: '关闭',
                type: (this.upload.errorList.length > 0) ? 'warning' : 'success'
            }).then(() => {
                this.$alert("<div style='overflow: auto;max-height: 40vh;color:#333;'>" + result + "</div>", "结果概览", { dangerouslyUseHTMLString: true });
            });
            
            this.getList();
            this.getUniqueLevels();
          }
        }).catch(() => {
          this.stopPollingProgress();
          this.upload.isUploading = false;
        });
      }, 1500); // 每 1.5 秒查一次
    },
    /** 关闭上传弹窗并清理 */
    closeUploadDialog() {
       this.upload.open = false;
       this.upload.showErrors = false;
       this.upload.errorList = [];
       this.stopPollingProgress();
    },
    stopPollingProgress() {
      if (this.upload.timer) {
        clearInterval(this.upload.timer);
        this.upload.timer = null;
      }
    },
    // 文件上传失败处理
    handleFileError(err, file, fileList) {
      this.upload.isUploading = false;
      let errMsg = "文件上传失败，请检查网络或联系管理员。";
      try {
        // el-upload 的 err 对象通常包含响应内容
        const response = JSON.parse(err.message);
        if (response && response.msg) errMsg = response.msg;
      } catch (e) {
        console.error("解析上传错误失败", err);
      }
      this.$message.error(errMsg);
    },
    // 提交上传文件
    submitFileForm() {
      this.upload.isUploading = true;
      this.$refs.upload.submit();
    },
    /** 获取实时去重的办学层次 */
    getUniqueLevels() {
      getUniqueEducationLevels().then(res => {
        this.uniqueEducationLevels = res.data;
      });
    }
  }
};
</script>
