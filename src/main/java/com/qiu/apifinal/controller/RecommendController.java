package com.qiu.apifinal.controller;


import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import com.qiu.apifinal.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RecommendController {


    @Autowired
    private RecommendService recommendService;

    @GetMapping("/getRecommend")
    public SaResult getRecommend(){
        int uid = Integer.parseInt(StpUtil.getLoginId().toString());
        int vid = recommendService.getRecommend(uid);
        return SaResult.data(vid);
    }

}
