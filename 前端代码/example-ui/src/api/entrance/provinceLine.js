import request from '@/utils/request'

// 查询档线列表
export function listProvinceLine(query) {
    return request({
        url: '/entrance/provinceLine/list',
        method: 'get',
        params: query
    })
}

// 查询档线详细
export function getProvinceLine(id) {
    return request({
        url: '/entrance/provinceLine/' + id,
        method: 'get'
    })
}

// 新增档线
export function addProvinceLine(data) {
    return request({
        url: '/entrance/provinceLine',
        method: 'post',
        data: data
    })
}

// 修改档线
export function updateProvinceLine(data) {
    return request({
        url: '/entrance/provinceLine',
        method: 'put',
        data: data
    })
}

// 删除档线
export function delProvinceLine(id) {
    return request({
        url: '/entrance/provinceLine/' + id,
        method: 'delete'
    })
}
