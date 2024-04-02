package com.lhclike.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lhclike.myblog.dao.ArticleBodyMapper;
import com.lhclike.myblog.dao.ArticleMapper;

import com.lhclike.myblog.dao.ArticleTagMapper;
import com.lhclike.myblog.pojo.Article;
import com.lhclike.myblog.pojo.ArticleBody;
import com.lhclike.myblog.pojo.SysUser;
import com.lhclike.myblog.service.*;
import com.lhclike.myblog.util.UserThreadLocal;
import com.lhclike.myblog.vo.*;
import com.lhclike.myblog.vo.params.ArticleParam;
import com.lhclike.myblog.vo.params.PageParams;
import org.apache.commons.collections.functors.TruePredicate;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;


    @Autowired
    private ThreadService threadService;

    @Autowired
    private TagsService tagsService;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private  ArticleTagMapper articleTagMapper;


    @Autowired
    private UserService userService;
    private ArticleVo copy(Article article, boolean isTag, boolean isAuthor, boolean isBody, boolean isCategory){
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article,articleVo);

        articleVo.setCreateDate(new DateTime(article.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        //并不是所有的接口 都需要标签 ，作者信息
        if (isTag){
            Long articleId = article.getId();
            articleVo.setTags(tagsService.findTagsByArticleId(articleId));
        }
        if (isAuthor){
            Long authorId = article.getAuthorId();
            articleVo.setAuthor(userService.findUserById(authorId).getNickname());
        }
        if (isBody){
            ArticleBodyVo articleBody = findArticleBody(article.getId());
            articleVo.setBody(articleBody);
        }
        if (isCategory){
            CategoryVo categoryVo = findCategory(article.getCategoryId());
            articleVo.setCategory(categoryVo);
        }
        return articleVo;
    }
    private CategoryVo findCategory(Long categoryId) {
        return categoryService.findCategoryById(categoryId);
    }

    public ArticleBodyVo findArticleBody(Long articleId) {
        LambdaQueryWrapper<ArticleBody> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleBody::getArticleId,articleId);
        ArticleBody articleBody = articleBodyMapper.selectOne(queryWrapper);
        ArticleBodyVo articleBodyVo = new ArticleBodyVo();
        articleBodyVo.setContent(articleBody.getContent());
        return articleBodyVo;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,false,false));
        }
        return articleVoList;
    }

    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor,boolean isBody) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,isBody,false));
        }
        return articleVoList;
    }
    private List<ArticleVo> copyList(List<Article> records, boolean isTag, boolean isAuthor,boolean isBody,boolean isCategory) {
        List<ArticleVo> articleVoList = new ArrayList<>();
        for (Article record : records) {
            articleVoList.add(copy(record,isTag,isAuthor,isBody,isCategory));
        }
        return articleVoList;
    }



    @Override
    public Result listArticlesPage(PageParams pageParams) {
//        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//        //查询文章的参数 加上分类id，判断不为空 加上分类条件
//        if (pageParams.getCategoryId() != null) {
//            queryWrapper.eq(Article::getCategoryId,pageParams.getCategoryId());
//        }
//        List<Long> articleIdList = new ArrayList<>();
//        if (pageParams.getTagId() != null){
//            LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
//            articleTagLambdaQueryWrapper.eq(ArticleTag::getTagId,pageParams.getTagId());
//            List<ArticleTag> articleTags = articleTagMapper.selectList(articleTagLambdaQueryWrapper);
//            for (ArticleTag articleTag : articleTags) {
//                articleIdList.add(articleTag.getArticleId());
//            }
//            if (articleIdList.size() > 0){
//                queryWrapper.in(Article::getId,articleIdList);
//            }
//        }
//
//        //是否置顶进行排序
//        //order by create_date desc
//        queryWrapper.orderByDesc(Article::getWeight,Article::getCreateDate);
//        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
//        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
//        List<ArticleVo> articleVoList = copyList(articlePage.getRecords(),true,false,false);
//        return articleVoList;
        Page<Article> page = new Page<>(pageParams.getPage(),pageParams.getPageSize());
        IPage<Article> articleIPage = this.articleMapper.listArticle(page,pageParams.getCategoryId(),pageParams.getTagId(),pageParams.getYear(),pageParams.getMonth());
        return Result.success(copyList(articleIPage.getRecords(),true,true));
    }

    @Override
    public Result newArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getCreateDate);

        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        List<Article> articles=articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles,false,false,false,false));
    }

    @Override
    public Result hotArticles(int limit) {
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Article::getViewCounts);
        queryWrapper.select(Article::getId,Article::getTitle);
        queryWrapper.last("limit "+limit);
        List<Article> articles=articleMapper.selectList(queryWrapper);

        return Result.success(copyList(articles,false,false,false,false));
    }



    @Override
    public Result listArchives() {
        List<Article> articleList=articleMapper.listArchives();
        return Result.success(articleList);

    }



    @Override
    public ArticleVo findArticleById(Long id) {
        Article article = articleMapper.selectById(id);
        threadService.updateViewCount(articleMapper,article);
        return copy(article,true,true,true,true);
    }

    @Override
    @Transactional
    public Result publish(ArticleParam articleParam) {
        SysUser sysUser = UserThreadLocal.get();

        Article article = new Article();
        article.setAuthorId(sysUser.getId());
        article.setCategoryId(articleParam.getCategory().getId());
        article.setCreateDate(System.currentTimeMillis());
        article.setCommentCounts(0);
        article.setSummary(articleParam.getSummary());
        article.setTitle(articleParam.getTitle());
        article.setViewCounts(0);
        article.setWeight(Article.Article_Common);
        article.setBodyId(-1L);
        this.articleMapper.insert(article);

        //tags
        List<TagVo> tags = articleParam.getTags();
        if (tags != null) {
            for (TagVo tag : tags) {
                ArticleTag articleTag = new ArticleTag();
                articleTag.setArticleId(article.getId());
                articleTag.setTagId(tag.getId());
                articleTagMapper.insert(articleTag);
            }
        }
        ArticleBody articleBody = new ArticleBody();
        articleBody.setContent(articleParam.getBody().getContent());
        articleBody.setContentHtml(articleParam.getBody().getContentHtml());
        articleBody.setArticleId(article.getId());
        articleBodyMapper.insert(articleBody);

        article.setBodyId(articleBody.getId());
        articleMapper.updateById(article);
        ArticleVo articleVo = new ArticleVo();
        articleVo.setId(article.getId());
        return Result.success(articleVo);
    }


}
