package com.tensquare.article.controller;

import com.tensquare.article.pojo.Comment;
import com.tensquare.article.service.CommentService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping
    public Result add(Comment comment){
        commentService.add(comment);
        return new Result(true, StatusCode.OK,"提交成功");
    }

    @GetMapping("/article/{articleId}")
    public Result findArticleById(@PathVariable("articleId")String articleId){
        List<Comment> list = commentService.findByArticleId(articleId);
        return new Result(true,StatusCode.OK,"查询成功",list);
    }

    @DeleteMapping("/{commentId}")
    public Result deleteComment(@PathVariable("commentId")String commentId){
        commentService.deleteComment(commentId);
        return new Result(true,StatusCode.OK,"删除成功");
    }
}
