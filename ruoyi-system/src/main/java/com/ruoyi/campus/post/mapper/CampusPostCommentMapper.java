// ruoyi-system/src/main/java/com/ruoyi/campus/post/mapper/CampusPostCommentMapper.java
package com.ruoyi.campus.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.campus.post.domain.CampusPostComment;
import java.util.List;

public interface CampusPostCommentMapper extends BaseMapper<CampusPostComment> {
    /** 查询帖子下所有顶级评论（含作者名） */
    List<CampusPostComment> selectCommentsByPostId(Long postId);
    /** 查询某条评论的子评论 */
    List<CampusPostComment> selectChildrenByParentId(Long parentId);
}