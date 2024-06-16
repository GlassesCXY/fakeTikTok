package com.qiu.apifinal.controller;

import cn.dev33.satoken.stp.StpUtil;

import com.qiu.apifinal.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/video/{videoId}")
    public ResponseEntity<Map<String, String>> addFavorite(@PathVariable String videoId) {
        var userId = StpUtil.getLoginId().toString();
        favoriteService.addFavorite(userId, videoId);

        Map<String, String> responseData = new HashMap<>();
        responseData.put("message", "收藏成功");
        // 返回一个包含JSON数据的ResponseEntity
        return ResponseEntity.ok(responseData);

    }

    @DeleteMapping("/video/{videoId}")
    public ResponseEntity<Map<String, String>> removeFavorite(@PathVariable String videoId) {
        var userId = StpUtil.getLoginId().toString();
        favoriteService.removeFavorite(userId, videoId);
        Map<String, String> responseData = new HashMap<>();
        responseData.put("message", "取消收藏成功");
        // 返回一个包含JSON数据的ResponseEntity
        return ResponseEntity.ok(responseData);

    }

    @GetMapping("/list/video/{videoId}")
    public ResponseEntity<Set<Map<String, String>>> getVideoFavorites(@PathVariable String videoId) {
        var  favorites = favoriteService.getVideoFavorites(videoId);
        return ResponseEntity.ok(favorites);
    }


    @GetMapping("/my")
    public ResponseEntity<Set<Map<String, String>>> getUserFavorites() {
        var userId = StpUtil.getLoginId().toString();
        var  favorites = favoriteService.getUserFavorites(userId);
        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/count/video/{videoId}")
    public ResponseEntity<Map<String, String>> getFavoriteCount(@PathVariable String videoId) {
        Map<String, String> responseData = new HashMap<>();
        String count = favoriteService.getFavoriteCount(videoId).toString();
        responseData.put("count", count);
        // 返回一个包含JSON数据的ResponseEntity
        return ResponseEntity.ok(responseData);

    }


}