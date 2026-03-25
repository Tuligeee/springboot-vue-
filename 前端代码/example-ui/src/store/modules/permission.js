import {constantRoutes} from '@/router'
import {getRouters} from '@/api/menu'
import Layout from '@/layout/index'
import ParentView from '@/components/ParentView';
import InnerLink from '@/layout/components/InnerLink'

const permission = {
  state: {
    routes: [],
    addRoutes: [],
    defaultRoutes: [],
    topbarRouters: [],
    sidebarRouters: []
  },
  mutations: {
    SET_ROUTES: (state, routes) => {
      state.addRoutes = routes
      state.routes = constantRoutes.concat(routes)
    },
    SET_DEFAULT_ROUTES: (state, routes) => {
      state.defaultRoutes = constantRoutes.concat(routes)
    },
    SET_TOPBAR_ROUTES: (state, routes) => {
      state.topbarRouters = routes.concat([{ path: 'index', meta: { title: '首页', icon: 'dashboard' } }]);
    },
    SET_SIDEBAR_ROUTERS: (state, routes) => {
      state.sidebarRouters = routes
    },
  },
  actions: {
    GenerateRoutes({ commit }) {
      return new Promise(resolve => {
        getRouters().then(res => {
          const sdata = JSON.parse(JSON.stringify(res.data))
          const rdata = JSON.parse(JSON.stringify(res.data))
          const sidebarRoutes = filterAsyncRouter(sdata)
          const rewriteRoutes = filterAsyncRouter(rdata, false, true)
          rewriteRoutes.push({ path: '*', redirect: '/404', hidden: true })
          commit('SET_ROUTES', rewriteRoutes)
          commit('SET_SIDEBAR_ROUTERS', constantRoutes.concat(sidebarRoutes))
          commit('SET_DEFAULT_ROUTES', sidebarRoutes)
          commit('SET_TOPBAR_ROUTES', sidebarRoutes)
          resolve(rewriteRoutes)
        })
      })
    }
  }
}

function filterAsyncRouter(asyncRouterMap, lastRouter = false, type = false) {
  return asyncRouterMap.filter(route => {
    // 全局更名逻辑 (去管理化)
    if (route.meta && route.meta.title) {
      const titleMap = {
        '招生信息管理': '院校查询',
        '本校专业管理': '专业查询',
        '填报管理': '志愿模拟填报',
        '高考资讯管理': '政策资讯中心',
        '档线信息管理': '历年分数线查询',
        '轮播图管理': '首页内容展示',
        '志愿管理': '志愿单管理'
      };
      if (titleMap[route.meta.title]) {
        route.meta.title = titleMap[route.meta.title];
      } else if (route.meta.title.includes('管理') && !route.meta.title.includes('系统')) {
        route.meta.title = route.meta.title.replace('管理', '查询');
      }
    }

    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
    if (route.component) {
      if (route.component === 'Layout') {
        route.component = Layout
      } else if (route.component === 'ParentView') {
        route.component = ParentView
      } else if (route.component === 'InnerLink') {
        route.component = InnerLink
      } else {
        route.component = loadView(route.component)
      }
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children, route, type)
    } else {
      delete route['children']
      delete route['redirect']
    }
    return true
  })
}

function filterChildren(childrenMap, lastRouter = false) {
  var children = []
  childrenMap.forEach((el, index) => {
    if (el.children && el.children.length) {
      if (el.component === 'ParentView') {
        el.children.forEach(c => {
          c.path = el.path + '/' + c.path
          if (c.children && c.children.length) {
            children = children.concat(filterChildren(c.children, c))
            return
          }
          children.push(c)
        })
        return
      }
    }
    if (lastRouter) {
      el.path = lastRouter.path + '/' + el.path
    }
    children = children.concat(el)
  })
  return children
}

export const loadView = (view) => {
  return (resolve) => require([`@/views/${view}`], resolve)
}

export default permission
