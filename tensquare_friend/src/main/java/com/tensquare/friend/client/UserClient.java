package com.tensquare.friend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("tensquare-user")
public interface UserClient {
    //更新粉丝数
    @PostMapping("/user/incfans/{userid}/{x}")
    public void incFanscount(@PathVariable String userid, @PathVariable int x);
    //更新关注数
    @PostMapping("/user/incfollow/{userid}/{x}")
    public void incFollowcount(@PathVariable String userid,@PathVariable int x);
}
