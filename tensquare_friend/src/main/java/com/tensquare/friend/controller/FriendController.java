package com.tensquare.friend.controller;

import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private FriendService friendService;

    @Autowired
    private HttpServletRequest request;

    @PutMapping("/like/{friendid}/{type}")
    public Result addFriend(@PathVariable("friendid")String friendid
            ,@PathVariable("type")String type){
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims==null){
            return new Result(false, StatusCode.ACCESSERROR,"权限不足");
        }
        if (type.equals("1")){//添加好友
            int i = friendService.addFriend(claims.getId(), friendid);
            if (i == 0){
                return new Result(false,StatusCode.ERROR,"不能重复添加好友");
            }
        }else if (type.equals("0")){//添加非好友
            friendService.addNoFriend(claims.getId(),friendid);
        }
        else
            return new Result(false,StatusCode.ERROR,"参数异常");

        return new Result(true,StatusCode.OK,"添加成功");
    }

    @DeleteMapping("/{friendid}")
    public Result remove(@PathVariable("friendid")String friendid){
        Claims claims = (Claims) request.getAttribute("user_claims");
        if (claims==null){
            return new Result(false, StatusCode.ACCESSERROR,"权限不足");
        }
        friendService.deleteFriend(claims.getId(),friendid);
        return new Result(true,StatusCode.OK,"删除成功");
    }
}
