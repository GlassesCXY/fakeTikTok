package com.qiu.apifinal.mapper;

import com.qiu.apifinal.entity.Video;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VideoMapper {
    @Select("SELECT * FROM video")
    List<Video> findAll();

    @Select("SELECT * FROM video WHERE vid = #{vid}")
    Video findById(int vid);

    @Insert("INSERT INTO video(name, description, uid) VALUES(#{name}, #{description}, #{uid})")
    @Options(useGeneratedKeys = true, keyProperty = "vid")
    void insert(Video video);
}