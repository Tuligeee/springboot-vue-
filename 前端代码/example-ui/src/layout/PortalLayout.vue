<template>
  <div class="portal-wrapper">
    <portal-navbar />
    <div class="portal-container">
      <portal-main />
    </div>
    
    <!-- 可以补充一个简易的 Footer -->
    <div class="portal-footer">
      <p>&copy; 2026 高考志愿填报系统 All Rights Reserved.</p>
    </div>
  </div>
</template>

<script>
import PortalNavbar from './components/PortalNavbar'
import PortalMain from './components/PortalMain'

export default {
  name: 'PortalLayout',
  components: {
    PortalNavbar,
    PortalMain
  }
}
</script>

<style lang="scss" scoped>
.portal-wrapper {
  position: relative;
  height: 100%;
  width: 100%;
  background-color: #f5f7fa; /* 门户轻量级背景色 */
  overflow-y: auto;
  
  .portal-container {
    padding-top: 60px; /* offset the fixed header */
  }

  .portal-footer {
    text-align: center;
    padding: 30px 0;
    color: #909399;
    font-size: 14px;
    background-color: #fff;
    border-top: 1px solid #ebeef5;
    margin-top: 40px;
  }
}
</style>
