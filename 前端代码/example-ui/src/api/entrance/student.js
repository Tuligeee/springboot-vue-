import request from '@/utils/request'

// 获取我的高考档案
export function getMyProfile() {
  return request({
    url: '/college_entrance/student/myProfile',
    method: 'get'
  })
}

// 更新我的高考档案
export function updateMyProfile(data) {
  return request({
    url: '/college_entrance/student/updateMyProfile',
    method: 'put',
    data: data
  })
}