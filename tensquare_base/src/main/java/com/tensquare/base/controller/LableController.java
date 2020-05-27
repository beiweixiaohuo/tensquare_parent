package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/label")
public class LableController {
    @Autowired
    private LabelService labelService;

    @GetMapping
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",labelService.findAll());
    }

    @GetMapping(value = "/{id}")
    public Result findById(@PathVariable("id")String id){
        return new Result(true, StatusCode.OK,"查询成功",labelService.findById(id));
    }

    @PostMapping
    public Result add(Label label){
        labelService.add(label);
        return new Result(true, StatusCode.OK,"增加成功");
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable("id")String id,Label label){
        label.setId(id);
        labelService.update(label);
        return new Result(true, StatusCode.OK,"修改成功");
    }

    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable("id")String id){
        labelService.deleteById(id);
        return new Result(true, StatusCode.OK,"删除成功");
    }
    @PostMapping("/search")
    public Result findSearch(Label label){
        return new Result(true,StatusCode.OK,"查询成功",labelService.findSearch(label));
    }

    @PostMapping("/search/{page}/{size}")
    public Result findSearch(Label label,@PathVariable("page") int page,@PathVariable("size")int size){
        return new Result(true,StatusCode.OK,"查询成功",labelService.findSearch(label,page,size));
    }
}
