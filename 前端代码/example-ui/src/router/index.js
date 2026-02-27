import Vue from 'vue'
import Router from 'vue-router'
/* Layout */
import Layout from '@/layout'

Vue.use(Router)

/**
 * Note: 路由配置项
 *
 * hidden: true                     // 当设置 true 的时候该路由不会再侧边栏出现 如401，login等页面，或者如一些编辑页面/edit/1
 * alwaysShow: true                 // 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
 * // 只有一个时，会将那个子路由当做根路由显示在侧边栏--如引导页面
 * // 若你想不管路由下面的 children 声明的个数都显示你的根路由
 * // 你可以设置 alwaysShow: true，这样它就会忽略之前定义的规则，一直显示根路由
 * redirect: noRedirect             // 当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
 * name:'router-name'               // 设定路由的名字，一定要填写不然使用<keep-alive>时会出现各种问题
 * query: '{"id": 1, "name": "ry"}' // 访问路由的默认传递参数
 * meta : {
    noCache: true                   // 如果设置为true，则不会被 <keep-alive> 缓存(默认 false)
    title: 'title'                  // 设置该路由在侧边栏和面包屑中展示的名字
    icon: 'svg-name'                // 设置该路由的图标，对应路径src/assets/icons/svg
    breadcrumb: false               // 如果设置为false，则不会在breadcrumb面包屑中显示
    activeMenu: '/system/user'      // 当路由设置了该属性，则会高亮相对应的侧边栏。
  }
 */

// 公共路由
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [{
      path: '/redirect/:path(.*)',
      component: (resolve) => require(['@/views/redirect'], resolve)
    }]
  },
  {
    path: '/login',
    component: (resolve) => require(['@/views/login'], resolve),
    hidden: true
  },
  {
    path: '/register',
    component: (resolve) => require(['@/views/register'], resolve),
    hidden: true
  },
  {
    path: '/404',
    component: (resolve) => require(['@/views/error/404'], resolve),
    hidden: true
  },
  {
    path: '/401',
    component: (resolve) => require(['@/views/error/401'], resolve),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: '/index',
    children: [{
      path: 'index',
      component: (resolve) => require(['@/views/index'], resolve),
      name: 'Index',
      meta: {
        title: '首页',
        icon: 'dashboard',
        affix: true
      }
    }]
  },
  {
    path: '/user',
    component: Layout,
    hidden: true,
    redirect: 'noredirect',
    children: [{
      path: 'profile',
      component: (resolve) => require(['@/views/system/user/profile/index'], resolve),
      name: 'Profile',
      meta: {
        title: '个人中心',
        icon: 'user'
      }
    }]
  },

  // =================================================================
  //  业务路由配置区
  // =================================================================

  // 1. 高考资讯列表页 (通过首页点击“更多”跳转)
  {
    path: '/entrance/news',
    component: Layout,
    hidden: true, // 不显示在侧边栏
    children: [
      {
        path: 'list',
        // 请确保 src/views/entrance/news/list.vue 存在
        component: (resolve) => require(['@/views/entrance/news/list'], resolve),
        name: 'NewsList',
        meta: { title: '高考资讯列表' }
      }
    ]
  },

  // 2. 高考资讯详情页 (点击具体文章跳转)
  {
    path: '/news',
    component: Layout,
    hidden: true,
    children: [
      {
        path: 'detail/:id',
        // 【重要】如果不创建 src/views/entrance/news/detail.vue 文件，项目会报错无法启动！
        component: (resolve) => require(['@/views/entrance/news/detail'], resolve),
        name: 'NewsDetail',
        meta: { title: '资讯详情' }
      }
    ]
  },

  // 3. 交流论坛
  {
    path: '/forum',
    component: Layout,
    hidden: false, // 显示在侧边栏
    children: [
      {
        path: 'index',
        component: (resolve) => require(['@/views/entrance/forum/index'], resolve),
        name: 'Forum',
        meta: { title: '交流论坛', icon: 'message' }
      },
      {
        path: 'detail/:postId',
        component: (resolve) => require(['@/views/entrance/forum/detail'], resolve),
        name: 'ForumDetail',
        hidden: true,
        meta: { title: '帖子详情', activeMenu: '/forum/index' }
      }
    ]
  },

  // =================================================================
  //  系统路由配置区 (一般不需要改动)
  // =================================================================

  {
    path: '/system/user-auth',
    component: Layout,
    hidden: true,
    children: [{
      path: 'role/:userId(\\d+)',
      component: (resolve) => require(['@/views/system/user/authRole'], resolve),
      name: 'AuthRole',
      meta: {
        title: '分配角色',
        activeMenu: '/system/user'
      }
    }]
  },
  {
    path: '/system/role-auth',
    component: Layout,
    hidden: true,
    children: [{
      path: 'user/:roleId(\\d+)',
      component: (resolve) => require(['@/views/system/role/authUser'], resolve),
      name: 'AuthUser',
      meta: {
        title: '分配用户',
        activeMenu: '/system/role'
      }
    }]
  },
  {
    path: '/college',
    component: Layout,
    hidden: true, // 详情页不在侧边栏显示
    children: [
      {
        path: 'detail/:id', // :id 是占位符，代表学校ID
        component: (resolve) => require(['@/views/entrance/college/detail'], resolve),
        name: 'CollegeDetail',
        meta: { title: '院校详情' }
      }
    ]
  },
  {
    path: '/system/dict-data',
    component: Layout,
    hidden: true,
    children: [{
      path: 'index/:dictId(\\d+)',
      component: (resolve) => require(['@/views/system/dict/data'], resolve),
      name: 'Data',
      meta: {
        title: '字典数据',
        activeMenu: '/system/dict'
      }
    }]
  }
]

export default new Router({
  mode: 'history', // 去掉url中的#
  scrollBehavior: () => ({
    y: 0
  }),
  routes: constantRoutes
})
