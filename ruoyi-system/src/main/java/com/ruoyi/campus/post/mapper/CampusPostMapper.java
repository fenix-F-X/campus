// ruoyi-system/src/main/java/com/ruoyi/campus/post/mapper/CampusPostMapper.java
package com.ruoyi.campus.post.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.campus.post.domain.CampusPost;
import java.util.List;

public interface CampusPostMapper extends BaseMapper<CampusPost> {
    List<CampusPost> selectPostList(CampusPost post);
    CampusPost selectPostById(Long postId);
}