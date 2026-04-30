package com.ruoyi.campus.notice.service;

import com.ruoyi.campus.notice.domain.CampusNotice;
import java.util.List;

public interface ICampusNoticeService {
    List<CampusNotice> selectNoticeList(CampusNotice notice);
    CampusNotice selectNoticeById(Long noticeId);
    int insertNotice(CampusNotice notice);
    int updateNotice(CampusNotice notice);
    int deleteNoticeById(Long noticeId);/** 获取置顶/热点公告（走Redis缓存） */
    List<CampusNotice> selectTopNotices();
}