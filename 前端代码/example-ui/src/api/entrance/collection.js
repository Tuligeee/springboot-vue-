import request from '@/utils/request'

export function checkCollect(query) {
    return request({ url: '/entrance/collection/check', method: 'get', params: query })
}

export function toggleCollect(data) {
    return request({ url: '/entrance/collection/toggle', method: 'post', data: data })
}

export function listCollection(query) {
    return request({ url: '/entrance/collection/list', method: 'get', params: query })
}
