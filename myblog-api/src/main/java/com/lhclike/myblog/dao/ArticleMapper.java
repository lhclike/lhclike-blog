package com.lhclike.myblog.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lhclike.myblog.pojo.Article;


import java.util.List;

public interface ArticleMapper extends BaseMapper<Article> {
    List<Article> listArchives();
}