import Vue from 'vue'
import Router from 'vue-router'
/* Layout */
import Layout from '@/layout'

Vue.use(Router)

export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [{ path: '/redirect/:path(.*)', component: (resolve) => require(['@/views/redirect'], resolve) }]
  },
  { path: '/login', component: (resolve) => require(['@/views/login'], resolve), hidden: true },
  { path: '/register', component: (resolve) => require(['@/views/register'], resolve), hidden: true },
  { path: '/404', component: (resolve) => require(['@/views/error/404'], resolve), hidden: true },
  { path: '/401', component: (resolve) => require(['@/views/error/401'], resolve), hidden: true },
  {
    path: '',
    component: Layout,
    redirect: 'index',
    children: [
      {
        path: 'index',
        component: (resolve) => require(['@/views/index'], resolve),
        name: 'Index',
        meta: { title: '首页', icon: 'dashboard', affix: true }
      }
    ]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [
      {
        path: 'profile',
        component: (resolve) => require(['@/views/system/user/profile/index'], resolve),
        name: 'Profile',
        meta: { title: '个人中心', icon: 'user' }
      }
    ]
  },
  // --- 核心业务静态路由 (回归独立入口，解决 404) ---
  {
    path: '/college-view',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'list',
        component: (resolve) => require(['@/views/entrance/college/index'], resolve),
        name: 'StaticCollege',
        meta: { title: '院校查询', activeMenu: '/college/college' }
      },
      {
        path: 'detail/:id(\\d+)',
        component: (resolve) => require(['@/views/entrance/college/detail'], resolve),
        name: 'StaticCollegeDetail',
        meta: { title: '院校详情' }
      }
    ]
  },
  {
    path: '/profession-view',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'list',
        component: (resolve) => require(['@/views/entrance/profession/index'], resolve),
        name: 'StaticProfession',
        meta: { title: '专业查询' }
      }
    ]
  },
  {
    path: '/news-view',
    component: Layout,
    hidden: false,
    children: [
      {
        path: 'list',
        component: (resolve) => require(['@/views/entrance/news/index'], resolve),
        name: 'StaticNews',
        meta: { title: '政策咨询', icon: 'documentation' }
      }
    ]
  },
  {
    path: '/filling-view',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'list',
        component: (resolve) => require(['@/views/entrance/filling/index'], resolve),
        name: 'StaticFilling',
        meta: { title: '模拟填报' }
      },
      {
        path: 'apply',
        component: (resolve) => require(['@/views/entrance/aspiration/apply'], resolve),
        name: 'StaticApply',
        meta: { title: '在线填报' }
      }
    ]
  }
]

export default new Router({
  mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
