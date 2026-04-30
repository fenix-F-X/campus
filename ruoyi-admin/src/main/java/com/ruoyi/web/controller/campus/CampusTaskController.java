// ruoyi-admin/src/main/java/com/ruoyi/web/controller/campus/CampusTaskController.java
package com.ruoyi.web.controller.campus;

import com.ruoyi.campus.task.domain.CampusTask;
import com.ruoyi.campus.task.service.ICampusTaskService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/campus/task")
@RequiredArgsConstructor
public class CampusTaskController extends BaseController {

    private final ICampusTaskService taskService;

    /** 任务列表（管理员/辅导员查所有，学生查可接取） */
    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('campus:task:list')")
    public TableDataInfo list(CampusTask task) {
        startPage();
        return getDataTable(taskService.selectTaskList(task));
    }

    /** 学生查看可接取任务 */
    @GetMapping("/available")
    @PreAuthorize("@ss.hasPermi('campus:task:list')")
    public AjaxResult available() {
        Long userId = SecurityUtils.getUserId();
        return success(taskService.selectAvailableTasks(userId));
    }

    /** 发布任务 */
    @PostMapping
    @PreAuthorize("@ss.hasPermi('campus:task:add')")
    @Log(title = "任务管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody CampusTask task) {
        task.setPublisherId(SecurityUtils.getUserId());
        return toAjax(taskService.insertTask(task));
    }

    /** 修改任务 */
    @PutMapping
    @PreAuthorize("@ss.hasPermi('campus:task:edit')")
    @Log(title = "任务管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@RequestBody CampusTask task) {
        return toAjax(taskService.updateTask(task));
    }

    /** 接取任务 */
    @PostMapping("/take/{taskId}")
    @PreAuthorize("@ss.hasRole('student')")
    public AjaxResult take(@PathVariable Long taskId) {
        return toAjax(taskService.takeTask(taskId, SecurityUtils.getUserId()));
    }

    /** 提交任务 */
    @PostMapping("/submit/{taskId}")
    @PreAuthorize("@ss.hasRole('student')")
    public AjaxResult submit(@PathVariable Long taskId, @RequestBody Map<String, String> body) {
        return toAjax(taskService.submitTask(taskId, SecurityUtils.getUserId(), body.get("submitDesc")));
    }

    /** 审核任务 */
    @PostMapping("/audit/{recordId}")
    @PreAuthorize("@ss.hasAnyRole('admin','counselor')")
    public AjaxResult audit(@PathVariable Long recordId, @RequestBody Map<String, String> body) {
        return toAjax(taskService.auditTask(
                recordId,
                body.get("status"),
                body.get("remark"),
                SecurityUtils.getUserId()
        ));
    }

    /** 删除任务 */
    @DeleteMapping("/{taskId}")
    @PreAuthorize("@ss.hasPermi('campus:task:remove')")
    @Log(title = "任务管理", businessType = BusinessType.DELETE)
    public AjaxResult remove(@PathVariable Long taskId) {
        return toAjax(taskService.deleteTaskById(taskId));
    }
}