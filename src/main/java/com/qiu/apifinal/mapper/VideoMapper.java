package com.qiu.apifinal.mapper;

import com.qiu.apifinal.entity.Video;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface VideoMapper {
    @Select("SELECT * FROM video")
    List<Video> findAll();

    @Select("SELECT * FROM video WHERE vid = #{vid}")
    Video findById(int vid);

    @Insert("INSERT INTO video(name, description, uid) VALUES(#{name}, #{description}, #{uid})")
    @Options(useGeneratedKeys = true, keyProperty = "vid", keyColumn = "vid")
    int insert(Video video);

    @Insert("INSERT INTO videolikenum(vid) VALUES(#{vid})")
    void createLikeRow(int vid);

    @Insert("INSERT INTO history(uid, vid) VALUES(#{uid}, #{vid})")
    void insertHistory(@Param("uid") int uid, @Param("vid") int vid);
}