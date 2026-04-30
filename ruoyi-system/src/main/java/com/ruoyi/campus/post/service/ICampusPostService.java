// ruoyi-system/src/main/java/com/ruoyi/campus/post/service/ICampusPostService.java
package com.ruoyi.campus.post.service;

import com.ruoyi.campus.post.domain.CampusPost;
import com.ruoyi.campus.post.domain.CampusPostComment;
import java.util.List;

public interface ICampusPostService {
    List<CampusPost> selectPostList(CampusPost post);
    CampusPost selectPostDetail(Long postId);
    int insertPost(CampusPost post);
    int updatePost(CampusPost post);
    int deletePostById(Long postId);

    /** 点赞 */
    int likePost(Long postId);

    /** 发表评论 */
    int addComment(CampusPostComment comment);

    /** 删除评论 */
    int deleteCommentById(Long commentId, Long userId);
}