package com.ruoyi.campus.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.campus.task.domain.CampusTaskRecord;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface CampusTaskRecordMapper extends BaseMapper<CampusTaskRecord> {

    List<CampusTaskRecord> selectRecordsByTaskId(@Param("taskId") Long taskId);

    CampusTaskRecord selectByTaskAndUser(@Param("taskId") Long taskId, @Param("takerId") Long takerId);
}