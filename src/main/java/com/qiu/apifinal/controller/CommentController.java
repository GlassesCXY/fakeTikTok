package com.qiu.apifinal.controller;

import cn.dev33.satoken.stp.StpUtil;

import com.qiu.apifinal.entity.dto.CommentRequest;
import com.qiu.apifinal.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/add")
    public ResponseEntity<String> addComment(@RequestBody CommentRequest c) {
        var userId = StpUtil.getLoginId().toString();
        String commentId = commentService.addComment(c.getVideoId(), userId,c.getCommentContent());
        return ResponseEntity.ok(commentId);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<Map<String, String>> getCommentById(@PathVariable String commentId) {
        Map<String, String> comment = commentService.getCommentByID(commentId);
        return ResponseEntity.ok(comment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId,
                                              @RequestParam String videoId,
                                              @RequestParam String userId) {
        commentService.deleteComment(videoId, userId, commentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/video/{videoId}")
    public ResponseEntity<Set<Map<String, String>>> getCommentsByVideo(@PathVariable String videoId) {
        Set<Map<String, String>> comments = commentService.getCommentsByVideo(videoId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/my")
    public ResponseEntity<Set<Map<String, String>>> getCommentsByUser() {
        var userId = StpUtil.getLoginId().toString();
        Set<Map<String, String>> comments = commentService.getCommentsByUser(userId);
        return ResponseEntity.ok(comments);
    }
}
