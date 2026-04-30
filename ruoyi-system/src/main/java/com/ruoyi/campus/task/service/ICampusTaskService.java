package com.ruoyi.campus.task.service;

import com.ruoyi.campus.task.domain.CampusTask;
import com.ruoyi.campus.task.domain.CampusTaskRecord;
import java.util.List;

public interface ICampusTaskService {

    List<CampusTask> selectTaskList(CampusTask task);

    List<CampusTask> selectAvailableTasks(Long userId);

    int insertTask(CampusTask task);

    int updateTask(CampusTask task);

    int deleteTaskById(Long taskId);

    /** 学生接取任务 */
    int takeTask(Long taskId, Long userId);

    /** 学生提交任务 */
    int submitTask(Long taskId, Long userId, String submitDesc);

    /** 审核任务（辅导员/管理员） */
    int auditTask(Long recordId, String status, String remark, Long auditorId);
}