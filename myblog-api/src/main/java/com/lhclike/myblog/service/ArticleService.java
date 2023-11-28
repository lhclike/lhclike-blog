package com.lhclike.myblog.service;

import com.lhclike.myblog.vo.ArticleVo;
import com.lhclike.myblog.vo.Result;
import com.lhclike.myblog.vo.params.ArticleParam;
import com.lhclike.myblog.vo.params.PageParams;


import java.util.List;

public interface ArticleService {

    List<ArticleVo> listArticlesPage(PageParams pageParams);
    Result newArticles(int limit);
    Result hotArticles(int limit);
    Result listArticles();
    ArticleVo findArticleById(Long id);
    Result publish(ArticleParam articleParam);

}
