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
