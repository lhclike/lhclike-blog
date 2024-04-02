package com.lhclike.myblog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lhclike.myblog.dao.ArticleMapper;
import com.lhclike.myblog.pojo.Article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ThreadService {


    @Async("taskExecutor")
    //事务性，要么同时成功要么同时失败，抛出任何异常都要回滚
    @Transactional(rollbackFor = Exception.class)
    public void updateViewCount(ArticleMapper articleMapper, Article article){
        boolean updated=false;
        int retryCount=0;
        int maxRetries=3;
        while(!updated &&retryCount<maxRetries){
            Article currentArticle=articleMapper.selectById(article.getId());
            if(currentArticle!=null){
                int currentViewCounts=currentArticle.getViewCounts();
                Article articleUpdate=new Article();
                articleUpdate.setViewCounts(currentViewCounts+1);

                LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
                queryWrapper.eq(Article::getId,article.getId());
                queryWrapper.eq(Article::getViewCounts,article.getViewCounts());
                // 尝试更新
                int updateCount = articleMapper.update(articleUpdate, queryWrapper);
                if (updateCount > 0) {
                    // 更新成功
                    updated = true;
                } else {
                    // 更新失败，增加重试计数
                    retryCount++;
                }
            } else {
                // 文章不存在
                break;
            }
            }


        try {
            //睡眠5秒 证明不会影响主线程的使用
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}