package com.ruoyi.campus.notice.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

@Data
@TableName("campus_notice")
public class CampusNotice extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long noticeId;

    private String title;
    private String content;

    /** 1通知 2活动 3紧急 */
    private String type;

    private Long publisherId;

    @TableField(exist = false)
    private String publisherName;

    private Integer viewCount;

    /** 是否置顶 0否 1是 */
    private String isTop;

    /** 0草稿 1发布 */
    private String status;

    @TableLogic
    @TableField("del_flag")
    private String delFlag;
}