<template>
  <div class="app-container">
    <el-card shadow="hover" class="page-card" v-loading="loading">
      <!-- 优化后的 Flex 头部 -->
      <div slot="header" class="header-container">
        <el-page-header @back="goBack" :content="college.collegeName || '院校详情'"></el-page-header>
      </div>

      <div v-if="college.id">
        <!-- Hero Section for College -->
        <div class="college-hero-section">
          <div class="avatar-wrapper">
            <div class="school-avatar">
              <i class="el-icon-school"></i>
            </div>
            <!-- Honorary Ranking Badge -->
            <div class="ranking-badge" v-if="college.ranking">
              <span class="rank-top">RANK</span>
              <span class="rank-number">{{ college.ranking }}</span>
            </div>
          </div>
          
          <h1 class="hero-name">{{ college.collegeName }}</h1>
          
          <div class="hero-info-line">
            <span class="info-item">
              <i class="el-icon-location-outline"></i>
              {{ college.city || '未知城市' }}
            </span>
            <span class="divider"></span>
            <span class="info-item">
              <i class="el-icon-medal"></i>
              {{ college.educationLevel || '未知层次' }}
            </span>
          </div>
          
          <div class="hero-actions-center">
            <!-- 管理员/校管编辑权限 -->
            <template v-if="checkRole(['admin', 'school_admin'])">
              <el-button 
                v-if="!editMode"
                class="btn-premium btn-edit"
                type="primary" 
                icon="el-icon-edit" 
                @click="toggleEdit"
              >编辑修改</el-button>
              <template v-else>
                <el-button class="btn-premium btn-save" type="success" icon="el-icon-check" @click="handleUpdate">保存修改</el-button>
                <el-button class="btn-premium btn-cancel" type="info" icon="el-icon-close" @click="toggleEdit">取消</el-button>
              </template>
            </template>
            
            <!-- 学生填报/收藏权限 -->
            <template v-if="checkRole(['student', 'common'])">
              <el-button
                class="btn-premium btn-apply"
                type="primary"
                round
                icon="el-icon-edit-outline"
                @click="handleApply"
              >立即填报该校</el-button>
              <el-button
                class="btn-premium btn-collect"
                :type="isCollected ? 'warning' : 'info'"
                :icon="isCollected ? 'el-icon-star-on' : 'el-icon-star-off'"
                @click="handleCollectToggle"
                circle
              ></el-button>
            </template>
          </div>
        </div>
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
            <el-col :span="12">
              <el-form-item label="办学层次" prop="educationLevel">
                <el-radio-group v-model="collegeForm.educationLevel">
                  <el-radio label="本科">本科</el-radio>
                  <el-radio label="专科">专科</el-radio>
                </el-radio-group>
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
                  <el-tag :type="college.educationLevel === '本科' ? 'success' : 'warning'" size="mini" style="margin-left: 10px;" v-if="college.educationLevel">
                    {{ college.educationLevel }}
                  </el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="所在城市">{{ college.city || '未知' }}</el-descriptions-item>
                <el-descriptions-item label="全国排名">第 {{ college.ranking }} 名</el-descriptions-item>
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
          <el-table-column label="修业年限" align="center" prop="studyYear" width="100">
            <template slot-scope="scope">
              <el-tag size="small" type="success" effect="light">{{ scope.row.studyYear }}年制</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="选科要求" align="center" prop="subjectRequirement" min-width="150">
            <template slot-scope="scope">
              <el-tag :type="scope.row.subjectRequirement === '不提科目要求' || !scope.row.subjectRequirement ? 'info' : 'warning'" size="small">
                {{ scope.row.subjectRequirement || '不提科目要求' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="200">
            <template slot-scope="scope">
              <!-- <el-button type="text" icon="el-icon-data-analysis" @click="handleShowScore(scope.row)">历年分数</el-button> -->
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
          <el-table-column property="admissionCount" label="录取人数" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.admissionCount">{{ scope.row.admissionCount }}人</span>
              <span v-else style="color:#999">-</span>
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
.college-title { font-weight: bold; font-size: 18px; color: #2c3e50; }
.section-title { margin: 35px 0 15px 0; font-size: 19px; font-weight: 700; color: #1a1a1a; border-left: 5px solid #0974e7; padding-left: 15px; letter-spacing: 0.5px; }

/* Hero Section Styles */
.college-hero-section {
  padding: 50px 20px;
  text-align: center;
  background: linear-gradient(to bottom, #ffffff 0%, #f8fafc 100%);
  border-radius: 20px;
  margin-bottom: 30px;
  position: relative;
  overflow: hidden;
}

.avatar-wrapper {
  position: relative;
  width: 100px;
  height: 100px;
  margin: 0 auto 25px;
}

.school-avatar {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 100%);
  border-radius: 24px;
  display: flex;
  justify-content: center;
  align-items: center;
  box-shadow: 0 15px 35px rgba(79, 70, 229, 0.2);
  transform: rotate(-5deg);
  transition: all 0.5s ease;
}

.avatar-wrapper:hover .school-avatar {
  transform: rotate(0deg) scale(1.05);
}

.school-avatar i {
  font-size: 50px;
  color: #fff;
}

/* Ranking Badge Styles */
.ranking-badge {
  position: absolute;
  top: -10px;
  right: -15px;
  background: linear-gradient(135deg, #fbbf24 0%, #f59e0b 100%);
  color: #fff;
  padding: 8px 12px;
  border-radius: 12px;
  box-shadow: 0 8px 15px rgba(245, 158, 11, 0.3);
  display: flex;
  flex-direction: column;
  line-height: 1;
  border: 2px solid #ffffff;
  z-index: 10;
}

.rank-top {
  font-size: 10px;
  font-weight: 800;
  letter-spacing: 1px;
  opacity: 0.9;
  margin-bottom: 2px;
}

.rank-number {
  font-size: 20px;
  font-weight: 900;
}

.hero-name {
  font-size: 36px;
  font-weight: 850;
  color: #0f172a;
  margin-bottom: 15px;
  letter-spacing: -1px;
}

.hero-info-line {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  margin-bottom: 35px;
  color: #64748b;
  font-size: 16px;
  font-weight: 500;
}

.divider {
  width: 4px;
  height: 4px;
  background: #cbd5e1;
  border-radius: 50%;
}

.hero-actions-center {
  display: flex;
  justify-content: center;
  gap: 20px;
  align-items: center;
}

/* Premium Button Styles */
.btn-premium {
  font-weight: 600;
  letter-spacing: 0.5px;
  padding: 12px 28px;
  height: auto;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}
.btn-premium:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.15);
}
.btn-apply {
  background: linear-gradient(135deg, #3b82f6 0%, #2563eb 100%) !important;
  border: none !important;
  color: white !important;
}
.btn-edit {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%) !important;
  border: none !important;
  color: white !important;
}
.btn-collect {
  padding: 12px !important;
}

.intro-content { padding: 30px; background: #ffffff; border: 1px solid #e4e7ed; border-radius: 16px; color: #4a4a4a; line-height: 2.1; font-size: 15.5px; box-shadow: 0 4px 20px rgba(0,0,0,0.03); }
.multi-line-text { white-space: pre-wrap; word-break: break-all; }
.edit-form { padding: 25px; background: #f8fafc; border: 1px dashed #cbd5e1; border-radius: 12px; margin-bottom: 25px; }
.custom-textarea >>> .el-textarea__inner { font-family: inherit; font-size: 15px; padding: 15px; line-height: 1.8; border-radius: 8px; }
.modern-table { border-radius: 8px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.02); }
</style>
