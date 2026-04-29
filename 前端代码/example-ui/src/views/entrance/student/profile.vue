<template>
  <div class="profile-container">
    <!-- 头部横幅 - Hero Banner -->
    <div class="hero-banner">
      <div class="hero-content">
        <h1 class="hero-title">
          <i class="el-icon-medal"></i> 我的高考档案
        </h1>
        <p class="hero-desc">在此完善您的学术底座，系统将根据档案为您提供最精准的模拟填报分析</p>
      </div>
      <div class="hero-icon">
        <i class="el-icon-data-analysis"></i>
      </div>
    </div>

    <!-- 概览卡片区域 - Quick Info Cards -->
    <el-row :gutter="20" class="stat-row">
      <el-col :span="8">
        <div class="stat-card score">
          <div class="stat-label">预估总分</div>
          <div class="stat-value">{{ form.achievement || '--' }} <span class="unit">分</span></div>
          <i class="el-icon-trophy card-bg-icon"></i>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card subject-first">
          <div class="stat-label">首选科目</div>
          <div class="stat-value">
             <el-tag :type="form.subjectFirst === '物理' ? '' : 'danger'" effect="dark" v-if="form.subjectFirst">
               {{ form.subjectFirst }}
             </el-tag>
             <span v-else>未选择</span>
          </div>
          <i class="el-icon-reading card-bg-icon"></i>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="stat-card subject-second">
          <div class="stat-label">有效组合</div>
          <div class="stat-value">
            <template v-if="form.subjectSecondList && form.subjectSecondList.length">
              <el-tag v-for="s in form.subjectSecondList" :key="s" size="small" type="success" effect="plain" class="sub-tag">
                {{ s }}
              </el-tag>
            </template>
            <span v-else>未选定</span>
          </div>
          <i class="el-icon-magic-stick card-bg-icon"></i>
        </div>
      </el-col>
    </el-row>

    <!-- 核心表单区域 - Form Section -->
    <el-card shadow="never" class="editor-card">
      <div class="card-header">
        <span class="title">档案信息维护</span>
      </div>

      <el-form ref="form" :model="form" :rules="rules" label-position="top" v-loading="loading">
        <el-row :gutter="60">
          <el-col :span="8">
             <div class="section-title"><i class="el-icon-user"></i> 基础概况</div>
             <el-form-item label="性别选择" prop="sex">
               <el-radio-group v-model="form.sex" size="medium">
                 <el-radio-button label="MAN">男</el-radio-button>
                 <el-radio-button label="WOMEN">女</el-radio-button>
               </el-radio-group>
             </el-form-item>
             
             <el-form-item label="高考成绩 (总分)" prop="achievement" style="margin-top: 30px">
               <el-input-number 
                  v-model="form.achievement" 
                  :min="0" :max="750" 
                  placeholder="0 - 750" 
                  style="width: 100%"
                  controls-position="right"
                />
             </el-form-item>
          </el-col>

          <el-col :span="16">
            <div class="section-title"><i class="el-icon-collection"></i> 选科策略 (新高考组合)</div>
            
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="首选科目 (2 选 1)" prop="subjectFirst">
                  <el-select v-model="form.subjectFirst" placeholder="请选择主修方向" style="width: 100%">
                    <el-option label="物理" value="物理">
                      <span style="float: left">物理</span>
                      <span style="float: right; color: #8492a6; font-size: 13px">理工农医方向</span>
                    </el-option>
                    <el-option label="历史" value="历史">
                      <span style="float: left">历史</span>
                      <span style="float: right; color: #8492a6; font-size: 13px">文史哲社方向</span>
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              
              <el-col :span="12">
                <el-form-item label="再选科目 (4 选 2)" prop="subjectSecondList">
                  <div class="checkbox-group-wrapper">
                    <el-checkbox-group v-model="form.subjectSecondList" :max="2">
                      <el-checkbox-button label="化学">化学</el-checkbox-button>
                      <el-checkbox-button label="生物">生物</el-checkbox-button>
                      <el-checkbox-button label="思想政治">思想政治</el-checkbox-button>
                      <el-checkbox-button label="地理">地理</el-checkbox-button>
                    </el-checkbox-group>
                  </div>
                </el-form-item>
              </el-col>
            </el-row>

            <div class="policy-tip">
              <i class="el-icon-info"></i>
              <span>温馨提示：不同的选科组合将直接决定您可报考的大学专业范围，保存后系统将自动为您匹配。</span>
            </div>
          </el-col>
        </el-row>

        <div class="form-footer">
          <el-button type="primary" class="submit-btn" @click="submitForm" :loading="submitLoading">
            <i class="el-icon-upload"></i> 保存并更新档案
          </el-button>
          <el-button icon="el-icon-refresh" @click="resetForm" plain>重置</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { getMyProfile, updateMyProfile } from "@/api/entrance/student";

