package com.qiu.apifinal.service;


import com.qiu.apifinal.mapper.RecommendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RecommendService {


    @Autowired
    private RecommendMapper recommendMapper;



    public int getRecommend(int uid){

        return recommendMapper.findMostLikedUnwatchedVideo(uid);
    }
}
