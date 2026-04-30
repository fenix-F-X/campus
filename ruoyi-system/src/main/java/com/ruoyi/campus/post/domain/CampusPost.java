// ruoyi-system/src/main/java/com/ruoyi/campus/post/domain/CampusPost.java
package com.ruoyi.campus.post.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import java.util.List;

@Data
@TableName("campus_post")
public class CampusPost extends BaseEntity {

    @TableId(type = IdType.AUTO)
    private Long postId;

    private String title;
    private String content;
    private Long authorId;

    @TableField(exist = false)
    private String authorName;

    /** 失物招领/求助/闲置/其他 */
    private String category;

    private Integer viewCount;
    private Integer likeCount;

    /** 0下架 1正常 */
    private String status;

    @TableLogic
    @TableField("del_flag")
    private String delFlag;

    /** 评论列表，查详情时填充 */
    @TableField(exist = false)
    private List<CampusPostComment> comments;
}