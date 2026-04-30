// ruoyi-system/src/test/java/com/ruoyi/campus/CampusTaskServiceTest.java
package com.ruoyi.campus;

import com.ruoyi.campus.task.domain.CampusTask;
import com.ruoyi.campus.task.service.ICampusTaskService;
import com.ruoyi.common.exception.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CampusTaskServiceTest {

    @Autowired
    private ICampusTaskService taskService;

    private Long testTaskId;

    @Before
    public void setup() {
        CampusTask task = new CampusTask();
        task.setTitle("单元测试任务_" + System.currentTimeMillis());
        task.setContent("测试内容");
        task.setPublisherId(1L);
        task.setRewardPoints(10);
        task.setDeadline(new Date());
        taskService.insertTask(task);
        testTaskId = task.getTaskId();
    }

    @Test
    public void testInsertTask() {
        assertNotNull(testTaskId);}

    @Test
    public void testTakeTask() {
        int rows = taskService.takeTask(testTaskId, 2L);
        assertEquals(1, rows);
    }

    @Test
    public void testTakeTaskRepeat() {
        taskService.takeTask(testTaskId, 2L);
        try {
            taskService.takeTask(testTaskId, 2L);
            fail("应抛出异常");
        } catch (ServiceException e) {
            assertTrue(e.getMessage().contains("已接取"));
        }
    }

    @Test
    public void testTaskList() {
        List<CampusTask> list = taskService.selectTaskList(new CampusTask());
        assertFalse(list.isEmpty());
    }
}