<template>
  <div class="app-container">
    <el-card v-loading="loading">
      <div slot="header" class="clearfix">
        <el-page-header @back="goBack" :content="college.collegeName || '学校详情'"></el-page-header>
      </div>

      <div v-if="college.id">
        <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
          <div style="display: flex; align-items: center;">
            <el-avatar icon="el-icon-school" :size="60" style="background-color: #409EFF; margin-right: 20px;"></el-avatar>
            <div>
              <h1 style="margin: 0; font-size: 24px;">{{ college.collegeName }}</h1>
              <p style="margin: 5px 0 0 0; color: #999;">
                <i class="el-icon-location-outline"></i> {{ college.city || '未知城市' }} |
                <el-tag size="mini">排名: {{ college.ranking || '暂无' }}</el-tag>
              </p>
            </div>
          </div>
        </div>

        <el-descriptions title="院校档案" :column="2" border style="margin-bottom: 30px;">
          <el-descriptions-item label="院校代码">{{ college.collegeNo }}</el-descriptions-item>
          <el-descriptions-item label="所在城市">{{ college.city }}</el-descriptions-item>
          <el-descriptions-item label="招生人数">{{ college.personCount }}人</el-descriptions-item>
          <el-descriptions-item label="院校简介" :span="2">{{ college.detailInfo }}</el-descriptions-item>
        </el-descriptions>

        <div style="border-top: 1px solid #eee; padding-top: 20px;">
          <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px;">
            <h3 style="border-left: 4px solid #409EFF; padding-left: 10px; margin: 0;">开设专业</h3>

            <el-button
                type="warning"
                icon="el-icon-circle-check"
                :disabled="selectedProfessions.length === 0"
                @click="handleBatchAdd"
            >
              批量填报选中专业 ({{ selectedProfessions.length }})
            </el-button>
          </div>

          <el-table
              :data="professionList"
              stripe
              style="width: 100%"
              @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55" align="center" />

            <el-table-column prop="professionNo" label="专业代码" width="120" align="center" />

            <el-table-column prop="professionName" label="专业名称" width="220">
              <template slot-scope="scope">
                <span style="font-weight: bold; color: #303133;">{{ scope.row.professionName }}</span>
              </template>
            </el-table-column>

            <el-table-column prop="detailInfo" label="专业介绍" :show-overflow-tooltip="true" />

            <el-table-column prop="studyYear" label="修业年限" width="100" align="center">
              <template slot-scope="scope">
                {{ scope.row.studyYear ? scope.row.studyYear + '年' : '4年' }}
              </template>
            </el-table-column>

            <el-table-column label="操作" width="100" align="center">
              <template slot-scope="scope">
                <el-button
                    type="text"
                    size="mini"
                    icon="el-icon-plus"
                    @click="handleAddSingle(scope.row)"
                >
                  填报
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>

      </div>
    </el-card>
  </div>
</template>

<script>
import { getCollege } from "@/api/entrance/college";
import { listProfession } from "@/api/entrance/profession";
import { addVolunteer } from "@/api/entrance/volunteer";

export default {
  name: "CollegeDetail",
  data() {
    return {
      id: null,
      loading: true,
      college: {},
      professionList: [],
      // 【新增】存储选中的专业
      selectedProfessions: []
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
      getCollege(this.id).then(res => {
        this.college = res.data;
        if (this.college.collegeNo) {
          this.getProfessions(this.college.collegeNo);
        }
        this.loading = false;
      });
    },
    getProfessions(collegeNo) {
      listProfession({ collegeNo: collegeNo }).then(res => {
        this.professionList = res.rows;
      });
    },
    goBack() {
      this.$router.go(-1);
    },

    // 【新增】监听复选框变化
    handleSelectionChange(selection) {
      this.selectedProfessions = selection;
    },

    // 【新增】批量添加逻辑
    handleBatchAdd() {
      const count = this.selectedProfessions.length;
      if (count === 0) return;

      this.$confirm(`确定要同时填报这 ${count} 个专业吗？`, '批量志愿确认', {
        confirmButtonText: '确定全部加入',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        // 使用 Promise.all 并行发送请求
        // 构造请求列表
        const requestPromises = this.selectedProfessions.map(prof => {
          const data = {
            collegeId: this.college.id,
            collegeName: this.college.collegeName,
            professionId: prof.id,
            professionName: prof.professionName
          };
          return addVolunteer(data);
        });

        try {
          // 等待所有请求完成
          await Promise.all(requestPromises);
          this.$message.success(`成功添加 ${count} 个志愿！`);
          // 清空选中
          this.$refs.multipleTable.clearSelection();
        } catch (error) {
          // 如果部分失败（比如有重复添加的），这里会被捕获
          // 但由于我们后端有查重逻辑，前端只会显示第一条报错，这是正常的
          console.error(error);
        }
      });
    },

    // 单个添加（保留旧功能）
    handleAddSingle(profession) {
      const data = {
        collegeId: this.college.id,
        collegeName: this.college.collegeName,
        professionId: profession.id,
        professionName: profession.professionName
      };

      this.$confirm(`确定要填报 "${profession.professionName}" 吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        addVolunteer(data).then(res => {
          this.$message.success('已成功加入志愿表！');
        });
      });
    }
  }
};
</script>
