package com.qiu.apifinal.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class LikeService {

    private final RedisTemplate<String, String> redisTemplate;

    public LikeService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String getUserLikesKey(String userId) {
        return "uid-" + userId + ":likes";
    }

    private String getVideoLikesKey(String videoId) {
        return "vid-" + videoId + ":likes";
    }

    public void addLike(String userId, String videoId) {
        String userLikesKey = getUserLikesKey(userId);
        String videoLikesKey = getVideoLikesKey(videoId);

        redisTemplate.opsForSet().add(userLikesKey, videoId);
        redisTemplate.opsForSet().add(videoLikesKey, userId);
    }

    public void removeLike(String userId, String videoId) {
        String userLikesKey = getUserLikesKey(userId);
        String videoLikesKey = getVideoLikesKey(videoId);
        redisTemplate.opsForSet().remove(userLikesKey, videoId);
        redisTemplate.opsForSet().remove(videoLikesKey, userId);
    }

    public Set<Map<String, String>> getUserLikes(String userId) {
        String userLikesKey = getUserLikesKey(userId);
        Set<String> sets = redisTemplate.opsForSet().members(userLikesKey);

        if (sets != null) {
            return addMorePrefix(sets, "vid");
        }
        return null;
    }

    public Set<Map<String, String>> getVideoLikes(String videoId) {
        String videoLikesKey = getVideoLikesKey(videoId);
        Set<String> sets = redisTemplate.opsForSet().members(videoLikesKey);

        if (sets != null) {
            return addMorePrefix(sets, "uid");
        }
        return null;
    }

    public Long getLikeCount(String videoId) {
        String videoLikesKey = getVideoLikesKey(videoId);
        return redisTemplate.opsForSet().size(videoLikesKey);
    }

    private Set<Map<String, String>> addMorePrefix(Set<String> sets, String prefix) {
        Set<Map<String, String>> res = new HashSet<>();
        for (String set : sets) {
            Map<String, String> map = new HashMap<>();
            map.put(prefix, set);
            res.add(map);
        }
        return res;
    }
}