<template>
  <section class="portal-main">
    <transition name="fade-transform" mode="out-in">
      <keep-alive>
        <router-view :key="key" />
      </keep-alive>
    </transition>
  </section>
</template>

<script>
export default {
  name: 'PortalMain',
  computed: {
    key() {
      return this.$route.path
    }
  }
}
</script>

<style lang="scss" scoped>
.portal-main {
  /* header is 60px */
  min-height: calc(100vh - 60px);
  width: 100%;
  position: relative;
  overflow: hidden;
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px 20px;
}
</style>

<!-- 【修复4】Portal 下的子页面全局样式覆盖，消除套娃 Card -->
<style lang="scss">
.portal-main {
  /* 去掉子页面 .app-container 的额外 padding（后台默认 15px） */
  .app-container {
    padding: 0 !important;
  }

  /* 让最外层的 page-card 在 Portal 下变通透，融入背景 */
  .page-card {
    border: none !important;
    box-shadow: none !important;
    background: transparent !important;

    > .el-card__header {
      padding: 0 0 20px 0;
      border-bottom: none;
      
      span {
        font-size: 22px !important;
        font-weight: 700 !important;
      }
    }

    > .el-card__body {
      padding: 0 !important;
    }
  }

  /* 内层 el-card 保持正常圆角卡片样式 */
  .el-card {
    border-radius: 12px;
  }
}
</style>
