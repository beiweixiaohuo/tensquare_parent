package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/spit")
public class SpitController {
    @Autowired
    private SpitService spitService;
    @Autowired
    private RedisTemplate redisTemplate;

    @PutMapping("thumbup/{spitId}")
    public Result updateThumbup(@PathVariable("spitId")String spitId){
        String userId = "2023";//当前登录用户，之后需要改
        if (redisTemplate.opsForValue().get("thumpbup_"+userId+"_"+spitId) != null){
            return new Result(false, StatusCode.REPERROR,"您已经点赞过了！");
        }
        spitService.updateThumbup(spitId);
        redisTemplate.opsForValue().set("thumpbup_"+userId+"_"+spitId,"1");
        return new Result(true, StatusCode.OK,"点赞成功");
    }
    /**
     * 根据上级ID查询吐槽分页数据
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/comment/{parentid}/{page}/{size}")
    public Result findByParentid(@PathVariable("parentid")String parentid,
                                 @PathVariable("page")int page,@PathVariable("size")int size){
        Page<Spit> pageList = spitService.findByParentid(parentid, page, size);
        PageResult<Spit> pageResult = new PageResult<Spit>(pageList.getTotalElements(),pageList.getContent());
        return new Result(true, StatusCode.OK,"查询成功",pageResult);
    }
    @GetMapping
    public Result findAll(){
        List<Spit> list = spitService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",list);
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable("id")String id){
        Spit spit = spitService.findById(id);
        return new Result(true, StatusCode.OK,"查询成功",spit);
    }

    @PutMapping("/{id}")
    public Result update(Spit spit,@PathVariable("id")String id){
        spit.set_id(id);
        spitService.update(spit);
        return new Result(true, StatusCode.OK,"修改成功");
    }

    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable("id")String id){
        spitService.deleteById(id);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    @PostMapping
    public Result add(Spit spit){
        spitService.add(spit);
        return new Result(true, StatusCode.OK,"添加成功");
    }
}
