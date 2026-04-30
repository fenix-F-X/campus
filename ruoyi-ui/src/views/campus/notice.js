import request from '@/utils/request'

// 查询公告列表
export function listNotice(query) {
  return request({
    url: '/campus/notice/list',
    method: 'get',
    params: query
  })
}

// 查询置顶公告
export function topNotices() {
  return request({
    url: '/campus/notice/top',
    method: 'get'
  })
}

// 查询公告详情
export function getNotice(noticeId) {
  return request({
    url: '/campus/notice/' + noticeId,
    method: 'get'
  })
}

// 新增公告
export function addNotice(data) {
  return request({
    url: '/campus/notice',
    method: 'post',
    data: data
  })
}

// 修改公告
export function updateNotice(data) {
  return request({
    url: '/campus/notice',
    method: 'put',
    data: data
  })
}

// 删除公告
export function delNotice(noticeId) {
  return request({
    url: '/campus/notice/' + noticeId,
    method: 'delete'
  })
}
