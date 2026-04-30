// ruoyi-admin/src/main/java/com/ruoyi/web/controller/campus/CampusPostController.java
package com.ruoyi.web.controller.campus;

import com.ruoyi.campus.post.domain.CampusPost;
import com.ruoyi.campus.post.domain.CampusPostComment;
import com.ruoyi.campus.post.service.ICampusPostService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/campus/post")
@RequiredArgsConstructor
public class CampusPostController extends BaseController {

    private final ICampusPostService postService;

    /** 帖子列表 */
    @GetMapping("/list")
    public TableDataInfo list(CampusPost post) {
        startPage();
        return getDataTable(postService.selectPostList(post));
    }

    /** 帖子详情（含评论树） */
    @GetMapping("/{postId}")
    public AjaxResult getDetail(@PathVariable Long postId) {
        return success(postService.selectPostDetail(postId));
    }

    /** 发帖 */
    @PostMapping
    @Log(title = "信息交流", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody CampusPost post) {
        post.setAuthorId(SecurityUtils.getUserId());
        return toAjax(postService.insertPost(post));
    }

    /** 修改帖子（仅本人） */
    @PutMapping
    @Log(title = "信息交流", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@RequestBody CampusPost post) {
        return toAjax(postService.updatePost(post));
    }

    /** 删除帖子 */
    @DeleteMapping("/{postId}")
    @Log(title = "信息交流", businessType = BusinessType.DELETE)
    public AjaxResult remove(@PathVariable Long postId) {
        return toAjax(postService.deletePostById(postId));
    }

    /** 点赞 */
    @PostMapping("/like/{postId}")
    public AjaxResult like(@PathVariable Long postId) {
        return toAjax(postService.likePost(postId));
    }

    /** 发表评论 */
    @PostMapping("/comment")
    public AjaxResult addComment(@RequestBody CampusPostComment comment) {
        comment.setAuthorId(SecurityUtils.getUserId());
        return toAjax(postService.addComment(comment));
    }

    /** 删除评论 */
    @DeleteMapping("/comment/{commentId}")
    public AjaxResult deleteComment(@PathVariable Long commentId) {
        return toAjax(postService.deleteCommentById(commentId, SecurityUtils.getUserId()));
    }

    /** 管理员下架帖子 */
    @PutMapping("/disable/{postId}")
    @PreAuthorize("@ss.hasPermi('campus:post:edit')")
    public AjaxResult disable(@PathVariable Long postId) {
        CampusPost post = new CampusPost();
        post.setPostId(postId);
        post.setStatus("0");
        return toAjax(postService.updatePost(post));
    }
}