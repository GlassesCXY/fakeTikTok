package com.qiu.apifinal.service;

import com.qiu.apifinal.entity.Video;
import com.qiu.apifinal.mapper.VideoMapper;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoService {

    private final VideoMapper videoMapper;

    @Autowired
    public VideoService(VideoMapper videoMapper) {
        this.videoMapper = videoMapper;
    }

    public List<Video> findAll() {
        return videoMapper.findAll();
    }

    public Video findById(int vid) {
        return videoMapper.findById(vid);
    }

    public int insert(Video video) {
        return videoMapper.insert(video);
    }

    public void createLikeRow(int vid){
        videoMapper.createLikeRow(vid);
    }

    public void viewed(int vid, int uid){
        videoMapper.insertHistory(uid, vid);
    }

    public List<Video> getVideosByUser(int uid, int offset, int limit) {
        RowBounds rowBounds = new RowBounds(offset, limit);
        return videoMapper.findVideosByUser(uid, rowBounds);
    }

    public void deleteVideo(int uid, int vid) {
        videoMapper.deleteVideoById(vid, uid);
    }

    public Integer getUserVideoCount(int uid) {
        return videoMapper.getUserVideoCount(uid);
    }
}