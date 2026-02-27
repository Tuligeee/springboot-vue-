import request from '@/utils/request'

// 查询帖子列表
export function listPost() {
    return request({
        url: '/entrance/forum/list',
        method: 'get'
    })
}

// 查询帖子详情
export function getPost(id) {
    return request({
        url: '/entrance/forum/' + id,
        method: 'get'
    })
}

// 发布帖子
export function addPost(data) {
    return request({
        url: '/entrance/forum/add',
        method: 'post',
        data: data
    })
}

// 查询评论列表
export function listComment(postId) {
    return request({
        url: '/entrance/forum/comment/list/' + postId,
        method: 'get'
    })
}

// 发表评论
export function addComment(data) {
    return request({
        url: '/entrance/forum/comment/add',
        method: 'post',
        data: data
    })
}

export function delPost(id) {
    return request({
        url: '/entrance/forum/' + id,
        method: 'delete'
    })
}

// [新增] 删除评论
export function delComment(id) {
    return request({
        url: '/entrance/forum/comment/' + id,
        method: 'delete'
    })
}
