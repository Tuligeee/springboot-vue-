import request from '@/utils/request'

// 查询专业列表
export function listProfession(query) {
    return request({
        url: '/entrance/profession/list',
        method: 'get',
        params: query
    })
}

// 查询专业详细
export function getProfession(professionId) {
    return request({
        url: '/college_entrance/profession/' + professionId,
        method: 'get'
    })
}

// 新增专业
export function addProfession(data) {
    return request({
        url: '/college_entrance/profession',
        method: 'post',
        data: data
    })
}

// 修改专业
export function updateProfession(data) {
    return request({
        url: '/college_entrance/profession',
        method: 'put',
        data: data
    })
}

// 删除专业
export function delProfession(professionId) {
    return request({
        url: '/college_entrance/profession/' + professionId,
        method: 'delete'
    })
}
