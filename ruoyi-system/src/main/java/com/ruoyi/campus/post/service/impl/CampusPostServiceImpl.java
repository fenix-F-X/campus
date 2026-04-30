// ruoyi-system/src/main/java/com/ruoyi/campus/post/service/impl/CampusPostServiceImpl.java
package com.ruoyi.campus.post.service.impl;

import com.ruoyi.campus.post.domain.CampusPost;
import com.ruoyi.campus.post.domain.CampusPostComment;
import com.ruoyi.campus.post.mapper.CampusPostCommentMapper;
import com.ruoyi.campus.post.mapper.CampusPostMapper;
import com.ruoyi.campus.post.service.ICampusPostService;
import com.ruoyi.common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CampusPostServiceImpl implements ICampusPostService {

    private final CampusPostMapper postMapper;
    private final CampusPostCommentMapper commentMapper;

    @Override
    public List<CampusPost> selectPostList(CampusPost post) {
        return postMapper.selectPostList(post);
    }

    @Override
    public CampusPost selectPostDetail(Long postId) {
        CampusPost post = postMapper.selectPostById(postId);
        if (post == null) {
            throw new ServiceException("帖子不存在");
        }
        // 浏览量+1
        post.setViewCount(post.getViewCount() + 1);
        postMapper.updateById(post);

        // 组装评论树
        List<CampusPostComment> topComments = commentMapper.selectCommentsByPostId(postId);
        topComments.forEach(c ->
                c.setChildren(commentMapper.selectChildrenByParentId(c.getCommentId()))
        );
        post.setComments(topComments);
        return post;
    }

    @Override
    public int insertPost(CampusPost post) {
        post.setStatus("1");
        post.setViewCount(0);
        post.setLikeCount(0);
        return postMapper.insert(post);
    }

    @Override
    public int updatePost(CampusPost post) {
        return postMapper.updateById(post);
    }

    @Override
    @Transactional
    public int deletePostById(Long postId) {
        // 删帖同时删评论
        postMapper.deleteById(postId);
        commentMapper.selectCommentsByPostId(postId)
                .forEach(c -> commentMapper.deleteById(c.getCommentId()));
        return 1;
    }

    @Override
    public int likePost(Long postId) {
        CampusPost post = postMapper.selectById(postId);
        if (post == null) {
            throw new ServiceException("帖子不存在");
        }
        post.setLikeCount(post.getLikeCount() + 1);
        return postMapper.updateById(post);
    }

    @Override
    public int addComment(CampusPostComment comment) {
        comment.setCreateTime(new Date());
        if (comment.getParentId() == null) {
            comment.setParentId(0L);
        }
        return commentMapper.insert(comment);
    }

    @Override
    public int deleteCommentById(Long commentId, Long userId) {
        CampusPostComment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            throw new ServiceException("评论不存在");
        }
        // 只能删自己的评论
        if (!comment.getAuthorId().equals(userId)) {
            throw new ServiceException("无权删除他人评论");
        }
        return commentMapper.deleteById(commentId);
    }
}