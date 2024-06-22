package com.qiu.apifinal.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.qiu.apifinal.entity.Video;
import com.qiu.apifinal.service.VideoService;
import com.qiu.apifinal.utils.MinioUtils;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class VideoController {
    private static final String FILE_NAME_PATTERN = "{}_{}";
    @Resource
    MinioUtils minioUtils;
    @Resource
    VideoService videoService;


    @PostMapping("/videos/video/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("des") String description
    ) {
        String filename = file.getOriginalFilename();
        Integer uid = Integer.parseInt(StpUtil.getLoginId().toString());
        Video video = new Video(0, filename, description, uid);
        videoService.insert(video);
        videoService.createLikeRow(video.getVid());
        minioUtils.uploadFile("video", file, String.valueOf(video.getVid()), "video/mp4");
        return ResponseEntity.ok("ok");
    }



    @PostMapping("/videos/video/getUrl")
    public SaResult getUrl(@RequestParam("vid") Integer vid) {
        Video video = videoService.findById(vid);
        return SaResult.data(minioUtils.getObjectUrl("video", String.valueOf(video.getVid())));
    }

    @PostMapping("/videos/video/view")
    public SaResult viewVideo(@RequestParam("vid") Integer vid){
        Integer uid = Integer.parseInt(StpUtil.getLoginId().toString());
        videoService.viewed(vid, uid);
        return SaResult.ok(vid+" viewed by"+uid);
    }


    @GetMapping("/user/videos")
    public SaResult getVideosByUser(@RequestParam int offset,
                                                       @RequestParam int limit) {
        Integer uid = Integer.parseInt(StpUtil.getLoginId().toString());
        List<Video> videos = videoService.getVideosByUser(uid, offset, limit);
        return SaResult.data(videos);
    }

    @DeleteMapping("/videos/video/{vid}")
    public SaResult deleteVideo(@PathVariable int vid) {
        Integer uid = Integer.parseInt(StpUtil.getLoginId().toString());
        videoService.deleteVideo(uid, vid);
        return SaResult.ok("删除成功");
    }

    @GetMapping("/videos/count")
    public SaResult getMyVideoCount(){
        Integer uid = Integer.parseInt(StpUtil.getLoginId().toString());
        Integer count = videoService.getUserVideoCount(uid);
        Map<String, Integer> map = new HashMap<>();
        map.put("videoCount", count);

        return SaResult.data(map);
    }


}