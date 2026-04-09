<template>
  <div class="portal-navbar">
    <div class="nav-content">
      <!-- 左侧 Logo 和系统标题 -->
      <div class="logo-container" @click="$router.push('/index')">
        <i class="el-icon-reading" style="font-size: 28px; color: #409EFF; margin-right: 10px;"></i>
        <span class="system-title">高考志愿填报系统</span>
      </div>

      <!-- 中间核心导航菜单 -->
      <div class="menu-container">
        <el-menu
          :default-active="activeMenu"
          mode="horizontal"
          @select="handleSelect"
          class="portal-el-menu"
          text-color="#303133"
          active-text-color="#409EFF"
        >
          <el-menu-item index="/index">首页</el-menu-item>
          <el-menu-item index="/college-view/list">院校查询</el-menu-item>
          <el-menu-item index="/profession-view/list">专业查询</el-menu-item>
          <el-menu-item index="/score-view/list">历年分数线</el-menu-item>
          <el-menu-item index="/news-view/list">政策资讯</el-menu-item>
          <el-menu-item index="/notice-view/list">系统公告</el-menu-item>
          <el-menu-item index="/filling-view/list">模拟填报</el-menu-item>
        </el-menu>
      </div>

      <!-- 右侧头像与操作栏 -->
      <div class="right-menu">
        <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
          <div class="avatar-wrapper">
            <img :src="avatar" class="user-avatar" />
            <span class="user-name">{{ $store.getters.name }}</span>
            <i class="el-icon-caret-bottom" />
          </div>
          <el-dropdown-menu slot="dropdown">
            <router-link to="/user/profile">
              <el-dropdown-item icon="el-icon-user">个人中心</el-dropdown-item>
            </router-link>
            <router-link to="/my-view/collection">
              <el-dropdown-item icon="el-icon-star-off">我的收藏</el-dropdown-item>
            </router-link>
            <router-link to="/filling-view/list">
              <el-dropdown-item icon="el-icon-s-order">我的志愿</el-dropdown-item>
            </router-link>
            <!-- 仅管理员显示后台管理入口 -->
            <router-link to="/index" v-if="checkRole(['admin'])">
              <el-dropdown-item icon="el-icon-setting">后台管理</el-dropdown-item>
            </router-link>
            <el-dropdown-item divided icon="el-icon-switch-button" @click.native="logout">
              <span>退出登录</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import { checkRole } from "@/utils/permission"

export default {
  name: 'PortalNavbar',
  computed: {
    ...mapGetters(['avatar']),
    activeMenu() {
      const route = this.$route;
      const { meta, path } = route;
      if (meta.activeMenu) {
        return meta.activeMenu;
      }
      return path;
    }
  },
  methods: {
    checkRole,
    handleSelect(key, keyPath) {
      if (this.$route.path !== key) {
        this.$router.push(key)
      }
    },
    async logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/index';
        })
      }).catch(() => {});
    }
  }
}
</script>

<style lang="scss" scoped>
.portal-navbar {
  height: 60px;
  background-color: #fff;
  box-shadow: 0 1px 10px rgba(0, 0, 0, 0.05);
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 1000;

  .nav-content {
    max-width: 1200px;
    height: 100%;
    margin: 0 auto;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 20px;
  }

  .logo-container {
    display: flex;
    align-items: center;
    cursor: pointer;
    
    .system-title {
      font-size: 20px;
      font-weight: bold;
      color: #303133;
      letter-spacing: 1px;
    }
  }

  .menu-container {
    flex: 1;
    margin-left: 50px;
    
    .portal-el-menu {
      border-bottom: none;
      height: 60px;
      line-height: 60px;
      
      .el-menu-item {
        font-size: 16px;
        font-weight: 500;
        height: 60px;
        line-height: 60px;
        
        &:hover {
          background-color: transparent !important;
          color: #409EFF !important;
        }
      }
    }
  }

  .right-menu {
    display: flex;
    align-items: center;

    .avatar-wrapper {
      display: flex;
      align-items: center;
      cursor: pointer;
      padding: 0 10px;

      .user-avatar {
        width: 36px;
        height: 36px;
        border-radius: 50%;
        border: 2px solid #e6ebf5;
      }
      
      .user-name {
        margin-left: 10px;
        font-size: 14px;
        color: #606266;
        font-weight: 500;
      }

      .el-icon-caret-bottom {
        margin-left: 5px;
        font-size: 14px;
        color: #909399;
      }
    }
    
    .avatar-wrapper:hover {
      .user-name, .el-icon-caret-bottom {
        color: #409EFF;
      }
    }
  }
}
</style>
