// ruoyi-system/src/main/java/com/ruoyi/campus/task/mapper/CampusTaskMapper.java
package com.ruoyi.campus.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.campus.task.domain.CampusTask;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface CampusTaskMapper extends BaseMapper<CampusTask> {

    List<CampusTask> selectTaskList(CampusTask task);

    // 查询可接取的任务（status=0，未被当前用户接取）
    List<CampusTask> selectAvailableTasks(@Param("userId") Long userId);
}