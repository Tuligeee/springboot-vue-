import request from '@/utils/request'

export function listBanner(query) {
    return request({ url: '/entrance/banner/list', method: 'get', params: query })
}
export function getBanner(id) {
    return request({ url: '/entrance/banner/' + id, method: 'get' })
}
export function addBanner(data) {
    return request({ url: '/entrance/banner', method: 'post', data: data })
}
export function updateBanner(data) {
    return request({ url: '/entrance/banner', method: 'put', data: data })
}
export function delBanner(id) {
    return request({ url: '/entrance/banner/' + id, method: 'delete' })
}
