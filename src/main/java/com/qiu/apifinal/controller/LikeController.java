package com.qiu.apifinal.controller;

import cn.dev33.satoken.stp.StpUtil;

import com.qiu.apifinal.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/video/{videoId}")
    public ResponseEntity<Map<String, String>> likeVideo(@PathVariable String videoId) {
        var userId = StpUtil.getLoginId().toString();
        likeService.addLike(userId, videoId);
        Map<String, String> responseData = new HashMap<>();
        responseData.put("message", "点赞成功");
        // 返回一个包含JSON数据的ResponseEntity
        return ResponseEntity.ok(responseData);
    }

    @GetMapping("/count/video/{videoId}")
    public ResponseEntity<Map<String, String>> getLikeCount(@PathVariable String videoId) {
        var count = likeService.getLikeCount(videoId).toString();
        Map<String, String> responseData = new HashMap<>();
        responseData.put("count", count);
        // 返回一个包含JSON数据的ResponseEntity
        return ResponseEntity.ok(responseData);
    }

    @DeleteMapping("/video/{videoId}")
    ResponseEntity<Map<String, String>> cancelLikeVideo(@PathVariable String videoId) {
        var userId = StpUtil.getLoginId().toString();
        likeService.removeLike(userId, videoId);
        Map<String, String> responseData = new HashMap<>();
        responseData.put("message", "取消点赞成功");
        // 返回一个包含JSON数据的ResponseEntity
        return ResponseEntity.ok(responseData);
    }


    @GetMapping("/list/video/{videoId}")
    public ResponseEntity<Set<Map<String, String>>> getVideoLikes(@PathVariable String videoId) {
        var likes = likeService.getVideoLikes(videoId);
        return ResponseEntity.ok(likes);
    }


    @GetMapping("/my")
    public ResponseEntity<Set<Map<String, String>>> getUserLikes() {
        var userId = StpUtil.getLoginId().toString();
        var likes = likeService.getUserLikes(userId);
        return ResponseEntity.ok(likes);
    }


}
