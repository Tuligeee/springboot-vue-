<template>
  <div class="app-container">
    <el-card shadow="hover" class="page-card" v-loading="loading">
      <!-- 优化后的 Flex 头部 -->
      <div slot="header" class="header-container">
        <el-page-header @back="goBack" :content="college.collegeName || '院校详情'"></el-page-header>
        <div class="header-actions">
          <!-- 管理员/校管编辑权限 -->
          <template v-if="checkRole(['admin', 'school_admin'])">
            <el-button 
              v-if="!editMode"
              type="primary" 
              icon="el-icon-edit" 
              size="small" 
              @click="toggleEdit"
            >编辑修改</el-button>
            <template v-else>
              <el-button type="success" icon="el-icon-check" size="small" @click="handleUpdate">保存修改</el-button>
              <el-button type="info" icon="el-icon-close" size="small" @click="toggleEdit">取消</el-button>
            </template>
          </template>
          
          <!-- 学生填报/收藏权限 -->
          <template v-if="checkRole(['student', 'common'])">
            <el-button
              type="primary"
              round
              icon="el-icon-edit-outline"
              @click="handleApply"
              size="small"
            >立即填报该校</el-button>
            <el-tooltip :content="isCollected ? '取消收藏' : '点击收藏'" placement="top">
              <el-button
                :type="isCollected ? 'warning' : 'info'"
                :icon="isCollected ? 'el-icon-star-on' : 'el-icon-star-off'"
                @click="handleCollectToggle"
                circle
                size="small"
              ></el-button>
            </el-tooltip>
          </template>
        </div>
      </div>

      <div v-if="college.id">
        <!-- 编辑模式表单 -->
        <el-form v-if="editMode" ref="collegeForm" :model="collegeForm" label-width="100px" class="edit-form shadow-sm">
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="院校名称" prop="collegeName">
                <el-input v-model="collegeForm.collegeName" placeholder="输入院校名称" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="所在城市" prop="city">
                <el-input v-model="collegeForm.city" placeholder="输入所在城市" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="全国排名" prop="ranking">
                <el-input-number v-model="collegeForm.ranking" :min="1" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
             <el-col :span="8">
              <el-form-item label="招生人数" prop="personCount">
                <el-input-number v-model="collegeForm.personCount" :min="0" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>
          
          <div class="section-title">院校简介编辑</div>
          <el-form-item label-width="0">
            <el-input 
              v-model="collegeForm.detailInfo" 
              type="textarea" 
              :rows="12" 
              placeholder="请输入院校详细简介，支持长篇文字..."
              class="custom-textarea"
            />
          </el-form-item>
        </el-form>

        <!-- 展示模式 -->
        <template v-else>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-descriptions class="margin-top" title="院校概况" :column="3" border>
                <el-descriptions-item label="学校名称">
                  <span class="college-title">{{ college.collegeName }}</span>
                </el-descriptions-item>
                <el-descriptions-item label="所在城市">{{ college.city || '未知' }}</el-descriptions-item>
                <el-descriptions-item label="全国排名">第 {{ college.ranking }} 名</el-descriptions-item>
                <el-descriptions-item label="招生人数">{{ college.personCount }} 人</el-descriptions-item>
                <el-descriptions-item label="更新时间">{{ college.updatedTime || '暂无记录' }}</el-descriptions-item>
              </el-descriptions>
            </el-col>
          </el-row>

          <div class="section-title">院校简介</div>
          <div class="intro-content">
            <p v-if="college.detailInfo" class="multi-line-text">{{ college.detailInfo }}</p>
            <el-empty v-else :image-size="60" description="该校暂未完善简介"></el-empty>
          </div>
        </template>

        <div class="section-title">
          开设专业 ({{ professions.length }} 个)
          <el-button type="text" style="float: right" @click="$router.push({path: '/profession-view/list', query: { collegeName: college.collegeName }})">查看更多专业</el-button>
        </div>
        <el-table :data="professions" stripe style="width: 100%" border class="modern-table">
          <el-table-column label="专业名称" align="center" prop="professionName">
            <template slot-scope="scope">
              <span style="font-weight: 600; color: #303133;">{{ scope.row.professionName }}</span>
            </template>
          </el-table-column>
          <el-table-column label="修业年限" align="center" prop="studyYear" width="120">
            <template slot-scope="scope">
              <el-tag size="small" type="success" effect="light">{{ scope.row.studyYear }}年制</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="200">
            <template slot-scope="scope">
              <el-button type="text" icon="el-icon-data-analysis" @click="handleShowScore(scope.row)">历年分数</el-button>
              <el-button v-if="checkRole(['student', 'common'])" type="text" icon="el-icon-edit" @click="handleApplyWithProf(scope.row)">快速填报</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <el-empty v-else description="无法加载该院校信息"></el-empty>
    </el-card>

    <!-- 分数弹窗 -->
    <el-dialog title="历年录取分" :visible.sync="scoreVisible" width="400px" custom-class="score-dialog">
       <el-table :data="currentScoreData" size="mini" v-loading="scoreLoading" border>
          <el-table-column property="year" label="年份" align="center"></el-table-column>
          <el-table-column property="score" label="录取分数" align="center">
            <template slot-scope="scope">
              <span style="font-weight: bold; color: #F56C6C;">{{ scope.row.score }}</span>
            </template>
          </el-table-column>
        </el-table>
    </el-dialog>
  </div>
