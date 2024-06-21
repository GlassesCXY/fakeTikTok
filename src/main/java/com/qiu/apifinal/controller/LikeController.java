package com.qiu.apifinal.controller;

import cn.dev33.satoken.stp.StpUtil;

import cn.dev33.satoken.util.SaResult;
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
    public SaResult likeVideo(@PathVariable Integer videoId) {
        Integer userId = Integer.valueOf(StpUtil.getLoginId().toString());
        likeService.addLike(userId, videoId);
        Map<String, String> responseData = new HashMap<>();
        responseData.put("message", "点赞成功");
        // 返回一个包含JSON数据的ResponseEntity
        return SaResult.ok("点赞成功");
    }

    @GetMapping("/count/video/{videoId}")
    public SaResult getLikeCount(@PathVariable Integer videoId) {
        var count = likeService.getLikes(videoId).toString();
        Map<String, String> responseData = new HashMap<>();
        responseData.put("count", count);
        // 返回一个包含JSON数据的ResponseEntity
        return SaResult.data(responseData);
    }

    @DeleteMapping("/video/{videoId}")
    public SaResult cancelLikeVideo(@PathVariable Integer videoId) {
        int userId = Integer.parseInt(StpUtil.getLoginId().toString());
        likeService.removeLike(userId, videoId);
        Map<String, String> responseData = new HashMap<>();
        responseData.put("message", "取消点赞成功");
        // 返回一个包含JSON数据的ResponseEntity
        return SaResult.ok("取消成功");
    }

    @GetMapping("/isLiked/{videoId}")
    public SaResult getVideoLikedStatus(@PathVariable Integer videoId){
        int userId = Integer.parseInt(StpUtil.getLoginId().toString());
        Boolean res = likeService.isLiked(userId, videoId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("status",res );

        return SaResult.data(response);
    }

//    @GetMapping("/my")
//    public ResponseEntity<Set<Map<String, String>>> getUserLikes() {
//        var userId = StpUtil.getLoginId().toString();
//        var likes = likeService.getUserLikes(userId);
//        return ResponseEntity.ok(likes);
//    }


}
