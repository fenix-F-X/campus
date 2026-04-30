// ruoyi-system/src/main/java/com/ruoyi/campus/task/domain/CampusTaskRecord.java
package com.ruoyi.campus.task.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import java.util.Date;

@Data
@TableName("campus_task_record")
public class CampusTaskRecord {

    @TableId(type = IdType.AUTO)
    private Long recordId;

    private Long taskId;
    private Long takerId;

    /** 1进行中 2提交审核 3通过 4拒绝 */
    private String status;

    private String submitDesc;
    private Date submitTime;
    private Date auditTime;
    private Long auditorId;
    private String auditRemark;
    private Date createTime;
}