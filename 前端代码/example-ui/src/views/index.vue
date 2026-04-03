<template>
  <div class="home-wrapper">
    <admin-dashboard v-if="isGlobalAdmin" />
    <school-dashboard v-else-if="isSchoolAdmin" />
    <div v-else class="app-container home">

      <el-row :gutter="20" style="margin-bottom: 30px;" v-if="bannerList && bannerList.length > 0">
      <el-col :span="24">
        <el-carousel class="home-banner" style="border-radius: 10px; overflow: hidden; box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);">
          <el-carousel-item v-for="item in bannerList" :key="item.id">
            <a :href="item.linkUrl || 'javascript:void(0)'" :target="item.linkUrl && item.linkUrl.startsWith('http') ? '_blank' : '_self'" @click.prevent="!item.linkUrl.startsWith('http') && $router.push(item.linkUrl)" style="display: block; height: 100%; position: relative;">
              <el-image
                  :src="item.imgUrl"
                  fit="cover"
                  style="width: 100%; height: 100%;">
                <div slot="error" class="image-slot" style="display: flex; justify-content: center; align-items: center; height: 100%; background: #f5f7fa; color: #909399; font-size: 30px;">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </el-image>
              <div class="banner-title" v-if="item.title">{{ item.title }}</div>
            </a>
          </el-carousel-item>
        </el-carousel>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="24">
        <div class="notice-bar">
          <i class="el-icon-bell notice-icon"></i>
          <span class="notice-label">官方动态：</span>
          <div class="notice-content">
            <marquee behavior="scroll" direction="left" scrollamount="5">
              [最新] 2026年全国普通高校招生计划查询系统已开放 ... [通知] 关于做好2026年普通高校招生填报志愿工作的通知 ... [提醒] 请广大考生注意保护个人账号密码安全，切勿泄露给第三方。
            </marquee>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="30">
      <el-col :xs="24" :sm="24" :md="17" :lg="17">
        <div style="padding: 10px 0;">
          <h2 style="font-size: 28px; font-weight: bold; color: #1f2d3d; margin: 0 0 10px 0; display: flex; align-items: center;">
            <i class="el-icon-school" style="color: #409EFF; margin-right: 12px;"></i>
            欢迎使用高考志愿填报助手
          </h2>
          <p style="font-size: 16px; color: #909399; margin-bottom: 35px;">
            提供权威的院校招生计划与历年录取数据查询，协助您科学管理志愿方案：
          </p>

          <div class="step-grid">
            <div class="step-item" @click="$router.push('/college-view/list')">
              <div class="step-icon bg-blue"><i class="el-icon-search"></i></div>
              <div class="step-info">
                <h3>1. 院校数据查询</h3>
                <p>实时查询全国高校招生计划、专业设置及历年录取分数明细。</p>
              </div>
              <i class="el-icon-arrow-right arrow"></i>
            </div>

            <div class="step-item" @click="$router.push('/user/profile')">
              <div class="step-icon bg-green"><i class="el-icon-user"></i></div>
              <div class="step-info">
                <h3>2. 个人档案管理</h3>
                <p>记录个人高考成绩与位次信息，方便在查询过程中进行参考对比。</p>
              </div>
              <i class="el-icon-arrow-right arrow"></i>
            </div>

            <div class="step-item" @click="$router.push('/news-view/list')">
              <div class="step-icon bg-orange"><i class="el-icon-news"></i></div>
              <div class="step-info">
                <h3>3. 政策咨询中心</h3>
                <p>汇总各省市最新的高考录取政策、报考指南及官方公告信息。</p>
              </div>
              <i class="el-icon-arrow-right arrow"></i>
            </div>

            <div class="step-item" @click="$router.push('/filling-view/list')">
              <div class="step-icon bg-purple"><i class="el-icon-edit-outline"></i></div>
              <div class="step-info">
                <h3>4. 志愿方案管理</h3>
                <p>支持创建 5 份独立的模拟志愿表，方便您进行多方案记录与保存。</p>
              </div>
              <i class="el-icon-arrow-right arrow"></i>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :xs="24" :sm="24" :md="7" :lg="7">
        <el-card shadow="never" class="box-card" style="border-top: 4px solid #67C23A;">
          <div slot="header" class="clearfix">
            <span style="font-weight: bold; font-size: 17px; color: #303133;">
              <i class="el-icon-phone-outline" style="margin-right: 8px; color: #67C23A;"></i>
              官方咨询通道
            </span>
          </div>
          <div class="contact-info">
            <div class="contact-item">
              <div class="c-label">招生办咨询</div>
              <div class="c-value"><i class="el-icon-user"></i> 张老师</div>
            </div>
            <div class="contact-item">
              <div class="c-label">咨询热线</div>
              <div class="c-value" style="color: #409EFF; font-weight: bold; font-size: 18px;">
                <i class="el-icon-phone"></i> 010-88888888
              </div>
            </div>
            <div class="contact-item">
              <div class="c-label">办公时间</div>
              <div class="c-value"><i class="el-icon-time"></i> 周一至周五 09:00-17:00</div>
            </div>
            <el-divider></el-divider>
            <p style="font-size: 13px; color: #909399; line-height: 1.6; text-align: center;">
              * 请在办公时间内拨打咨询热线，或通过系统论坛留言。
            </p>
          </div>
        </el-card>
      </el-col>
      </el-col>
    </el-row>
    </div>
  </div>
