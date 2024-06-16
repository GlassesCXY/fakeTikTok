package com.qiu.apifinal.controller;

import cn.dev33.satoken.stp.StpUtil;

import com.qiu.apifinal.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/customer")
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/follow")
    public void followUser( @RequestParam String followsUserId) {
        var userId = StpUtil.getLoginId().toString();
        followService.follow(userId, followsUserId);
    }



    @GetMapping("/following")
    public Set<String> getFollowing() {
        var userId = StpUtil.getLoginId().toString();
        return followService.getFollowing(userId);
    }

    @GetMapping("/fans")
    public Set<String> getFollowers() {
        var userId = StpUtil.getLoginId().toString();
        return followService.getFans(userId);
    }
}