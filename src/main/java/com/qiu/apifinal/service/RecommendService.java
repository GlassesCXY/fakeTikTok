package com.qiu.apifinal.service;


import com.qiu.apifinal.mapper.RecommendMapper;
import com.qiu.apifinal.thrift.TTSocket;
import com.qiu.apifinal.thrift.ThriftClient;
import com.qiu.apifinal.thrift.ThriftClientConnectPoolFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RecommendService {


    @Autowired
    private RecommendMapper recommendMapper;

    @Autowired
    ThriftClient thriftClient;

    @Autowired
    ThriftClientConnectPoolFactory thriftClientPool;

    public int getRecommend(int uid){

        int result = 0;
        TTSocket ttSocket = null;
        try {
            ttSocket = thriftClientPool.getConnect();
//            thriftClient.open();

            result = ttSocket.getService().getRecommend(uid);

            thriftClientPool.returnConnection(ttSocket);
        } catch (Exception e) {
            e.printStackTrace();
            if (ttSocket != null) {
                thriftClientPool.invalidateObject(ttSocket);
            }
            // 当远程调用失败时，使用本地mapper获取推荐结果
            result = recommendMapper.findMostLikedUnwatchedVideo(uid);
        } finally {
//            thriftClient.close();
        }


        return result;
    }
}
