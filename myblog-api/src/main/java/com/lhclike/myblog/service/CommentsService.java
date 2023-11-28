package com.lhclike.myblog.service;

import com.lhclike.myblog.vo.CommentParam;
import com.lhclike.myblog.vo.Result;

public interface CommentsService {


    Result commentsByArticleId(Long articleId);
    Result comment(CommentParam commentParam);
}