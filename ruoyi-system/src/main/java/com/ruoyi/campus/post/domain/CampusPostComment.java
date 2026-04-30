// ruoyi-system/src/main/java/com/ruoyi/campus/post/domain/CampusPostComment.java
package com.ruoyi.campus.post.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
@TableName("campus_post_comment")
public class CampusPostComment {

    @TableId(type = IdType.AUTO)
    private Long commentId;

    private Long postId;
    private Long authorId;

    @TableField(exist = false)
    private String authorName;

    private String content;

    /** 0为顶级评论，否则为父评论ID */
    private Long parentId;

    /** 子评论列表 */
    @TableField(exist = false)
    private List<CampusPostComment> children;

    private Date createTime;

    @TableLogic
    @TableField("del_flag")
    private String delFlag;
}