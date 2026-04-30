package com.ruoyi.campus.task.service.impl;

import com.ruoyi.campus.task.domain.CampusTask;
import com.ruoyi.campus.task.domain.CampusTaskRecord;
import com.ruoyi.campus.task.mapper.CampusTaskMapper;
import com.ruoyi.campus.task.mapper.CampusTaskRecordMapper;
import com.ruoyi.campus.task.service.ICampusTaskService;
import com.ruoyi.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampusTaskServiceImpl implements ICampusTaskService {

    private final CampusTaskMapper taskMapper;
    private final CampusTaskRecordMapper recordMapper;

    @Override
    public List<CampusTask> selectTaskList(CampusTask task) {
        return taskMapper.selectTaskList(task);
    }

    @Override
    public List<CampusTask> selectAvailableTasks(Long userId) {
        return taskMapper.selectAvailableTasks(userId);
    }

    @Override
    public int insertTask(CampusTask task) {
        task.setStatus("0");
        return taskMapper.insert(task);
    }

    @Override
    public int updateTask(CampusTask task) {
        return taskMapper.updateById(task);
    }

    @Override
    public int deleteTaskById(Long taskId) {
        return taskMapper.deleteById(taskId);
    }

    @Override
    @Transactional
    public int takeTask(Long taskId, Long userId) {
        CampusTask task = taskMapper.selectById(taskId);
        if (task == null || !"0".equals(task.getStatus())) {
            throw new ServiceException("任务不可接取");
        }
        // 检查是否已接取
        CampusTaskRecord existing = recordMapper.selectByTaskAndUser(taskId, userId);
        if (existing != null) {
            throw new ServiceException("您已接取该任务");
        }
        CampusTaskRecord record = new CampusTaskRecord();
        record.setTaskId(taskId);
        record.setTakerId(userId);
        record.setStatus("1");
        record.setCreateTime(new Date());
        recordMapper.insert(record);

        task.setStatus("1");
        return taskMapper.updateById(task);
    }

    @Override
    @Transactional
    public int submitTask(Long taskId, Long userId, String submitDesc) {
        CampusTaskRecord record = recordMapper.selectByTaskAndUser(taskId, userId);
        if (record == null || !"1".equals(record.getStatus())) {
            throw new ServiceException("无法提交该任务");
        }
        record.setStatus("2");
        record.setSubmitDesc(submitDesc);
        record.setSubmitTime(new Date());
        recordMapper.updateById(record);

        CampusTask task = taskMapper.selectById(taskId);
        task.setStatus("2");
        return taskMapper.updateById(task);
    }

    @Override
    @Transactional
    public int auditTask(Long recordId, String status, String remark, Long auditorId) {
        CampusTaskRecord record = recordMapper.selectById(recordId);
        if (record == null) {
            throw new ServiceException("记录不存在");
        }
        record.setStatus(status); // 3通过 4拒绝
        record.setAuditRemark(remark);
        record.setAuditorId(auditorId);
        record.setAuditTime(new Date());
        recordMapper.updateById(record);

        CampusTask task = taskMapper.selectById(record.getTaskId());
        task.setStatus("3".equals(status) ? "3" : "4");
        return taskMapper.updateById(task);
    }
}