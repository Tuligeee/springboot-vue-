<template>
  <div class="app-container">
    <el-card v-loading="loading">
      <div slot="header" class="clearfix">
        <el-page-header @back="goBack" :content="news.title || '资讯详情'"></el-page-header>
      </div>

      <div v-if="news.id">
        <div style="text-align: center; margin-bottom: 30px;">
          <h1 style="font-size: 24px; color: #303133;">{{ news.title }}</h1>
          <div style="color: #909399; font-size: 13px; margin-top: 10px;">
            <span style="margin-right: 20px;"><i class="el-icon-user"></i> 发布者：{{ news.createBy || '管理员' }}</span>
            <span style="margin-right: 20px;"><i class="el-icon-time"></i> 时间：{{ news.createTime }}</span>
            <span style="margin-right: 20px;"><i class="el-icon-view"></i> 阅读：{{ news.viewCount }}</span>
          </div>
        </div>
        <div class="news-content" v-html="news.content"></div>
      </div>

      <div v-else style="text-align: center; color: #909399; padding: 50px;">
        <p>未找到该资讯或已被删除</p>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getNews } from "@/api/entrance/news";

export default {
  name: "NewsDetail",
  data() {
    return {
      id: null,
      loading: true,
      news: {}
    };
  },
  created() {
    this.id = this.$route.params.id;
    if (this.id) {
      this.getDetail();
    }
  },
  methods: {
    getDetail() {
      this.loading = true;
      getNews(this.id).then(response => {
        this.news = response.data;
        this.loading = false;
      }).catch(() => {
        this.loading = false;
      });
    },
    goBack() {
      this.$router.go(-1);
    }
  }
};
</script>

<style scoped>
.news-content {
  padding: 20px;
  line-height: 1.8;
}
.news-content >>> img {
  max-width: 100%;
}
</style>
