<template>
  <div class="profile-container my-profile-page">
    <!-- 头部横幅 - Hero Banner -->
    <div class="hero-banner">
      <div class="hero-content">
        <h1 class="hero-title">
          <i class="el-icon-school"></i> 本校机构档案
        </h1>
        <p class="hero-desc">在此维护院校的基础信息、办学规模及官方简介，确保考生获取最新、最准的报考参考数据。</p>
      </div>
      <div class="hero-icon">
        <i class="el-icon-office-building"></i>
      </div>
    </div>

    <!-- 核心指标统计卡片 - Quick Stat Cards -->
    <el-row :gutter="20" class="stat-row">
      <el-col :span="8">
        <div class="stat-card ranking">
          <div class="stat-label">全国参考排名</div>
          <div class="stat-value">{{ form.ranking || '--' }} <span class="unit">名</span></div>
          <i class="el-icon-medal card-bg-icon"></i>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card location">
          <div class="stat-label">所在城市</div>
          <div class="stat-value">
            <el-tag type="success" effect="dark" v-if="form.city">
              <i class="el-icon-location-outline"></i> {{ form.city }}
            </el-tag>
            <span v-else>未设置</span>
          </div>
          <i class="el-icon-map-location card-bg-icon"></i>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card student-count">
          <div class="stat-label">预估在校人数</div>
          <div class="stat-value">{{ form.personCount || '0' }} <span class="unit">人</span></div>
          <i class="el-icon-user card-bg-icon"></i>
        </div>
      </el-col>
    </el-row>

    <!-- 核心表单区域 - Form Section -->
    <el-card shadow="never" class="editor-card" v-loading="loading">
      <!-- 未绑定学校的提示 -->
      <el-empty v-if="!loading && !form.id" description="当前账号尚未绑定院校，请联系管理员分配。"></el-empty>

      <template v-else>
        <div class="card-header">
          <span class="title">基础信息维护</span>
        </div>

        <el-form ref="profileForm" :model="form" :rules="rules" label-position="top">
          <el-row :gutter="40">
            <el-col :span="12">
              <div class="section-title"><i class="el-icon-edit-outline"></i> 注册信息</div>
              <el-form-item label="院校官方名称" prop="collegeName">
                <el-input v-model="form.collegeName" placeholder="建议输入全称" />
              </el-form-item>
              <el-form-item label="统一院校代码" prop="collegeNo">
                <el-input v-model="form.collegeNo" placeholder="请输入5位或数字代码" />
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <div class="section-title"><i class="el-icon-set-up"></i> 办学指标</div>
              <el-row :gutter="20">
                <el-col :span="12">
                  <el-form-item label="所在城市" prop="city">
                    <el-input v-model="form.city" placeholder="例：武汉" />
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="全国排名">
                    <el-input-number v-model="form.ranking" :min="1" :max="5000" controls-position="right" style="width: 100%;" />
                  </el-form-item>
                </el-col>
              </el-row>
              <el-form-item label="学生总数">
                <el-input-number v-model="form.personCount" :min="0" controls-position="right" style="width: 100%;" />
              </el-form-item>
            </el-col>
          </el-row>

          <div class="section-title" style="margin-top: 20px;">
            <i class="el-icon-reading"></i> 院校详细介绍
          </div>
          <el-form-item label-width="0">
            <editor v-model="form.detailInfo" :min-height="300" />
          </el-form-item>

          <div class="form-footer">
            <el-button type="primary" class="submit-btn" :loading="submitLoading" @click="handleSubmit">
              <i class="el-icon-upload"></i> 保存修改并同步
            </el-button>
            <el-button icon="el-icon-refresh" @click="fetchProfile" plain>重置</el-button>
          </div>
        </el-form>
      </template>
    </el-card>
  </div>
</template>

<script>
import { getMyCollege, updateCollege } from "@/api/entrance/college";
import Editor from "@/components/Editor";

export default {
  name: "MyProfile",
  components: { Editor },
  data() {
    return {
      loading: true,
      submitLoading: false,
      form: {},
      rules: {
        collegeName: [
          { required: true, message: "院校名称不能为空", trigger: "blur" }
        ],
        collegeNo: [
          { required: true, message: "院校代码不能为空", trigger: "blur" }
        ],
        city: [
          { required: true, message: "所在城市不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.fetchProfile();
  },
  methods: {
    /** 获取本校资料 */
    fetchProfile() {
      this.loading = true;
      getMyCollege().then(res => {
        this.form = res.data || {};
        this.loading = false;
      }).catch(() => {
        this.form = {};
        this.loading = false;
      });
    },
    /** 提交修改 */
    handleSubmit() {
      this.$refs.profileForm.validate(valid => {
        if (!valid) return;
        this.submitLoading = true;
        updateCollege(this.form).then(res => {
          this.msgSuccess("保存成功");
          this.submitLoading = false;
          this.fetchProfile();
        }).catch(() => {
          this.submitLoading = false;
        });
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.profile-container {
  padding: 24px;
  background-color: #f8fafc;
  min-height: calc(100vh - 84px);
}

/* 复用学生端的 Hero Banner 样式 */
.hero-banner {
  background: linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%);
  border-radius: 16px;
  padding: 40px;
  color: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  box-shadow: 0 10px 25px rgba(59, 130, 246, 0.2);

  .hero-title {
    font-size: 32px;
    font-weight: 700;
    margin: 0 0 12px 0;
    letter-spacing: 1px;
    i { margin-right: 12px; }
  }
  .hero-desc {
    font-size: 16px;
    opacity: 0.9;
    margin: 0;
    max-width: 600px;
    line-height: 1.6;
  }
  .hero-icon {
    font-size: 80px;
    opacity: 0.2;
  }
}

.stat-row {
  margin-bottom: 24px;
}

.stat-card {
  background: #fff;
  border-radius: 16px;
  padding: 24px;
  height: 120px;
  position: relative;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.03);
  display: flex;
  flex-direction: column;
  justify-content: center;
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
  }

  .stat-label {
    color: #94a3b8;
    font-size: 14px;
    font-weight: 500;
    margin-bottom: 10px;
  }
  .stat-value {
    color: #1e293b;
    font-size: 28px;
    font-weight: 700;
    z-index: 1;
    display: flex;
    align-items: center;
    
    .unit { font-size: 14px; color: #64748b; margin-left: 4px; }
    .el-tag { font-size: 18px; border-radius: 8px; padding: 4px 12px; }
  }
  .card-bg-icon {
    position: absolute;
    right: -10px;
    bottom: -10px;
    font-size: 80px;
    opacity: 0.05;
    color: #3b82f6;
  }
}

.editor-card {
  border-radius: 16px;
  border: none;
  box-shadow: 0 4px 20px rgba(0,0,0,0.04);
  
  .card-header {
    padding: 20px 0;
    border-bottom: 1px solid #f1f5f9;
    margin-bottom: 30px;
    .title { font-size: 18px; font-weight: 600; color: #334155; }
  }
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #475569;
  margin-bottom: 24px;
  display: flex;
  align-items: center;
  i { margin-right: 8px; color: #3b82f6; }
}

.form-footer {
  margin-top: 40px;
  padding-top: 30px;
  border-top: 1px solid #f1f5f9;
  text-align: center;
  
  .submit-btn {
    padding: 12px 40px;
    font-size: 16px;
    font-weight: 600;
    border-radius: 12px;
    box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
  }
}

::v-deep .el-form-item__label {
  font-weight: 600;
  color: #64748b;
  padding-bottom: 8px;
}

/* 适配富文本编辑器间距 */
::v-deep .editor-wrapper {
  margin-bottom: 0;
}
</style>
