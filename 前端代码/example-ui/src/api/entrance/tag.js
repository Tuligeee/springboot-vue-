import request from '@/utils/request'

// 查询标签列表
export function getTags(tagType, relId) {
    return request({
        url: '/college_entrance/tag/' + tagType + '/' + relId,
        method: 'get'
    })
}

// 绑定标签
export function bindTags(data) {
    return request({
        url: '/college_entrance/tag',
        method: 'post',
        data: data
    })
}
