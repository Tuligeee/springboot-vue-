<template>
  <component :is="layoutComponent" />
</template>

<script>
import Layout from './index'
import PortalLayout from './PortalLayout'

export default {
  name: 'DynamicLayout',
  components: {
    Layout,
    PortalLayout
  },
  computed: {
    layoutComponent() {
      const roles = this.$store.getters.roles || [];
      // 如果包含管理员或学校管理员，则使用后台布局；否则（普通学生）使用前台门户布局
      if (roles.includes('admin') || roles.includes('school_admin')) {
        return Layout;
      }
      return PortalLayout;
    }
  }
}
</script>
