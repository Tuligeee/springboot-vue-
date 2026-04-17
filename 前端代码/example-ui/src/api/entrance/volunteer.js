import request from '@/utils/request'

// 获取我的志愿列表
export function listMyVolunteer(query) {
    return request({
        url: '/entrance/volunteer/my',
        method: 'get',
        params: query
    })
}

// 添加志愿
export function addVolunteer(data) {
    return request({
        url: '/entrance/volunteer',
        method: 'post',
        data: data
    })
}

// 删除志愿
export function delVolunteer(id) {
    return request({
        url: '/entrance/volunteer/' + id,
        method: 'delete'
    })
}
