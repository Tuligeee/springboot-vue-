<template>
  <div class="app-container">
    <el-card>
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; font-size: 16px;"><i class="el-icon-s-flag"></i> 我的模拟志愿表</span>
      </div>

      <el-table v-loading="loading" :data="dataList" border style="width: 100%">
        <el-table-column label="序号" type="index" width="55" align="center" />

        <el-table-column label="高校名称" prop="collegeName" align="center">
          <template slot-scope="scope">
            <span style="font-weight: bold; color: #409EFF;">{{ scope.row.collegeName }}</span>
          </template>
        </el-table-column>

        <el-table-column label="填报专业" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.professionName" type="success" effect="light">
              {{ scope.row.professionName }}
            </el-tag>
            <span v-else style="color: #999;">--</span>
          </template>
        </el-table-column>

        <el-table-column label="填报时间" prop="createTime" align="center" width="180" />

        <el-table-column label="操作" align="center" width="150">
          <template slot-scope="scope">
            <el-button
                size="mini"
                type="danger"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
            >移除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import { listMyVolunteer, delVolunteer } from "@/api/entrance/volunteer";

export default {
  name: "MyVolunteer",
  data() {
    return {
      loading: true,
      dataList: []
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listMyVolunteer().then(res => {
        this.dataList = res.rows;
        this.loading = false;
      });
    },
    handleDelete(row) {
      this.$confirm('确定要移出该志愿吗？', '提示', { type: 'warning' })
          .then(() => delVolunteer(row.id))
          .then(() => {
            this.$message.success("移除成功");
            this.getList();
          });
    }
  }
};
</script>
