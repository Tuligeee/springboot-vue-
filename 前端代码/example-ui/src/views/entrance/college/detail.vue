<template>
  <div class="app-container">
    <el-card v-loading="loading">
      <div slot="header" class="clearfix">
        <el-page-header @back="goBack" :content="college.name || '学校详情'">
          <template slot="title">
            <el-button
                :type="isCollected ? 'warning' : 'default'"
                :icon="isCollected ? 'el-icon-star-on' : 'el-icon-star-off'"
                @click="handleCollect"
                circle
                size="small"
                title="点击收藏/取消收藏"
                style="margin-left: 15px;">
            </el-button>
          </template>
        </el-page-header>
      </div>

      <div v-if="college.id">
        <el-descriptions title="院校基本信息" :column="2" border>
          <el-descriptions-item label="学校名称">{{ college.name }}</el-descriptions-item>
          <el-descriptions-item label="院校代码">{{ college.code }}</el-descriptions-item>
          <el-descriptions-item label="所在地区">{{ college.region }}</el-descriptions-item>
          <el-descriptions-item label="办学性质">{{ college.nature }}</el-descriptions-item>
          <el-descriptions-item label="院校类型">{{ college.type }}</el-descriptions-item>
          <el-descriptions-item label="学校官网" :span="2">
            <el-link :href="college.website" target="_blank" type="primary">{{ college.website }}</el-link>
          </el-descriptions-item>
          <el-descriptions-item label="学校简介" :span="2">
            {{ college.intro }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <el-empty v-else-if="!loading" description="暂无学校信息"></el-empty>
    </el-card>
  </div>
</template>

<script>
import { getCollege } from "@/api/entrance/college";
import { checkCollect, toggleCollect } from "@/api/entrance/collection";

export default {
  name: "CollegeDetail",
  data() {
    return {
      college: {},
      loading: true,
      isCollected: false,
      collegeId: null
    };
  },
  created() {
    // 兼容取 id 或 collegeId
    let id = this.$route.query.id || this.$route.query.collegeId || this.$route.params.id;

    if (id) {
      this.collegeId = id;
      this.getDetail(id);
      this.getCollectionStatus();
    } else {
      this.loading = false;
      this.$modal.msgError("错误：无法在地址栏中获取到学校ID！");
    }
  },
  methods: {
    getDetail(id) {
      this.loading = true;
      getCollege(id).then(response => {
        this.college = response.data;
        this.loading = false;
      });
    },
    getCollectionStatus() {
      checkCollect({ targetId: this.collegeId, targetType: 2 }).then(res => {
        this.isCollected = res.data;
      });
    },
    handleCollect() {
      toggleCollect({ targetId: this.collegeId, targetType: 2 }).then(res => {
        this.$modal.msgSuccess(res.msg);
        this.isCollected = !this.isCollected;
      });
    },
    goBack() {
      this.$router.go(-1);
    }
  }
};
</script>
<style scoped>
.app-container { padding: 20px; }
.clearfix:after { content: ""; display: table; clear: both; }
</style>
