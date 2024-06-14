package com.qiu.apifinal;

import com.qiu.apifinal.utils.MinioUtils;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApiFinalApplicationTests {
    @Resource
    MinioUtils minioUtils;

    @Test
    void contextLoads() {
        System.out.println(minioUtils.getObjectUrl("video","MAD　ライブ音響 「残酷な天使のテーゼ」アスカ・ラングレー　４K高画質.mp4"));
    }

}
