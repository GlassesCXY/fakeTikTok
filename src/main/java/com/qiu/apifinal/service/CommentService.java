package com.qiu.apifinal.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class CommentService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    private HashOperations<String, String, String> hashOps;
    private SetOperations<String, String> setOps;
    @Resource
    private StringRedisTemplate generator;


    @PostConstruct
    private void init() {
        hashOps = redisTemplate.opsForHash();
        setOps = redisTemplate.opsForSet();
    }

    public Long generateUniqueId(String key) {
        ValueOperations<String, String> ops = this.generator.opsForValue();
        return ops.increment(key);
    }


    public String getVideoKey(String vid) {

        return "vid-" + vid + ":comments";
    }

    public String getUserKey(String uid) {

        return "uid-" + uid + ":comments";
    }


    public String addComment(String videoId, String userId, String content) {
        // 生成唯一的评论ID

        String commentId = generateUniqueId("UUID").toString();

        // 存储评论详情
        String commentKey = "comment:" + commentId;
        hashOps.put(commentKey, "userId", userId);
        hashOps.put(commentKey, "content", content);
        hashOps.put(commentKey, "timestamp", String.valueOf(System.currentTimeMillis()));
        // 更新视频评论ID集合
        setOps.add("vid-" + videoId + ":comments", commentId);
        // 更新用户评论ID集合
        setOps.add("uid-" + userId + ":comments", commentId);

        return commentId;
    }

    public Map<String, String> getCommentByID(String commentId) {
        String commentKey = "comment:" + commentId;
        return hashOps.entries(commentKey);
    }

    public void deleteComment(String videoId, String userId, String commentId) {
        // 删除评论详情
        redisTemplate.delete("comment:" + commentId);

        // 从视频评论ID集合中移除
        setOps.remove(getVideoKey(videoId), commentId);
        // 从用户评论ID集合中移除
        setOps.remove(getUserKey(userId), commentId);
    }

    public Set<Map<String, String>> getCommentsByVideo(String videoId) {
        Set<String> commentIds = setOps.members(getVideoKey(videoId));
        return getCommentsByIDs(commentIds);
    }


    public Set<Map<String, String>> getCommentsByUser(String userId) {
        Set<String> commentIds = setOps.members(getUserKey(userId));
        return getCommentsByIDs(commentIds);
    }

    private Set<Map<String, String>> getCommentsByIDs(Set<String> commentIds) {
        Set<Map<String, String>> comments = new HashSet<>();
        if (commentIds != null) {
            for (String commentId : commentIds) {
                Map<String, String> comment = getCommentByID(commentId);
                comment.put("commentId", commentId);
                comments.add(comment);
            }
        }
        return comments;
    }
}

