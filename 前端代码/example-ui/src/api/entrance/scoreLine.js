import request from '@/utils/request'

// 查询专业分数线列表
export function listScoreLine(query) {
  return request({
    url: '/entrance/scoreLine/list',
    method: 'get',
    params: query
  })
}

// 查询专业分数线详细
export function getScoreLine(id) {
  return request({
    url: '/entrance/scoreLine/' + id,
    method: 'get'
  })
}

// 新增专业分数线
export function addScoreLine(data) {
  return request({
    url: '/entrance/scoreLine',
    method: 'post',
    data: data
  })
}

// 修改专业分数线
export function updateScoreLine(data) {
  return request({
    url: '/entrance/scoreLine',
    method: 'put',
    data: data
  })
}

// 删除专业分数线
export function delScoreLine(id) {
  return request({
    url: '/entrance/scoreLine/' + id,
    method: 'delete'
  })
}
