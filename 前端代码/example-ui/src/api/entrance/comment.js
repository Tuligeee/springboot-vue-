import request from '@/utils/request'

// 获取评论列表
export function listComment(query) {
    return request({
        url: '/entrance/comment/list',
        method: 'get',
        params: query
    })
}

// 发表评论
export function addComment(data) {
    return request({
        url: '/entrance/comment/add',
        method: 'post',
        data: data
    })
}

// 删除评论
export function delComment(id) {
    return request({
        url: '/entrance/comment/' + id,
        method: 'delete'
    })
}
