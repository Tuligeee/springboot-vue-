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
            <marquee behavior="scroll" direction="left" scrollamount="5" v-if="noticeTitles">
              {{ noticeTitles }}
            </marquee>
            <span v-else>暂无系统公告</span>
          </div>
          <el-link type="warning" :underline="false" class="notice-more" @click="$router.push('/notice-view/list')">
            更多
          </el-link>
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

          <div class="modern-cards">
            <div class="modern-card" @click="$router.push('/college-view/list')">
              <div class="card-header">
                <div class="icon-wrapper bg-blue-light">
                  <i class="el-icon-school text-blue"></i>
                </div>
                <i class="el-icon-right arrow-icon"></i>
              </div>
              <div class="card-body">
                <h3 class="card-title">院校分数查询</h3>
                <p class="card-desc">权威的全国高校招生计划、专业设置及历年录取最低分数线明细数据查询。</p>
              </div>
            </div>

            <div class="modern-card" @click="$router.push('/user/profile')">
              <div class="card-header">
                <div class="icon-wrapper bg-green-light">
                  <i class="el-icon-user text-green"></i>
                </div>
                <i class="el-icon-right arrow-icon"></i>
              </div>
              <div class="card-body">
                <h3 class="card-title">个人档案管理</h3>
                <p class="card-desc">记录并管理个人高考成绩与位次信息，利用系统算法提供科学志愿参考建议。</p>
              </div>
            </div>

            <div class="modern-card" @click="$router.push('/news-view/list')">
              <div class="card-header">
                <div class="icon-wrapper bg-orange-light">
                  <i class="el-icon-reading text-orange"></i>
                </div>
                <i class="el-icon-right arrow-icon"></i>
              </div>
              <div class="card-body">
                <h3 class="card-title">政策资讯中心</h3>
                <p class="card-desc">全网最新汇总的各省市高考录取政策、查分动态、报考技巧及官方权威公告。</p>
              </div>
            </div>

            <div class="modern-card" @click="$router.push('/filling-view/list')">
              <div class="card-header">
                <div class="icon-wrapper bg-purple-light">
                  <i class="el-icon-document-checked text-purple"></i>
                </div>
                <i class="el-icon-right arrow-icon"></i>
              </div>
              <div class="card-body">
                <h3 class="card-title">志愿方案管理</h3>
                <p class="card-desc">支持创建多份独立的模拟志愿表，方便您进行多方案记录与保存，为您提供科学参考。</p>
              </div>
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
import { listPublicNotice } from "@/api/system/notice";
import AdminDashboard from './dashboard/AdminDashboard.vue';
import SchoolDashboard from './dashboard/SchoolDashboard.vue';

export default {
  name: "Index",
  components: { AdminDashboard, SchoolDashboard },
  data() {
    return {
      bannerList: [],
      newsList: [],
      noticeList: [],
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
    },
    noticeTitles() {
      if (!this.noticeList.length) {
        return "";
      }
      return this.noticeList.map(item => `【${item.noticeType === "1" ? "通知" : "公告"}】${item.noticeTitle}`).join("  ·  ");
    }
  },
  created() {
    if (!this.isAdmin) {
      this.getBannerList();
      this.getNewsList();
      this.getNoticeList();
    }
  },
  activated() {
    if (!this.isAdmin) {
      this.getBannerList();
      this.getNewsList();
      this.getNoticeList();
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
    },
    getNoticeList() {
      listPublicNotice({ pageNum: 1, pageSize: 6 }).then(response => {
        this.noticeList = response.rows || [];
      }).catch(() => {
        this.noticeList = [];
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
    .notice-more { margin-left: 12px; white-space: nowrap; }
  }

  .modern-cards {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 24px;
    margin-top: 25px;
  }

  .modern-card {
    background: #ffffff;
    border-radius: 16px;
    padding: 24px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.04);
    border: 1px solid rgba(0, 0, 0, 0.03);
    transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
    min-height: 160px;
    display: flex;
    flex-direction: column;

    &:hover {
      transform: translateY(-6px);
      box-shadow: 0 16px 30px rgba(64, 158, 255, 0.12);
      border-color: rgba(64, 158, 255, 0.2);
      
      .arrow-icon {
        opacity: 1;
        transform: translateX(0);
        color: #409EFF;
      }
    }

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      margin-bottom: 20px;
    }

    .icon-wrapper {
      width: 50px;
      height: 50px;
      border-radius: 14px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 26px;
      transition: all 0.3s;
      
      &.bg-blue-light { background: #ecf5ff; }
      &.bg-green-light { background: #f0f9eb; }
      &.bg-orange-light { background: #fdf6ec; }
      &.bg-purple-light { background: #f4f0fa; }
      
      .text-blue { color: #409EFF; }
      .text-green { color: #67C23A; }
      .text-orange { color: #E6A23C; }
      .text-purple { color: #9059f7; }
    }

    .arrow-icon {
      font-size: 22px;
      color: #c0c4cc;
      opacity: 0;
      transform: translateX(-15px);
      transition: all 0.3s ease;
    }

    .card-body {
      flex: 1;

      .card-title {
        font-size: 18px;
        font-weight: 600;
        color: #303133;
        margin: 0 0 10px 0;
        letter-spacing: 0.5px;
      }

      .card-desc {
        font-size: 13.5px;
        color: #909399;
        line-height: 1.6;
        margin: 0;
      }
    }
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

@media (max-width: 768px) { .home .modern-cards { grid-template-columns: 1fr; } }
</style>
