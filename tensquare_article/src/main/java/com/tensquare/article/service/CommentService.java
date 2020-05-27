package com.tensquare.article.service;

import com.tensquare.article.dao.CommentDao;
import com.tensquare.article.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.IdWorker;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentDao commentDao;

    @Autowired
    private IdWorker idWorker;

    public void add(Comment comment){
        comment.set_id(idWorker.nextId()+"");
        commentDao.save(comment);
    }

    /**
     * 根据文章ID查询评论列表
     * @param id
     * @return
     */
    public List<Comment> findByArticleId(String id){
        return commentDao.findByArticleid(id);
    }

    /**
     * 删除评论
     * @param id
     */
    public void deleteComment(String id){
        commentDao.deleteById(id);
    }
}
