import request from '@/utils/request'

// 查询历年分数线列表
export function listProvinceScore(query) {
  return request({
    url: '/entrance/provinceScore/list',
    method: 'get',
    params: query
  })
}

// 查询历年分数线详细
export function getProvinceScore(id) {
  return request({
    url: '/entrance/provinceScore/' + id,
    method: 'get'
  })
}

// 新增历年分数线
export function addProvinceScore(data) {
  return request({
    url: '/entrance/provinceScore',
    method: 'post',
    data: data
  })
}

// 修改历年分数线
export function updateProvinceScore(data) {
  return request({
    url: '/entrance/provinceScore',
    method: 'put',
    data: data
  })
}

// 删除历年分数线
export function delProvinceScore(id) {
  return request({
    url: '/entrance/provinceScore/' + id,
    method: 'delete'
  })
}