</template>

<script>
import { getCollege, updateCollege } from "@/api/entrance/college";
import { listProfession } from "@/api/entrance/profession";
import { checkCollect, toggleCollect } from "@/api/entrance/collection";
import { listScoreLine } from "@/api/entrance/scoreLine";
import { checkRole } from "@/utils/permission";

export default {
  name: "CollegeDetail",
  data() {
    return {
      college: {},
      collegeForm: {},
      editMode: false,
      professions: [],
      loading: true,
      isCollected: false,
      collegeId: null,
      scoreDataMap: {},
      currentScoreData: [],
      scoreVisible: false,
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
        if (this.college && this.college.collegeName) {
          this.getProfessions(this.college.collegeName);
        }
        this.loading = false;
      }).catch(() => { this.loading = false; });
    },
    getProfessions(collegeName) {
      listProfession({ collegeName: collegeName, pageSize: 50 }).then(res => {
        this.professions = res.rows;
      });
    },
    toggleEdit() {
      if (!this.editMode) {
        this.collegeForm = JSON.parse(JSON.stringify(this.college));
      }
      this.editMode = !this.editMode;
    },
    handleUpdate() {
      this.loading = true;
      updateCollege(this.collegeForm).then(res => {
        this.$message.success("院校信息更新成功");
        this.editMode = false;
        this.getDetail();
      }).catch(() => { this.loading = false; });
    },
    handleShowScore(row) {
      this.scoreVisible = true;
      if (this.scoreDataMap[row.professionNo]) {
        this.currentScoreData = this.scoreDataMap[row.professionNo];
        return;
      }
      this.scoreLoading = true;
      listScoreLine({ collegeNo: this.college.collegeNo, professionNo: row.professionNo }).then(res => {
        this.currentScoreData = res.rows;
        this.scoreDataMap[row.professionNo] = res.rows;
        this.scoreLoading = false;
      }).catch(() => { this.scoreLoading = false; });
    },
    checkMyCollection() {
      if (!this.checkRole(['student', 'common'])) return;
      checkCollect({ targetId: this.collegeId, targetType: 2 }).then(res => {
        this.isCollected = !!res.data;
      });
    },
    handleCollectToggle() {
      toggleCollect({ targetId: this.collegeId, targetType: 2 }).then(res => {
        this.$message.success(this.isCollected ? "已从收藏夹移除" : "已成功加入收藏");
        this.checkMyCollection();
      });
    },
    handleApply() {
      this.$router.push({ path: '/filling-view/apply', query: { collegeNo: this.college.collegeNo } });
    },
    handleApplyWithProf(prof) {
      this.$router.push({ path: '/filling-view/apply', query: { professionId: prof.id } });
    },
    goBack() { 
        // 尝试返回来源页，默认返回院校列表
        if (window.history.length > 1) {
            this.$router.back();
        } else {
            this.$router.push('/profession-view/college');
        }
    }
  }
};
</script>

<style scoped>
.header-container { display: flex; justify-content: space-between; align-items: center; width: 100%; }
.header-actions { display: flex; align-items: center; gap: 15px; }
.college-title { font-weight: bold; font-size: 18px; color: #2c3e50; }
.section-title { margin: 35px 0 15px 0; font-size: 19px; font-weight: 700; color: #1a1a1a; border-left: 5px solid #0974e7; padding-left: 15px; letter-spacing: 0.5px; }
.intro-content { padding: 30px; background: #ffffff; border: 1px solid #e4e7ed; border-radius: 16px; color: #4a4a4a; line-height: 2.1; font-size: 15.5px; box-shadow: 0 4px 20px rgba(0,0,0,0.03); }
.multi-line-text { white-space: pre-wrap; word-break: break-all; }
.edit-form { padding: 25px; background: #f8fafc; border: 1px dashed #cbd5e1; border-radius: 12px; margin-bottom: 25px; }
.custom-textarea >>> .el-textarea__inner { font-family: inherit; font-size: 15px; padding: 15px; line-height: 1.8; border-radius: 8px; }
.modern-table { border-radius: 8px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.02); }
</style>
