package com.lhclike.myblog.service;

import com.lhclike.myblog.vo.ArticleBodyVo;
import com.lhclike.myblog.vo.ArticleVo;
import com.lhclike.myblog.vo.Result;
import com.lhclike.myblog.vo.params.ArticleParam;
import com.lhclike.myblog.vo.params.PageParams;


import java.util.List;

public interface ArticleService {

    Result listArticlesPage(PageParams pageParams);
    Result newArticles(int limit);
    Result hotArticles(int limit);
    Result listArchives();
    ArticleVo findArticleById(Long id);
    ArticleBodyVo findArticleBody(Long articleId);
    Result publish(ArticleParam artileParam);

}
