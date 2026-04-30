import request from '@/utils/request'

// 查询任务列表
export function listTask(query) {
  return request({
    url: '/campus/task/list',
    method: 'get',
    params: query
  })
}

// 查询可接取任务
export function availableTasks() {
  return request({
    url: '/campus/task/available',
    method: 'get'
  })
}

// 新增任务
export function addTask(data) {
  return request({
    url: '/campus/task',
    method: 'post',
    data: data
  })
}

// 修改任务
export function updateTask(data) {
  return request({
    url: '/campus/task',
    method: 'put',
    data: data
  })
}

// 删除任务
export function delTask(taskId) {
  return request({
    url: '/campus/task/' + taskId,
    method: 'delete'
  })
}

// 接取任务
export function takeTask(taskId) {
  return request({
    url: '/campus/task/take/' + taskId,
    method: 'post'
  })
}

// 提交任务
export function submitTask(taskId, data) {
  return request({
    url: '/campus/task/submit/' + taskId,
    method: 'post',
    data: data
  })
}

// 审核任务
export function auditTask(recordId, data) {
  return request({
    url: '/campus/task/audit/' + recordId,
    method: 'post',
    data: data
  })
}
