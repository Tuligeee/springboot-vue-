<template>
  <div class="app-container home">

    <el-row :gutter="20" style="margin-bottom: 30px;" v-if="bannerList && bannerList.length > 0">
      <el-col :span="24">
        <el-carousel height="380px" style="border-radius: 10px; overflow: hidden; box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);">
          <el-carousel-item v-for="item in bannerList" :key="item.id">
            <a :href="item.linkUrl || 'javascript:void(0)'" :target="item.linkUrl ? '_blank' : '_self'" style="display: block; height: 100%; position: relative;">
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

    <el-row :gutter="20">
      <el-col :xs="24" :sm="24" :md="16" :lg="16">
        <div style="padding: 10px 20px;">
          <h2 style="font-size: 26px; font-weight: bold; color: #303133; margin-top: 0;">高考志愿填报系统</h2>
          <h4 style="font-size: 18px; font-weight: normal; color: #303133; margin-bottom: 30px;">同学们登录系统后按照以下步骤进行操作</h4>

          <el-timeline>
            <el-timeline-item timestamp="步骤 1" placement="top" color="#e4e7ed">
              <el-card shadow="hover" class="step-card">
                <h4>查学校、查专业</h4>
                <p>登录系统，在“院校信息”或“填报中心”查询自己感兴趣的学校和专业分数线。</p>
              </el-card>
            </el-timeline-item>

            <el-timeline-item timestamp="步骤 2" placement="top" color="#e4e7ed">
              <el-card shadow="hover" class="step-card">
                <h4>完善个人档案</h4>
                <p>在“个人中心”填写自己的高考分数、位次和生源地，这是智能推荐的基础。</p>
              </el-card>
            </el-timeline-item>

            <el-timeline-item timestamp="步骤 3" placement="top" color="#e4e7ed">
              <el-card shadow="hover" class="step-card">
                <h4>浏览资讯与攻略</h4>
                <p>在首页或资讯中心查看最新的高考政策和报考指南，获取一手信息。</p>
              </el-card>
            </el-timeline-item>

            <el-timeline-item timestamp="步骤 4" placement="top" color="#e4e7ed">
              <el-card shadow="hover" class="step-card">
                <h4>智能模拟填报</h4>
                <p>进入“填报中心”，使用智能推荐算法一键生成专属的高考志愿填报方案。</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </div>
      </el-col>

      <el-col :xs="24" :sm="24" :md="8" :lg="8">
        <el-card shadow="never" class="box-card" style="margin-bottom: 20px;">
          <div slot="header" class="clearfix" style="display: flex; justify-content: space-between; align-items: center;">
            <span style="font-weight: bold; font-size: 16px;"><i class="el-icon-document" style="margin-right: 5px;"></i>最新资讯</span>
            <el-link type="primary" :underline="false">更多 >></el-link>
          </div>
          <div style="text-align: center; color: #909399; padding: 40px 0;">
            暂无资讯发布
          </div>
        </el-card>

        <el-card shadow="never" class="box-card">
          <div slot="header" class="clearfix">
            <span style="font-weight: bold; font-size: 16px;"><i class="el-icon-phone-outline" style="margin-right: 5px;"></i>联系信息</span>
          </div>
          <div class="contact-info">
            <p><i class="el-icon-user-solid"></i> 招生办老师：张老师</p>
            <p><i class="el-icon-phone"></i> 咨询电话：010-88888888</p>
            <p><i class="el-icon-message"></i> 邮箱：service@gaokao.com</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listBanner } from "@/api/entrance/banner";

export default {
  name: "Index",
  data() {
    return {
      // 存放轮播图的数据
      bannerList: [],
    };
  },
  created() {
    // 页面一加载，就去拉取轮播图数据
    this.getBannerList();
  },
  methods: {
    /** 查询轮播图列表 */
    getBannerList() {
      // 请求 status="0" (正常) 的数据
      listBanner({ status: "0" }).then(response => {
        // 兼容不同的返回体结构，确保能拿到数组
        this.bannerList = response.rows || response.data || [];
      });
    }
  }
};
</script>

<style scoped lang="scss">
.home {
  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  color: #676a6c;
  overflow-x: hidden;
  padding: 20px;

  /* 轮播图底部黑色标题区域样式 */
  .banner-title {
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
    color: #ffffff;
    padding: 20px 20px 10px 20px;
    font-size: 20px;
    font-weight: bold;
    letter-spacing: 1px;
    text-align: left;
  }

  /* 时间轴卡片样式调整，使其更贴近截图效果 */
  .step-card {
    border-radius: 4px;
    h4 {
      margin-top: 0;
      margin-bottom: 10px;
      font-size: 16px;
      color: #303133;
    }
    p {
      margin: 0;
      line-height: 1.8;
      color: #606266;
      font-size: 14px;
    }
  }

  /* 右侧卡片基础样式 */
  .box-card {
    border-radius: 4px;
  }

  /* 联系信息行高与图标间距 */
  .contact-info {
    p {
      margin: 15px 0;
      font-size: 15px;
      color: #303133;
      display: flex;
      align-items: center;

      i {
        font-size: 18px;
        margin-right: 10px;
        color: #606266;
      }
    }
  }
}

/* 修改 Element UI 默认的 timeline 样式，让节点颜色变淡贴近截图 */
::v-deep .el-timeline-item__timestamp {
  color: #909399;
  font-size: 14px;
  padding-bottom: 8px;
}
::v-deep .el-timeline-item__tail {
  border-left-color: #e4e7ed;
}
</style>
