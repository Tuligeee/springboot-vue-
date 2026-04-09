<template>
  <div class="app-container">
    <el-card shadow="hover" class="page-card">
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; font-size: 18px; color: #303133;">
          <i class="el-icon-news" style="color: #409EFF; margin-right: 8px;"></i>
          {{ isAdmin ? '政策资讯管理' : '最新高考政策资讯' }}
        </span>
      </div>
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="资讯标题" prop="title">
        <el-input
            v-model="queryParams.title"
            placeholder="请输入资讯标题"
            clearable
            size="small"
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="资讯类型" prop="type">
        <el-select v-model="queryParams.type" placeholder="请选择类型" clearable size="small">
          <el-option label="高考政策" value="1" />
          <el-option label="报考指南" value="2" />
          <el-option label="院校动态" value="3" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8" v-if="isAdmin">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
        >发布资讯</el-button>
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

    <el-table v-loading="loading" :data="newsList" @selection-change="handleSelectionChange">
      <el-table-column v-if="isAdmin" type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="60">
        <template slot-scope="scope">
          {{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="封面" align="center" width="100">
        <template slot-scope="scope">
          <el-image
              v-if="scope.row.coverImg"
              style="width: 50px; height: 50px; border-radius: 4px;"
              :src="scope.row.coverImg"
              :preview-src-list="[scope.row.coverImg]">
          </el-image>
          <span v-else>-</span>
        </template>
      </el-table-column>

      <el-table-column label="标题" align="center" prop="title" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-link type="primary" :underline="false" style="font-weight: bold;" @click="handleViewDetail(scope.row)">{{ scope.row.title }}</el-link>
        </template>
      </el-table-column>

      <el-table-column label="类型" align="center" prop="type" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type == '1'" type="danger">高考政策</el-tag>
          <el-tag v-else-if="scope.row.type == '2'" type="primary">报考指南</el-tag>
          <el-tag v-else-if="scope.row.type == '3'" type="success">院校动态</el-tag>
          <span v-else>其他</span>
        </template>
      </el-table-column>

      <el-table-column label="阅读量" align="center" prop="viewCount" width="80" />
      <el-table-column label="发布时间" align="center" prop="createTime" width="160" />

      <el-table-column v-if="isAdmin" label="操作" align="center" class-name="small-padding fixed-width">
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

    <!-- 查看详情弹窗 (ReadOnly模式) -->
    <el-dialog :title="form.title || '资讯详情'" :visible.sync="viewOpen" width="1000px" append-to-body>
      <div class="news-view-container">
        <div class="news-view-header">
          <h1>{{ form.title }}</h1>
          <div class="news-view-meta">
            <span>类型：<el-tag size="mini">{{ getTypeName(form.type) }}</el-tag></span>
            <span style="margin-left: 20px;">阅读：{{ form.viewCount || 0 }}</span>
            <span style="margin-left: 20px;">时间：{{ form.createTime }}</span>
          </div>
        </div>
        <el-divider></el-divider>
        <div class="news-view-content ql-editor" v-html="form.content"></div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!-- 管理员编辑弹窗 -->
    <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="16">
            <el-form-item label="资讯标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入资讯标题" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="资讯类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择">
                <el-option label="高考政策" value="1" />
                <el-option label="报考指南" value="2" />
                <el-option label="院校动态" value="3" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="封面图片" prop="coverImg">
          <image-upload v-model="form.coverImg" :limit="1"/>
        </el-form-item>
        <el-form-item label="摘要" prop="summary">
          <el-input v-model="form.summary" type="textarea" placeholder="请输入文章摘要" />
        </el-form-item>
        <el-form-item label="正文内容" prop="content">
          <editor v-model="form.content" :min-height="300"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">发布内容</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
      </el-card>
  </div>
</template>

<script>
import { listNews, getNews, delNews, addNews, updateNews } from "@/api/entrance/news";
import Editor from '@/components/Editor';
import ImageUpload from '@/components/ImageUpload';
import { checkRole } from "@/utils/permission";

export default {
  name: "News",
  components: { Editor, ImageUpload },
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      newsList: [],
      title: "",
      open: false,
      viewOpen: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        type: null
      },
      form: {},
      rules: {
        title: [{ required: true, message: "资讯标题不能为空", trigger: "blur" }],
        type: [{ required: true, message: "请选择资讯类型", trigger: "change" }],
        content: [{ required: true, message: "正文内容不能为空", trigger: "blur" }]
      }
    };
  },
  computed: {
    isAdmin() {
      return checkRole(['admin']);
    }
  },
  created() {
    this.getList();
  },
  methods: {
    getTypeName(type) {
      const maps = { '1': '高考政策', '2': '报考指南', '3': '院校动态' };
      return maps[type] || '其他';
    },
    getList() {
      this.loading = true;
      listNews(this.queryParams).then(response => {
        this.newsList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    handleViewDetail(row) {
      const isAdmin = checkRole(['admin']);
      if (isAdmin) {
        // 管理员：弹窗快速预览
        getNews(row.id).then(response => {
          this.form = response.data;
          this.viewOpen = true;
        });
      } else {
        // 学生：跳转到有评论/收藏的完整详情页
        this.$router.push('/news-view/detail/' + row.id);
      }
    },
    cancel() { this.open = false; this.reset(); },
    reset() {
      this.form = { id: null, title: null, coverImg: null, summary: null, content: null, type: "1", status: "0" };
      this.resetForm("form");
    },
    handleQuery() { this.queryParams.pageNum = 1; this.getList(); },
    resetQuery() { this.resetForm("queryForm"); this.handleQuery(); },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    handleAdd() { this.reset(); this.open = true; this.title = "发布新资讯"; },
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getNews(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改资讯";
      });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateNews(this.form).then(() => {
              this.$message.success("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addNews(this.form).then(() => {
              this.$message.success("发布成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('确认要彻底删除选中的资讯吗？', '警告', { type: 'warning' }).then(() => {
        return delNews(ids);
      }).then(() => {
        this.getList();
        this.$message.success("删除成功");
      });
    },
  }
};
</script>

<style scoped>
.news-view-header { text-align: center; margin-bottom: 20px; }
.news-view-header h1 { color: #303133; font-size: 24px; margin-bottom: 15px; }
.news-view-meta { color: #909399; font-size: 13px; }
.news-view-content { line-height: 1.8; font-size: 16px; color: #606266; min-height: 400px; padding: 0 20px; }
</style>