</template>

<script>
import { listBanner } from "@/api/entrance/banner";
import { listNews } from "@/api/entrance/news";
import AdminDashboard from './dashboard/AdminDashboard.vue';
import SchoolDashboard from './dashboard/SchoolDashboard.vue';

export default {
  name: "Index",
  components: { AdminDashboard, SchoolDashboard },
  data() {
    return {
      bannerList: [],
      newsList: [],
    };
  },
  computed: {
    isGlobalAdmin() {
      const roles = this.$store.getters.roles || [];
      return roles.includes('admin');
    },
    isSchoolAdmin() {
      const roles = this.$store.getters.roles || [];
      return roles.includes('school_admin');
    },
    isAdmin() {
      return this.isGlobalAdmin || this.isSchoolAdmin;
    }
  },
  created() {
    if (!this.isAdmin) {
      this.getBannerList();
      this.getNewsList();
    }
  },
  activated() {
    if (!this.isAdmin) {
      this.getBannerList();
      this.getNewsList();
    }
  },
  methods: {
    getBannerList() {
      listBanner({ status: "0" }).then(response => {
        let rows = response.rows || response.data || [];
        if (rows.length === 0) {
          rows = [
            { id: 101, title: '圆梦名校 · 精准导航', imgUrl: '/img/banner/banner1.svg', linkUrl: '/college-view/list' },
            { id: 102, title: '科学填报 · 规划未来', imgUrl: '/img/banner/banner2.svg', linkUrl: '/filling-view/list' }
          ];
        }
        this.bannerList = rows;
      }).catch(() => {
        this.bannerList = [
          { id: 101, title: '圆梦名校 · 精准导航', imgUrl: '/img/banner/banner1.svg', linkUrl: '/college-view/list' },
          { id: 102, title: '科学填报 · 规划未来', imgUrl: '/img/banner/banner2.svg', linkUrl: '/filling-view/list' }
        ];
      });
    },
    getNewsList() {
      listNews({ pageNum: 1, pageSize: 5 }).then(response => {
        this.newsList = response.rows || response.data || [];
      });
    }
  }
};
</script>

<style scoped lang="scss">
.home {
  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  color: #676a6c;
  padding: 20px;

  .home-banner {
    width: 100%;
    aspect-ratio: 2.5 / 1;
    height: auto !important;
    ::v-deep .el-carousel__container { height: 100% !important; }
  }

  .banner-title {
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
    color: #ffffff;
    padding: 20px;
    font-size: 20px;
    font-weight: bold;
  }

  .notice-bar {
    display: flex;
    align-items: center;
    background: #FFF9E6;
    border: 1px solid #FFD591;
    border-radius: 8px;
    padding: 8px 15px;
    color: #E6A23C;
    font-size: 14px;
    .notice-icon { font-size: 18px; margin-right: 10px; }
    .notice-label { font-weight: bold; white-space: nowrap; margin-right: 15px; }
    .notice-content { flex: 1; overflow: hidden; }
  }

  .step-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 20px;
    margin-top: 20px;
  }

  .step-item {
    display: flex;
    align-items: center;
    background: #ffffff;
    border: 1px solid #e6ebf1;
    border-radius: 12px;
    padding: 20px;
    cursor: pointer;
    transition: all 0.3s;
    &:hover {
      transform: translateY(-5px);
      box-shadow: 0 12px 20px rgba(0,0,0,0.05);
      border-color: #409EFF;
      .arrow { transform: translateX(5px); color: #409EFF; }
    }
    .step-icon {
      width: 54px; height: 54px; border-radius: 12px;
      display: flex; justify-content: center; align-items: center;
      font-size: 24px; color: white; margin-right: 20px; flex-shrink: 0;
      &.bg-blue { background: linear-gradient(135deg, #6a11cb 0%, #2575fc 100%); }
      &.bg-green { background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%); }
      &.bg-orange { background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%); }
      &.bg-purple { background: linear-gradient(135deg, #89f7fe 0%, #66a6ff 100%); }
    }
    .step-info {
      h3 { margin: 0 0 5px 0; font-size: 17px; color: #303133; }
      p { margin: 0; font-size: 13px; color: #909399; line-height: 1.5; }
    }
    .arrow { margin-left: auto; color: #dcdfe6; font-size: 18px; transition: all 0.3s; }
  }

  .box-card { border-radius: 12px; border: 1px solid #f0f2f5; }

  .contact-info {
    padding: 10px 5px;
    .contact-item {
      margin-bottom: 20px;
      .c-label { font-size: 13px; color: #909399; margin-bottom: 8px; }
      .c-value { font-size: 15px; color: #303133; display: flex; align-items: center; i { margin-right: 10px; } }
    }
  }
}

@media (max-width: 768px) { .home .step-grid { grid-template-columns: 1fr; } }
</style>
