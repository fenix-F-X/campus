// ruoyi-system/src/main/java/com/ruoyi/campus/task/domain/CampusTask.java
package com.ruoyi.campus.task.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import java.util.Date;

@Data
@TableName("campus_task")



public class CampusTask extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long taskId;

    private String title;
    private String content;
    /**
     * 任务ID，使用@TableId注解标记为主键
     *
     */
    private Long publisherId;
    private Long deptId;

    /** 任务标题 */
    /** 0待接取 1进行中 2待审核 3已完成 4已拒绝 */
    /** 任务内容 */
    private String status;
    /** 发布者ID */

    /** 部门ID */
    private Integer rewardPoints;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deadline;

    @TableLogic
    @TableField("del_flag")
    private String delFlag;


    // 加在 CampusTask.java 中，非数据库字段
    @TableField(exist = false)
    private String publisherName;


}