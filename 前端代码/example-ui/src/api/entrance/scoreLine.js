import request from '@/utils/request'

// 查询各校专业录取分数线列表
export function listScoreLine(query) {
  return request({
    url: '/entrance/scoreLine/list',
    method: 'get',
    params: query
  })
}

// 查询详情
export function getScoreLine(id) {
  return request({
    url: '/entrance/scoreLine/' + id,
    method: 'get'
  })
}

// 新增
export function addScoreLine(data) {
  return request({
    url: '/entrance/scoreLine',
    method: 'post',
    data: data
  })
}

// 修改
export function updateScoreLine(data) {
  return request({
    url: '/entrance/scoreLine',
    method: 'put',
    data: data
  })
}

// 删除
export function delScoreLine(id) {
  return request({
    url: '/entrance/scoreLine/' + id,
    method: 'delete'
  })
}
