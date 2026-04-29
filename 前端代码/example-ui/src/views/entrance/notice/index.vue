<template>
  <div class="app-container">
    <el-card shadow="hover" class="page-card">
      <template slot="header">
        <span class="page-title">
          <i class="el-icon-bell" style="color: #E6A23C; margin-right: 8px;"></i>
          系统公告
        </span>
      </template>

      <el-form :model="queryParams" :inline="true" label-width="68px" style="margin-bottom: 8px;">
        <el-form-item label="标题">
          <el-input
            v-model="queryParams.noticeTitle"
            placeholder="请输入公告标题"
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

      <el-table v-loading="loading" :data="noticeList">
        <el-table-column label="标题" prop="noticeTitle" min-width="360" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <el-link type="primary" :underline="false" @click="openDetail(scope.row.noticeId)">
              {{ scope.row.noticeTitle }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column label="类型" prop="noticeType" width="110" align="center">
          <template slot-scope="scope">
            <dict-tag :options="typeOptions" :value="scope.row.noticeType" />
          </template>
        </el-table-column>
        <el-table-column label="发布时间" prop="createTime" width="180" align="center" />
      </el-table>

      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-card>

    <el-dialog title="公告详情" :visible.sync="detailOpen" width="860px" append-to-body>
      <div v-if="detail.noticeId">
        <h2 class="notice-title">{{ detail.noticeTitle }}</h2>
        <div class="notice-meta">
          <dict-tag :options="typeOptions" :value="detail.noticeType" />
          <span style="margin-left: 12px;">发布时间：{{ detail.createTime }}</span>
        </div>
        <div class="notice-content" v-html="detail.noticeContent || ''"></div>
      </div>
      <div v-else class="empty-tip">公告不存在或已关闭</div>
    </el-dialog>
  </div>
</template>

<script>
import { listPublicNotice, getPublicNotice } from "@/api/system/notice";

export default {
  name: "EntranceNotice",
  data() {
    return {
      loading: false,
      total: 0,
      noticeList: [],
      detailOpen: false,
      detail: {},
      typeOptions: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        noticeTitle: undefined
      }
    };
  },
  created() {
    this.getList();
    this.getDicts("sys_notice_type").then(response => {
      this.typeOptions = response.data || [];
    });
  },
  methods: {
    getList() {
      this.loading = true;
      listPublicNotice(this.queryParams).then(res => {
        this.noticeList = res.rows || [];
        this.total = res.total || 0;
        this.loading = false;
      }).catch(() => {
        this.loading = false;
      });
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.queryParams.noticeTitle = undefined;
      this.handleQuery();
    },
    openDetail(id) {
      getPublicNotice(id).then(res => {
        this.detail = res.data || {};
        this.detailOpen = true;
      });
    }
  }
};
</script>

<style scoped>
.notice-title {
  margin: 0 0 10px 0;
  color: #303133;
}
.notice-meta {
  color: #909399;
  font-size: 13px;
  margin-bottom: 16px;
}
.notice-content {
  color: #303133;
  line-height: 1.8;
  padding: 14px;
  background: #fafafa;
  border-radius: 8px;
}
.empty-tip {
  text-align: center;
  color: #909399;
  padding: 20px 0;
}
</style>
