package com.qiu.apifinal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FollowService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private String getFollowsKey(String userId) {
        return "uid:" + userId + ":follows";
    }

    private String getFansKey(String videoId) {
        return "uid:" + videoId + ":fans";
    }


    public void follow(String userId, String targetUserId) {
        var followKey = getFollowsKey(userId);
        var fansKey = getFansKey(targetUserId);

        //检查是否已经关注
        if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(followKey, targetUserId))) {
            unfollowUser(userId, targetUserId);
            return;
        }

        redisTemplate.opsForSet().add(followKey, targetUserId);
        redisTemplate.opsForSet().add(fansKey , userId);

    }

    public void unfollowUser(String userId, String targetUserId) {

        redisTemplate.opsForSet().remove(getFollowsKey(userId), targetUserId);
        redisTemplate.opsForSet().remove(getFansKey(targetUserId), userId);
    }

    public Set<String> getFollowing(String userId) {
        var followKey = getFollowsKey(userId);
        return redisTemplate.opsForSet().members(followKey);
    }

    public Set<String> getFans(String userId) {
        var fansKey = getFansKey(userId);
        return redisTemplate.opsForSet().members(fansKey);
    }


}
