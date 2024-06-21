package com.qiu.apifinal.service;

import com.qiu.apifinal.mapper.LikeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class LikeService {

    @Autowired
    LikeMapper likeMapper;

    public boolean isLiked(int uid, int vid){
        return likeMapper.isVideoLikedByUser(uid, vid);
    }

    public void addLike(int uid, int vid){
        likeMapper.likeVideo(uid, vid);
        likeMapper.incrementLikeNum(vid);
    }

    public void removeLike(int uid, int vid){
        likeMapper.unlikeVideo(uid, vid);
        likeMapper.decrementLikeNum(vid);
    }

    public Integer getLikes(int vid){
        return likeMapper.getLikeNumByVid(vid);
    }
}