import Vue from 'vue'
import Router from 'vue-router'
/* Layout */
import Layout from '@/layout'
import PortalLayout from '@/layout/PortalLayout'
import DynamicLayout from '@/layout/DynamicLayout'

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
    component: DynamicLayout,
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
  // --- Admin 论坛隐藏路由 ---
  {
    path: '/entrance/forum',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'detail/:postId(\\d+)',
        component: (resolve) => require(['@/views/entrance/forum/detail'], resolve),
        name: 'AdminForumDetail',
        meta: { title: '帖子详情', activeMenu: '/forum' } // sys_menu path is /forum
      }
    ]
  },
  // --- 【修复1】个人中心：使用 DynamicLayout，学生看到Portal，管理员看到后台 ---
  {
    path: '/user',
    component: DynamicLayout,
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
  // --- 学校用户专属路由 ---
  {
    path: '/school',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'profile',
        component: (resolve) => require(['@/views/entrance/college/myProfile'], resolve),
        name: 'SchoolProfile',
        meta: { title: '本校资料维护', icon: 'education' }
      }
    ]
  },
  // --- 核心C端业务路由（全部归入 PortalLayout）---
  {
    path: '/college-view',
    component: PortalLayout,
    hidden: true,
    children: [
      {
        path: 'list',
        component: (resolve) => require(['@/views/entrance/college/index'], resolve),
        name: 'StaticCollege',
        meta: { title: '院校查询', activeMenu: '/college-view/list' }
      },
      {
        path: 'detail/:id(\\d+)',
        component: (resolve) => require(['@/views/entrance/college/detail'], resolve),
        name: 'StaticCollegeDetail',
        meta: { title: '院校详情', activeMenu: '/college-view/list' }
      }
    ]
  },
  {
    path: '/profession-view',
    component: PortalLayout,
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
    component: PortalLayout,
    hidden: true,
    children: [
      {
        path: 'list',
        component: (resolve) => require(['@/views/entrance/news/index'], resolve),
        name: 'StaticNews',
        meta: { title: '政策咨询', icon: 'documentation' }
      },
      // 【修复2】资讯详情页注册到 Portal 路由
      {
        path: 'detail/:id(\\d+)',
        component: (resolve) => require(['@/views/entrance/news/detail'], resolve),
        name: 'StaticNewsDetail',
        meta: { title: '资讯详情', activeMenu: '/news-view/list' }
      }
    ]
  },
  {
    path: '/notice-view',
    component: PortalLayout,
    hidden: true,
    children: [
      {
        path: 'list',
        component: (resolve) => require(['@/views/entrance/notice/index'], resolve),
        name: 'StaticNotice',
        meta: { title: '系统公告', icon: 'message' }
      }
    ]
  },
  {
    path: '/filling-view',
    component: PortalLayout,
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
  },
  {
    path: '/forum-view',
    component: PortalLayout,
    hidden: true,
    children: [
      {
        path: 'list',
        component: (resolve) => require(['@/views/entrance/forum/index'], resolve),
        name: 'StaticForum',
        meta: { title: '交流论坛', activeMenu: '/forum-view/list' }
      },
      {
        path: 'detail/:postId(\\d+)',
        component: (resolve) => require(['@/views/entrance/forum/detail'], resolve),
        name: 'StaticForumDetail',
        meta: { title: '帖子详情', activeMenu: '/forum-view/list' }
      }
    ]
  },
  // 【修复3】收藏中心、我的志愿、历年分数线 全部归入 PortalLayout
  {
    path: '/my-view',
    component: PortalLayout,
    hidden: true,
    children: [
      {
        path: 'collection',
        component: (resolve) => require(['@/views/entrance/myCollection/index'], resolve),
        name: 'StaticCollection',
        meta: { title: '我的收藏' }
      },
      {
        path: 'aspiration',
        component: (resolve) => require(['@/views/entrance/aspiration/index'], resolve),
        name: 'StaticAspiration',
        meta: { title: '我的志愿' }
      },
      {
        path: 'student-profile',
        component: (resolve) => require(['@/views/entrance/student/profile'], resolve),
        name: 'StaticStudentProfile',
        meta: { title: '高考档案' }
      }
    ]
  },
  {
    path: '/score-view',
    component: PortalLayout,
    hidden: true,
    children: [
      {
        path: 'list',
        component: (resolve) => require(['@/views/entrance/provinceScore/index'], resolve),
        name: 'StaticProvinceScore',
        meta: { title: '历年分数线' }
      }
    ]
  }
]

export default new Router({
  mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})
