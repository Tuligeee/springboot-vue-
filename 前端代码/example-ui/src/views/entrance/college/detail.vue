<template>
  <div class="app-container">
    <el-card shadow="hover" class="page-card" v-loading="loading">
      <div slot="header" class="clearfix">
        <el-page-header @back="goBack" :content="college.collegeName || '院校详情'">
          <template slot="title">
            <span>返回</span>
          </template>
        </el-page-header>
        <div style="float: right; margin-top: -35px;">
          <el-button
            type="primary"
            round
            icon="el-icon-edit-outline"
            @click="handleApply"
            v-if="checkRole(['student', 'common'])"
          >立即填报该校</el-button>
          <!-- 智能收藏按钮 -->
          <el-tooltip :content="isCollected ? '取消收藏' : '点击收藏'" placement="top">
            <el-button
              :type="isCollected ? 'warning' : 'info'"
              :icon="isCollected ? 'el-icon-star-on' : 'el-icon-star-off'"
              @click="handleCollectToggle"
              circle
              style="margin-left: 10px; font-size: 18px;"
            ></el-button>
          </el-tooltip>
        </div>
      </div>

      <div v-if="college.id">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-descriptions class="margin-top" title="院校概况" :column="3" border>
              <el-descriptions-item label="学校名称">
                <span style="font-weight: bold; font-size: 16px; color: #0974e7;">{{ college.collegeName }}</span>
              </el-descriptions-item>
              <el-descriptions-item label="院校代码">{{ college.collegeNo }}</el-descriptions-item>
              <el-descriptions-item label="所在城市">{{ college.city || '未知' }}</el-descriptions-item>
              <el-descriptions-item label="全国排名">第 {{ college.ranking }} 名</el-descriptions-item>
              <el-descriptions-item label="招生人数">{{ college.personCount }} 人</el-descriptions-item>
              <el-descriptions-item label="更新时间">{{ college.updatedTime || '暂无记录' }}</el-descriptions-item>
            </el-descriptions>
          </el-col>
        </el-row>

        <div class="section-title">院校简介</div>
        <div class="intro-content">
          <p v-if="college.detailInfo">{{ college.detailInfo }}</p>
          <el-empty v-else :image-size="60" description="该校暂未完善简介"></el-empty>
        </div>

        <div class="section-title">
          开设专业 ({{ professions.length }} 个)
          <el-button type="text" style="float: right" @click="$router.push({path: '/profession-view/list', query: {collegeNo: college.collegeNo}})">查看更多专业</el-button>
        </div>
        <el-table :data="professions" stripe style="width: 100%" border>
          <el-table-column label="专业名称" align="center" prop="professionName">
            <template slot-scope="scope">
              <span style="font-weight: 500;">{{ scope.row.professionName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="修业年限" align="center" prop="studyYear" width="120">
            <template slot-scope="scope">
              <el-tag size="small" effect="plain">{{ scope.row.studyYear }}年制</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="历年录取分" align="center" width="220">
            <template slot-scope="scope">
              <el-popover
                placement="right"
                width="300"
                trigger="click"
                @show="handleShowScore(scope.row)">
                <el-table :data="scoreDataMap[scope.row.professionNo]" size="mini" v-loading="scoreLoading">
                  <el-table-column width="100" property="year" label="年份"></el-table-column>
                  <el-table-column width="150" property="score" label="录取分数"></el-table-column>
                </el-table>
                <el-button slot="reference" type="text" icon="el-icon-data-analysis">查看往年分数</el-button>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="150">
            <template slot-scope="scope">
              <el-button type="text" icon="el-icon-edit" @click="handleApplyWithProf(scope.row)">快速填报</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <el-empty v-else description="无法加载该院校信息"></el-empty>
    </el-card>
  </div>
</template>

<script>
import { getCollege } from "@/api/entrance/college";
import { listProfession } from "@/api/entrance/profession";
import { addCollection, listCollection, delCollection } from "@/api/entrance/collection";
import { listScoreLine } from "@/api/entrance/scoreLine";
import { checkRole } from "@/utils/permission";

export default {
  name: "CollegeDetail",
  data() {
    return {
      college: {},
      professions: [],
      loading: true,
      isCollected: false,
      currentCollectionId: null, // 精准记录收藏ID
      collegeId: null,
      scoreDataMap: {}, // 缓存各专业的分数数据 {professionNo: []}
      scoreLoading: false
    };
  },
  created() {
    this.collegeId = this.$route.params.id || this.$route.query.id;
    if (this.collegeId) {
      this.getDetail();
      this.checkMyCollection();
    }
  },
  methods: {
    checkRole,
    getDetail() {
      this.loading = true;
      getCollege(this.collegeId).then(res => {
        this.college = res.data;
        if (this.college && this.college.collegeNo) {
          this.getProfessions(this.college.collegeNo);
        }
        this.loading = false;
      }).catch(() => { this.loading = false; });
    },
    getProfessions(collegeNo) {
      listProfession({ collegeNo: collegeNo, pageSize: 50 }).then(res => {
        this.professions = res.rows;
      });
    },
    /** 加载并缓存专业分数 */
    handleShowScore(row) {
      if (this.scoreDataMap[row.professionNo]) return; // 已加载则跳过
      this.scoreLoading = true;
      listScoreLine({ 
        collegeNo: this.college.collegeNo, 
        professionNo: row.professionNo 
      }).then(res => {
        this.$set(this.scoreDataMap, row.professionNo, res.rows);
        this.scoreLoading = false;
      }).catch(() => { this.scoreLoading = false; });
    },
    /** 检查当前用户对该校的收藏状态 */
    checkMyCollection() {
      listCollection().then(res => {
        // 后端返回的收藏列表，targetId 可能为 String 或 Long，强制 String 对比防止失效
        const mine = res.rows.find(c => String(c.targetId) === String(this.collegeId) && c.targetType === 2);
        if (mine) {
          this.isCollected = true;
          this.currentCollectionId = mine.collectionId; // 记录用于取消的 ID
        } else {
          this.isCollected = false;
          this.currentCollectionId = null;
        }
      });
    },
    /** 智能切换收藏状态 */
    handleCollectToggle() {
      if (this.isCollected && this.currentCollectionId) {
        // 已收藏 -> 执行取消
        delCollection(this.currentCollectionId).then(() => {
          this.$message.success("已取消收藏");
          this.isCollected = false;
          this.currentCollectionId = null;
        });
      } else {
        // 未收藏 -> 执行收藏
        const data = {
          targetId: this.collegeId,
          targetType: 2 // 约定院校类型为 2
        };
        addCollection(data).then(res => {
          if (res.code === 0) {
            this.$message.success("院校收藏成功");
            this.checkMyCollection(); // 重新检查以获取 collectionId
          }
        });
      }
    },
    handleApply() {
      this.$router.push({ path: '/filling-view/apply', query: { collegeNo: this.college.collegeNo } });
    },
    handleApplyWithProf(prof) {
      this.$router.push({ path: '/filling-view/apply', query: { collegeNo: this.college.collegeNo, professionNo: prof.professionNo } });
    },
    goBack() { this.$router.go(-1); }
  }
};
</script>

<style scoped>
.section-title { margin: 30px 0 15px 0; font-size: 18px; font-weight: bold; color: #303133; border-left: 4px solid #0974e7; padding-left: 15px; }
.intro-content { padding: 20px; background: #fdfdfd; border: 1px solid #f0f2f5; border-radius: 8px; color: #606266; line-height: 1.8; font-size: 15px; }
</style>
