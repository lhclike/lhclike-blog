package com.lhclike.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.lhclike.myblog.dao.CommentMapper;
import com.lhclike.myblog.pojo.Comment;
import com.lhclike.myblog.pojo.SysUser;
import com.lhclike.myblog.service.CommentsService;
import com.lhclike.myblog.service.UserService;
import com.lhclike.myblog.util.UserThreadLocal;
import com.lhclike.myblog.vo.CommentParam;
import com.lhclike.myblog.vo.CommentVo;
import com.lhclike.myblog.vo.Result;
import com.lhclike.myblog.vo.UserVo;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsServiceImpl implements CommentsService{


    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserService userService;


    @Override
    public Result commentsByArticleId(Long articleId) {
        LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getArticleId,articleId);
        queryWrapper.eq(Comment::getLevel,1);
        List<Comment> comments=commentMapper.selectList(queryWrapper);
        return Result.success(copyList(comments));
    }

    @Override
    public Result comment(CommentParam commentParam) {
        SysUser sysUser= UserThreadLocal.get();
        Comment comment=new Comment();
        comment.setArticleId(commentParam.getArticleId());
        comment.setAuthorId(sysUser.getId());
        comment.setContent(commentParam.getContent());
        comment.setCreateDate(System.currentTimeMillis());
        Long parent=commentParam.getParent();
        if(parent==null ||parent==0){
            comment.setLevel(1);
        }else {
            comment.setLevel(2);
        }
        comment.setParentId(parent==null?0:parent);
        Long toUserId=commentParam.getToUserId();
        comment.setToUid(toUserId==null?0:toUserId);
        this.commentMapper.insert(comment);
        return Result.success(null);

    }

    public CommentVo copy(Comment comment){
        CommentVo commentVo=new CommentVo();
        BeanUtils.copyProperties(comment,commentVo);
        //时间格式化
        commentVo.setCreateDate(new DateTime(comment.getCreateDate()).toString("yyyy-MM-dd HH:mm"));
        Long authorId=comment.getAuthorId();
        UserVo userVo=userService.findUserVoById(authorId);
        commentVo.setAuthor(userVo);
        //评论的评论
        List<CommentVo> commentVoList=findCommentsByParentId(comment.getId());
        commentVo.setChildrens(commentVoList);
        if(comment.getLevel()>1){
            Long toUid=comment.getToUid();
            UserVo toUserVo=userService.findUserVoById(toUid);
            commentVo.setToUser(toUserVo);
        }
        return commentVo;
    }

    public List<CommentVo> copyList(List<Comment> commentList){
        List<CommentVo> commentVoList=new ArrayList<>();
        for(Comment comment: commentList){
            commentVoList.add(copy(comment));
        }
        return commentVoList;
    }
    private List<CommentVo> findCommentsByParentId(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getParentId,id);
        queryWrapper.eq(Comment::getLevel,2);
        List<Comment> comments = this.commentMapper.selectList(queryWrapper);
        return copyList(comments);
    }
}
