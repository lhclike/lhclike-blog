package com.lhclike.myblog.service;

import com.lhclike.myblog.vo.Result;
import com.lhclike.myblog.vo.TagVo;
import io.swagger.models.auth.In;


import java.util.List;

public interface TagsService {


    List<TagVo> findTagsByArticleId(Long id);
    Result findAll();
    Result findAllDetail();
    Result findDetailById(Long id);
    Result findHotTag(Integer limit);
}
