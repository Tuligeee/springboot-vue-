<template>
  <div class="app-container">
    <el-card shadow="hover" class="page-card">
      <template slot="header">
        <span class="page-title">
          <i class="el-icon-star-on" style="color: #F7BA2A; margin-right: 8px;"></i>
          我的收藏中心
        </span>
      </template>

      <div v-loading="loading">
        <el-row :gutter="20" v-if="collectionList.length > 0">
          <el-col :span="12" v-for="item in collectionList" :key="item.collectionId" style="margin-bottom: 20px;">
            <el-card shadow="hover" class="collection-item-card">
              <div class="card-content">
                <div class="college-info">
                  <div class="title-row">
                    <span class="college-name">{{ item.collegeName }}</span>
                    <el-tag size="mini" type="warning" v-if="item.ranking">排名 {{ item.ranking }}</el-tag>
                  </div>
                  <div class="detail-row">
                    <span><i class="el-icon-location-outline"></i> {{ item.city || '未知城市' }}</span>
                  </div>
                  <div class="time-row">收藏于：{{ parseTime(item.createTime) }}</div>
                </div>
                <div class="actions">
                  <el-button type="primary" plain size="small" @click="handleView(item.targetId)">查看详情</el-button>
                  <el-button type="danger" plain size="small" icon="el-icon-delete" @click="handleDelete(item.collectionId)">取消收藏</el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        
        <el-empty v-else description="您还没有收藏任何院校哦，快去院校中心看看吧">
          <el-button type="primary" @click="$router.push('/college-view/list')">去寻找目标院校</el-button>
        </el-empty>
      </div>
    </el-card>
  </div>
</template>

<script>
import { listCollection, delCollection } from "@/api/entrance/collection";

export default {
  name: "MyCollection",
  data() {
    return {
      loading: true,
      collectionList: []
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listCollection().then(res => {
        // 后端现在返回结构化 Map
        this.collectionList = res.rows;
        this.loading = false;
      }).catch(() => {
        this.loading = false;
      });
    },
    handleView(id) {
      this.$router.push('/college-view/detail/' + id);
    },
    handleDelete(id) {
      this.$confirm('确认要取消收藏该院校吗？', '提示', {
        type: 'warning'
      }).then(() => {
        return delCollection(id);
      }).then(() => {
        this.$message.success("取消收藏成功");
        this.getList();
      });
    }
  }
};
</script>

<style scoped>
.collection-item-card {
  border-radius: 12px;
  border: 1px solid #ebeef5;
  background: #fdfdfd;
}
.card-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.college-name {
  font-size: 18px;
  font-weight: bold;
  color: #0974e7;
  margin-right: 10px;
}
.title-row {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}
.detail-row {
  font-size: 14px;
  color: #606266;
  margin-bottom: 8px;
}
.time-row {
  font-size: 12px;
  color: #909399;
}
.actions {
  display: flex;
  flex-direction: column;
  gap: 10px;
}
</style>
