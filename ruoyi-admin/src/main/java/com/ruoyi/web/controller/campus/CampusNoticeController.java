package com.ruoyi.web.controller.campus;

import com.ruoyi.campus.notice.domain.CampusNotice;
import com.ruoyi.campus.notice.service.ICampusNoticeService;
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
@RequestMapping("/campus/notice")
@RequiredArgsConstructor
public class CampusNoticeController extends BaseController {

    private final ICampusNoticeService noticeService;

    /** 公告列表 */
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('campus:notice:list')")
    public TableDataInfo list(CampusNotice notice) {
        startPage();
        return getDataTable(noticeService.selectNoticeList(notice));
    }

    /** 置顶公告（首页用，走Redis缓存） */
    @GetMapping("/top")
    public AjaxResult topList() {
        return success(noticeService.selectTopNotices());
    }

    /** 公告详情（走Redis缓存） */
    @GetMapping("/{noticeId}")
    public AjaxResult getInfo(@PathVariable Long noticeId) {
        return success(noticeService.selectNoticeById(noticeId));
    }

    /** 发布公告 */
    @PostMapping
    @PreAuthorize("@ss.hasPermi('campus:notice:add')")
    @Log(title = "公告管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody CampusNotice notice) {
        notice.setPublisherId(SecurityUtils.getUserId());
        return toAjax(noticeService.insertNotice(notice));
    }

    /** 修改公告 */
    @PutMapping
    @PreAuthorize("@ss.hasPermi('campus:notice:edit')")
    @Log(title = "公告管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@RequestBody CampusNotice notice) {
        return toAjax(noticeService.updateNotice(notice));
    }

    /** 删除公告 */
    @DeleteMapping("/{noticeId}")
    @PreAuthorize("@ss.hasPermi('campus:notice:remove')")
    @Log(title = "公告管理", businessType = BusinessType.DELETE)
    public AjaxResult remove(@PathVariable Long noticeId) {
        return toAjax(noticeService.deleteNoticeById(noticeId));
    }
}