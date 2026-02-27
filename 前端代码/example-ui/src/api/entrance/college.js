import request from '@/utils/request'

// 查询院校列表
export function listCollege(query) {
    return request({
        url: '/college_entrance/college/list',
        method: 'get',
        params: query
    })
}

// 查询院校详细
export function getCollege(collegeId) {
    return request({
        url: '/college_entrance/college/' + collegeId,
        method: 'get'
    })
}

// 新增院校
export function addCollege(data) {
    return request({
        url: '/college_entrance/college',
        method: 'post',
        data: data
    })
}

// 修改院校
export function updateCollege(data) {
    return request({
        url: '/college_entrance/college',
        method: 'put',
        data: data
    })
}

// 删除院校
export function delCollege(collegeId) {
    return request({
        url: '/college_entrance/college/' + collegeId,
        method: 'delete'
    })
}
