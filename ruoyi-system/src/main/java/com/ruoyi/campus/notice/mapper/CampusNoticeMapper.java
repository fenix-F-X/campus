package com.ruoyi.campus.notice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.campus.notice.domain.CampusNotice;
import java.util.List;

public interface CampusNoticeMapper extends BaseMapper<CampusNotice> {
    List<CampusNotice> selectNoticeList(CampusNotice notice);
}