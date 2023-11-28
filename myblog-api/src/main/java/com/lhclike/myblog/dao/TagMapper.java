package com.lhclike.myblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lhclike.myblog.pojo.Tag;
import io.swagger.models.auth.In;

import java.util.List;

public interface TagMapper extends BaseMapper<Tag> {

    List<Tag> findTagsByArticleId(Long articleId);
    List<Tag> findHotTag(Integer limit);
}
