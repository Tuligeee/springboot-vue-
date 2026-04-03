import request from '@/utils/request'

// 获取大屏顶部总览统计数据
export function getStatisticsOverview() {
  return request({
    url: '/entrance/statistics/overview',
    method: 'get'
  })
}

// 获取大屏图表统计数据
export function getStatisticsChart() {
  return request({
    url: '/entrance/statistics/chart',
    method: 'get'
  })
}
