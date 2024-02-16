package com.lhclike.myblog.api;

import com.lhclike.myblog.service.ArticleService;

import com.lhclike.myblog.vo.ArticleVo;
import com.lhclike.myblog.vo.Result;
import com.lhclike.myblog.vo.params.ArticleParam;
import com.lhclike.myblog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;
	//Result是统一结果t返回
    @CrossOrigin
    @PostMapping
    public Result articles(@RequestBody PageParams pageParams) {
        //ArticleVo 页面接收的数据
        List<ArticleVo> articles = articleService.listArticlesPage(pageParams);

        return Result.success(articles);
    }
    @PostMapping("new")
    public Result newArticles(){
        int limit=5;
        return  articleService.newArticles(limit);
    }
    @PostMapping("hot")
    public Result hotArticles(){
        int limit=5;
        return  articleService.hotArticles(limit);
    }



    @PostMapping("listArchives")
    public Result listArticles(){

        return  articleService.listArticles();
    }

    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long id){
        ArticleVo articleVo=articleService.findArticleById(id);
        return Result.success(articleVo);
    }

//    @PostMapping("publish")
//    public Result publish(@RequestBody ArticleParam articleParam){
//        return articleService.publish(articleParam);
//    }







}
