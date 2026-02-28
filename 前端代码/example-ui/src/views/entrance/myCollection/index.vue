<template>
  <div class="app-container">
    <el-card shadow="never">
      <div slot="header" class="clearfix">
        <span style="font-size: 18px; font-weight: bold;"><i class="el-icon-collection"></i> 我的收藏中心</span>
      </div>

      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="高考资讯" name="1"></el-tab-pane>
        <el-tab-pane label="大学院校" name="2"></el-tab-pane>
        <el-tab-pane label="历年分数" name="3"></el-tab-pane>
        <el-tab-pane label="填报中心" name="4"></el-tab-pane>
      </el-tabs>

      <el-table v-loading="loading" :data="collectionList" border style="margin-top: 15px">
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="内容名称 / 标题" prop="showTitle">
          <template slot-scope="scope">
            <el-link type="primary" :underline="false" @click="handleViewDetail(scope.row)">
              {{ scope.row.showTitle }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column label="收藏时间" align="center" prop="createTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="150">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-delete" style="color: #F56C6C" @click="handleCancelCollect(scope.row)">
              取消收藏
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { listCollection, toggleCollect } from "@/api/entrance/collection";

export default {
  name: "MyCollection",
  data() {
    return {
      activeTab: "2", // 默认打开“大学院校”Tab
      loading: true,
      collectionList: [],
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listCollection({ targetType: this.activeTab }).then(response => {
        this.collectionList = response.data;
        this.loading = false;
      });
    },
    handleTabClick() {
      this.getList(); // 点击切换Tab时重新查询
    },
    handleCancelCollect(row) {
      this.$modal.confirm('确认要取消收藏该内容吗？').then(() => {
        return toggleCollect({ targetId: row.targetId, targetType: row.targetType });
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("已移除收藏");
      }).catch(() => {});
    },
    handleViewDetail(row) {
      let path = "";
      if (row.targetType == 1) path = "/entrance/news/detail";
      else if (row.targetType == 2) path = "/entrance/college/detail";

      this.$router.push({ path: path, query: { id: row.targetId } });
    }
  }
};
</script>
