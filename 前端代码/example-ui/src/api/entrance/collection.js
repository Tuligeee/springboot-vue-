import request from '@/utils/request'

// 查询收藏列表
export function listCollection(query) {
    return request({
        url: '/college_entrance/collection/list',
        method: 'get',
        params: query
    })
}

// 新增收藏
export function addCollection(data) {
    return request({
        url: '/college_entrance/collection',
        method: 'post',
        data: data
    })
}

// 删除收藏
export function delCollection(collectionId) {
    return request({
        url: '/college_entrance/collection/' + collectionId,
        method: 'delete'
    })
}

// 查询当前是否已收藏
export function checkCollect(params) {
    return request({
        url: '/college_entrance/collection/check',
        method: 'get',
        params
    })
}

// 切换收藏状态（已收藏则取消，未收藏则新增）
export function toggleCollect(data) {
    return request({
        url: '/college_entrance/collection/toggle',
        method: 'post',
        data
    })
}
