package com.lhclike.myblog.api;

import com.lhclike.myblog.common.aop.LogAnnotation;
import com.lhclike.myblog.common.cache.Cache;
import com.lhclike.myblog.service.ArticleService;

import com.lhclike.myblog.vo.ArticleVo;
import com.lhclike.myblog.vo.Result;
import com.lhclike.myblog.vo.params.ArticleParam;
import com.lhclike.myblog.vo.params.PageParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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


        return articleService.listArticlesPage(pageParams);
    }
    @PostMapping("new")
    public Result newArticles(){
        int limit=5;
        return  articleService.newArticles(limit);
    }
    @PostMapping("hot")
    @Cacheable(value = {"category"}, key = "#root.methodName")
    public Result hotArticles(){
        int limit=5;
        return  articleService.hotArticles(limit);
    }



    @PostMapping("listArchives")
//    @LogAnnotation(module ="文章",operation="获取文章列表")
    public Result listArchives(){

        return  articleService.listArchives();
    }

    @PostMapping("view/{id}")
    public Result findArticleById(@PathVariable("id") Long id){
        ArticleVo articleVo=articleService.findArticleById(id);
        return Result.success(articleVo);
    }

   @PostMapping("publish")
   public Result publish(@RequestBody ArticleParam articleParam){
       return articleService.publish(articleParam);
    }







}
