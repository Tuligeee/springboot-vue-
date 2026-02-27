<template>
  <div class="app-container">
    <el-card>
      <div slot="header">
        <span>高考资讯列表</span>
      </div>
      <div v-loading="loading">
        <div v-for="item in newsList" :key="item.id" style="padding: 10px; border-bottom: 1px solid #eee; cursor: pointer;" @click="handleDetail(item.id)">
          <h3>{{ item.title }}</h3>
          <p style="color: #888; font-size: 12px;">{{ item.createTime }}</p>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
import { listNews } from "@/api/entrance/news";

export default {
  name: "NewsList",
  data() {
    return {
      loading: true,
      newsList: []
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listNews({ pageNum: 1, pageSize: 20 }).then(res => {
        this.newsList = res.rows;
        this.loading = false;
      });
    },
    handleDetail(id) {
      this.$router.push('/news/detail/' + id);
    }
  }
};
</script>
