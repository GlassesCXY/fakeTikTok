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

@RestController
public class VideoController {
    private static final String FILE_NAME_PATTERN = "{}_{}";
    @Resource
    MinioUtils minioUtils;
    @Resource
    VideoService videoService;


    @PostMapping("/upload")
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



    @PostMapping("/getUrl")
    public SaResult getUrl(@RequestParam("vid") Integer vid) {
        Video video = videoService.findById(vid);
        return SaResult.data(minioUtils.getObjectUrl("video", String.valueOf(video.getVid())));
    }

    @PostMapping("/view")
    public SaResult viewVideo(@RequestParam("vid") Integer vid){
        Integer uid = Integer.parseInt(StpUtil.getLoginId().toString());
        videoService.viewed(vid, uid);
        return SaResult.ok(vid+" viewed by"+uid);
    }


}