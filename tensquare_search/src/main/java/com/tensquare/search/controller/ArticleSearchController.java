package com.tensquare.search.controller;

import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.ArticleSearchService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleSearchController {
    @Autowired
    private ArticleSearchService articleSearchService;

    @PostMapping
    public Result add(Article article){
        articleSearchService.add(article);
        return new Result(true, StatusCode.OK,"增加成功");
    }

    /**
     * 检索
     * @param keywords
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value="/search/{keywords}/{page}/{size}")
    public Result findByTitleOrContentLike(@PathVariable("keywords")String keywords,
                   @PathVariable("page")int page,@PathVariable("size")int size){
        Page<Article> articles = articleSearchService.findByTitleOrContentLike(keywords, page, size);
        PageResult<Article> pageResult = new PageResult<>(articles.getTotalElements(), articles.getContent());
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }
}
