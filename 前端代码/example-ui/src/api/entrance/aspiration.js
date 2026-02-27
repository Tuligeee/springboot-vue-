import request from '@/utils/request'

// 专业测评
export function evaluate(query) {
    return request({
        url: '/college_entrance/aspiration/evaluate',
        method: 'get',
        params: query
    })
}

// 填报志愿
export function addForm(data) {
    return request({
        url: '/college_entrance/aspiration',
        method: 'post',
        data: data
    })
}

// 查询志愿
export function listAspiration(query) {
    return request({
        url: '/college_entrance/aspiration/list',
        method: 'get',
        params: query
    })
}

// 填报详情
export function aspirationDetail(studentNo){
    return request({
        url: '/college_entrance/aspiration/detail?studentNo='+ studentNo ,
        method: 'get'
    })
}

// 选项
export function selectItem() {
    return request({
        url: '/college_entrance/aspiration/selectItem',
        method: 'get'
    })
}

