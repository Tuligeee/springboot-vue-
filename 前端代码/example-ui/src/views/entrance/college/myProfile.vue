<template>
  <div class="app-container my-profile-page">
    <el-card shadow="hover" class="page-card">
      <div slot="header" class="clearfix">
        <span class="page-title">
          <i class="el-icon-school" style="color: #409EFF; margin-right: 8px;"></i>
          本校资料维护
        </span>
      </div>

      <div v-loading="loading">
        <!-- 未绑定学校的提示 -->
        <el-empty v-if="!loading && !form.id" description="当前账号尚未绑定院校，请联系管理员分配。"></el-empty>

        <!-- 主体表单内容 -->
        <el-form v-if="form.id" ref="profileForm" :model="form" :rules="rules" label-width="100px">
          <!-- ====== 区域 1：学校概览预览卡 ====== -->
          <div class="profile-header-card">
            <div class="school-avatar">
              <i class="el-icon-school"></i>
            </div>
            <div class="school-meta">
              <h2 class="school-name-display">{{ form.collegeName || '未设置名称' }}</h2>
              <div class="school-tags">
                <el-tag size="small" type="info" effect="plain" v-if="form.collegeNo">
                  <i class="el-icon-document"></i> 院校代码：{{ form.collegeNo }}
                </el-tag>
                <el-tag size="small" type="success" effect="plain" v-if="form.city">
                  <i class="el-icon-location-outline"></i> {{ form.city }}
                </el-tag>
                <el-tag size="small" type="warning" effect="plain" v-if="form.ranking">
                  <i class="el-icon-medal"></i> 排名：{{ form.ranking }}
                </el-tag>
                <el-tag size="small" type="primary" effect="plain" v-if="form.personCount">
                  <i class="el-icon-user"></i> 在校人数：{{ form.personCount }}
                </el-tag>
              </div>
            </div>
          </div>

          <el-divider content-position="left">
            <i class="el-icon-edit-outline"></i> 基础信息
          </el-divider>

          <el-row :gutter="24">
            <el-col :span="12">
              <el-form-item label="院校名称" prop="collegeName">
                <el-input v-model="form.collegeName" placeholder="请输入院校名称" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="院校代码" prop="collegeNo">
                <el-input v-model="form.collegeNo" placeholder="请输入院校代码" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="24">
            <el-col :span="12">
              <el-form-item label="所在城市" prop="city">
                <el-input v-model="form.city" placeholder="请输入所在城市" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="全国排名">
                <el-input-number v-model="form.ranking" :min="1" :max="3000" controls-position="right" style="width: 100%;" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="24">
            <el-col :span="12">
              <el-form-item label="在校人数">
                <el-input-number v-model="form.personCount" :min="0" controls-position="right" style="width: 100%;" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-divider content-position="left">
            <i class="el-icon-reading"></i> 院校简介
          </el-divider>

          <el-form-item label-width="0">
            <editor v-model="form.detailInfo" :min-height="300" />
          </el-form-item>

          <!-- ====== 底部操作栏 ====== -->
          <div class="form-actions">
            <el-button type="primary" icon="el-icon-check" :loading="submitLoading" @click="handleSubmit">
              保存修改
            </el-button>
            <el-button icon="el-icon-refresh" @click="fetchProfile">
              重置
            </el-button>
          </div>
        </el-form>
      </div>
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
.my-profile-page {
  .page-title {
    font-weight: bold;
    font-size: 18px;
    color: #303133;
  }

  .profile-header-card {
    display: flex;
    align-items: center;
    background: linear-gradient(135deg, #e6f0ff 0%, #f0f9eb 100%);
    border-radius: 16px;
    padding: 30px;
    margin-bottom: 30px;

    .school-avatar {
      width: 80px;
      height: 80px;
      border-radius: 20px;
      background: linear-gradient(135deg, #409EFF, #67C23A);
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 25px;
      flex-shrink: 0;
      box-shadow: 0 4px 15px rgba(64, 158, 255, 0.3);

      i {
        font-size: 40px;
        color: #fff;
      }
    }

    .school-meta {
      .school-name-display {
        font-size: 26px;
        font-weight: bold;
        color: #1f2d3d;
        margin: 0 0 12px 0;
        letter-spacing: 1px;
      }

      .school-tags {
        display: flex;
        flex-wrap: wrap;
        gap: 10px;

        .el-tag {
          border-radius: 6px;
          font-size: 13px;
          padding: 4px 12px;
        }
      }
    }
  }

  .el-divider {
    margin: 30px 0 20px;

    ::v-deep .el-divider__text {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }
  }

  .form-actions {
    text-align: center;
    padding: 30px 0 10px;
    border-top: 1px solid #EBEEF5;
    margin-top: 30px;

    .el-button {
      padding: 12px 40px;
      font-size: 15px;
    }
  }
}
</style>
