package com.qiu.apifinal.controller;


import com.qiu.apifinal.entity.Video;
import com.qiu.apifinal.service.VideoService;
import com.qiu.apifinal.utils.MinioUtils;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class VideoController {
    private static final String FILE_NAME_PATTERN = "{}_{}";
    @Resource
    MinioUtils minioUtils;
    @Resource
    VideoService videoService;


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("uid") Integer uid,
            @RequestParam("des") String description
    ) {
        String filename = file.getOriginalFilename();
        videoService.insert(new Video(0, filename, description, uid));
        minioUtils.uploadFile("video", file, filename, "video/mp4");
        return ResponseEntity.ok("ok");
    }


    @PostMapping("/getUrl")
    public ResponseEntity<String> getUrl(@RequestParam("vid") Integer vid) {
        Video video = videoService.findById(vid);
        return ResponseEntity.ok(minioUtils.getObjectUrl("video", video.getName()));
    }


}