package com.qiu.apifinal.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class FavoriteService {

    private final RedisTemplate<String, String> redisTemplate;

    public FavoriteService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String getUserFavoritesKey(String userId) {
        return "uid-" + userId+":favorites";
    }

    private String getVideoFavoritesKey(String videoId) {
        return "vid-" + videoId+":favorites";
    }


    public void addFavorite(String userId, String videoId) {
        String userFavoritesKey = getUserFavoritesKey(userId);
        String videoFavoritesKey = getVideoFavoritesKey(videoId);

        redisTemplate.opsForSet().add(userFavoritesKey, videoId);
        redisTemplate.opsForSet().add(videoFavoritesKey, userId);
    }


    public void removeFavorite(String userId, String videoId) {
        String userFavoritesKey = getUserFavoritesKey(userId);
        String videoFavoritesKey = getVideoFavoritesKey(videoId);

        redisTemplate.opsForSet().remove(userFavoritesKey, videoId);
        redisTemplate.opsForSet().remove(videoFavoritesKey, userId);
    }


    public Set<Map<String, String>> getUserFavorites(String userId) {
        String userFavoritesKey = getUserFavoritesKey(userId);
        var sets =  redisTemplate.opsForSet().members(userFavoritesKey);

        if (sets != null) {
            return addMorePrefix(sets,"vid");
        }
        return null;
    }


    public Set<Map<String, String>> getVideoFavorites(String videoId) {
        String videoFavoritesKey = getVideoFavoritesKey(videoId);
        var sets = redisTemplate.opsForSet().members(videoFavoritesKey);
        if (sets != null) {
            return addMorePrefix(sets,"uid");
        }
        return null;
    }

    public Long getFavoriteCount(String videoId) {
        String videoFavoritesKey = getVideoFavoritesKey(videoId);
        return redisTemplate.opsForSet().size(videoFavoritesKey);
    }


    public Set<Map<String,String>> addMorePrefix(Set<String> sets,String prefix){

        Set<Map<String,String>> res =new HashSet<>();
        for(var set:sets){
            Map<String,String> map = new HashMap<>();
            map.put(prefix,set);
            res.add(map);
        }
        return  res;

    }

}