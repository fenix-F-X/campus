// ruoyi-system/src/test/java/com/ruoyi/campus/CampusNoticeCacheTest.java
package com.ruoyi.campus;

import com.ruoyi.campus.notice.domain.CampusNotice;
import com.ruoyi.campus.notice.service.ICampusNoticeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CampusNoticeCacheTest {

    @Autowired
    private ICampusNoticeService noticeService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testCacheHit() {
        redisTemplate.delete("campus:notice:top");

        long t1 = System.currentTimeMillis();
        List<CampusNotice> list1 = noticeService.selectTopNotices();
        long dbTime = System.currentTimeMillis() - t1;

        long t2 = System.currentTimeMillis();
        List<CampusNotice> list2 = noticeService.selectTopNotices();
        long cacheTime = System.currentTimeMillis() - t2;

        assertEquals(list1.size(), list2.size());
        assertTrue("缓存应快于DB查询", cacheTime < dbTime);
        System.out.println("DB耗时:" + dbTime + "ms, 缓存耗时:" + cacheTime + "ms");
    }
}