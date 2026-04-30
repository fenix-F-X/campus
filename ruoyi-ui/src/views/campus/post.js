import request from '@/utils/request'

// 查询帖子列表
export function listPost(query) {
  return request({
    url: '/campus/post/list',
    method: 'get',
    params: query
  })
}

// 查询帖子详情
export function getPost(postId) {
  return request({
    url: '/campus/post/' + postId,
    method: 'get'
  })
}

// 新增帖子
export function addPost(data) {
  return request({
    url: '/campus/post',
    method: 'post',
    data: data
  })
}

// 修改帖子
export function updatePost(data) {
  return request({
    url: '/campus/post',
    method: 'put',
    data: data
  })
}

// 删除帖子
export function delPost(postId) {
  return request({
    url: '/campus/post/' + postId,
    method: 'delete'
  })
}

// 点赞
export function likePost(postId) {
  return request({
    url: '/campus/post/like/' + postId,
    method: 'post'
  })
}

// 发表评论
export function addComment(data) {
  return request({
    url: '/campus/post/comment',
    method: 'post',
    data: data
  })
}

// 删除评论
export function delComment(commentId) {
  return request({
    url: '/campus/post/comment/' + commentId,
    method: 'delete'
  })
}
