import request from '@/utils/request'

// 填报志愿
export function addForm(data) {
    return request({
        url: '/college_entrance/aspiration/addFrom',
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
export function selectItem(sheetNo) {
    return request({
        url: '/college_entrance/aspiration/selectItem',
        method: 'get',
        params: { sheetNo }
    })
}

// 获取所有志愿单状态
export function listSheets() {
    return request({
        url: '/college_entrance/aspiration/listSheets',
        method: 'get'
    })
}

// 删除指定志愿单
export function delSheet(sheetNo) {
    return request({
        url: '/college_entrance/aspiration/removeSheet/' + sheetNo,
        method: 'delete'
    })
}

