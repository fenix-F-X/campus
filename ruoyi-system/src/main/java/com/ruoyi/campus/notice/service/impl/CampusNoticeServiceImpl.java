package com.ruoyi.campus.notice.service.impl;

import com.ruoyi.campus.notice.domain.CampusNotice;
import com.ruoyi.campus.notice.mapper.CampusNoticeMapper;
import com.ruoyi.campus.notice.service.ICampusNoticeService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.core.redis.RedisCache;import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CampusNoticeServiceImpl implements ICampusNoticeService {

    private final CampusNoticeMapper noticeMapper;
    private final RedisCache redisCache;  // ← 替换 RedisTemplate

    private static final String CACHE_TOP_NOTICES = "campus:notice:top";
    private static final String CACHE_NOTICE_KEY  = "campus:notice:";
    private static final int    CACHE_TTL          = 30;

    @Override
    public List<CampusNotice> selectNoticeList(CampusNotice notice) {
        return noticeMapper.selectNoticeList(notice);
    }

    @Override
    public CampusNotice selectNoticeById(Long noticeId) {
        String key = CACHE_NOTICE_KEY + noticeId;
        CampusNotice cached = redisCache.getCacheObject(key);
        if (cached != null) {
            return cached;
        }
        CampusNotice notice = noticeMapper.selectById(noticeId);
        if (notice != null) {
            redisCache.setCacheObject(key, notice, CACHE_TTL, TimeUnit.MINUTES);
            notice.setViewCount(notice.getViewCount() + 1);
            noticeMapper.updateById(notice);
        }
        return notice;
    }

    @Override
    public int insertNotice(CampusNotice notice) {
        notice.setStatus("1");
        notice.setViewCount(0);
        int rows = noticeMapper.insert(notice);
        if ("1".equals(notice.getIsTop())) {
            redisCache.deleteObject(CACHE_TOP_NOTICES);
        }
        return rows;
    }

    @Override
    public int updateNotice(CampusNotice notice) {
        int rows = noticeMapper.updateById(notice);
        redisCache.deleteObject(CACHE_NOTICE_KEY + notice.getNoticeId());
        redisCache.deleteObject(CACHE_TOP_NOTICES);
        return rows;
    }

    @Override
    public int deleteNoticeById(Long noticeId) {
        redisCache.deleteObject(CACHE_NOTICE_KEY + noticeId);
        redisCache.deleteObject(CACHE_TOP_NOTICES);
        return noticeMapper.deleteById(noticeId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<CampusNotice> selectTopNotices() {
        List<CampusNotice> cached = redisCache.getCacheObject(CACHE_TOP_NOTICES);
        if (cached != null) {
            return cached;
        }
        CampusNotice query = new CampusNotice();
        query.setIsTop("1");
        query.setStatus("1");
        List<CampusNotice> list = noticeMapper.selectNoticeList(query);
        redisCache.setCacheObject(CACHE_TOP_NOTICES, list, CACHE_TTL, TimeUnit.MINUTES);
        return list;
    }
}