export default {
  name: "StudentProfile",
  data() {
    return {
      loading: false,
      submitLoading: false,
      form: {
        sex: "MAN",
        achievement: null,
        subjectFirst: "",
        subjectSecondList: [],
        subjectSecond: ""
      },
      rules: {
        achievement: [{ required: true, message: "请输入预估成绩", trigger: "blur" }],
        subjectFirst: [{ required: true, message: "请选择首选科目", trigger: "change" }],
        subjectSecondList: [
          { type: 'array', required: true, message: "请选择 2 门再选科目", trigger: "change", min: 2, max: 2 }
        ]
      }
    };
  },
  created() {
    this.getProfile();
  },
  methods: {
    getProfile() {
      this.loading = true;
      getMyProfile().then(res => {
        if (res.data) {
          const data = res.data;
          // --- 响应式修复：避免破坏 Vue 2 的数据劫持 ---
          Object.assign(this.form, data);
          
          // 解析再选科目字符串为数组，并去除可能的空格及空字符串
          let list = [];
          if (data.subjectSecond && data.subjectSecond.trim()) {
            list = data.subjectSecond.split(",").map(s => s.trim()).filter(Boolean);
          }
          this.$set(this.form, 'subjectSecondList', list);
        }
        this.loading = false;
      }).catch(() => { this.loading = false; });
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.submitLoading = true;
          // 标准化选科数据：清理空格并统一将“政治”归一化为“思想政治”
          const list = (this.form.subjectSecondList || []).map(s => s.trim()).filter(Boolean).map(s => s === '政治' ? '思想政治' : s);
          this.form.subjectSecond = list.join(",");
          
          updateMyProfile(this.form).then(res => {
            this.msgSuccess("档案已同步更新至云端");
            this.submitLoading = false;
            this.getProfile();
          }).catch(() => { this.submitLoading = false; });
        }
      });
    },
    resetForm() {
      this.getProfile();
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

.hero-banner {
  background: linear-gradient(135deg, #409EFF 0%, #1c92d2 48%, #409EFF 100%);
  border-radius: 16px;
  padding: 40px;
  color: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  box-shadow: 0 10px 25px rgba(64, 158, 255, 0.2);

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
    .unit { font-size: 14px; color: #64748b; margin-left: 4px; }
    .sub-tag { margin-right: 6px; }
  }
  .card-bg-icon {
    position: absolute;
    right: -10px;
    bottom: -10px;
    font-size: 80px;
    opacity: 0.05;
    color: #409EFF;
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
  i { margin-right: 8px; color: #409EFF; }
}

.checkbox-group-wrapper {
  margin-top: 4px;
  ::v-deep .el-checkbox-group {
    display: flex;
    flex-wrap: wrap;
    gap: 12px;
  }
  ::v-deep .el-checkbox-button {
    flex: 0 0 calc(50% - 6px);
    width: calc(50% - 6px);
  }
  ::v-deep .el-checkbox-button__inner {
    width: 100%;
    border-radius: 8px !important;
    border-left: 1px solid #DCDFE6 !important;
    padding: 12px 0;
    font-weight: 500;
    box-shadow: none !important;
    transition: all 0.3s;
    background-color: #f8fafc;
    border: 1px solid #e2e8f0;
    text-align: center;
  }
  ::v-deep .el-checkbox-button.is-checked .el-checkbox-button__inner {
    background-color: #409EFF;
    border-color: #409EFF;
    color: #fff;
  }
}

.policy-tip {
  margin-top: 30px;
  padding: 16px;
  background-color: #fffbeb;
  border: 1px solid #fef3c7;
  border-radius: 12px;
  color: #d97706;
  font-size: 13px;
  display: flex;
  align-items: center;
  i { font-size: 18px; margin-right: 10px; }
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
    box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
  }
}

::v-deep .el-form-item__label {
  font-weight: 600;
  color: #64748b;
  padding-bottom: 8px;
}
</style>
