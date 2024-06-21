package com.qiu.apifinal.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface LikeMapper {
    @Select("SELECT likenum FROM videolikenum WHERE vid = #{vid}")
    Integer getLikeNumByVid(@Param("vid") int vid);

    @Select("SELECT COUNT(*) > 0 FROM `like` WHERE uid = #{uid} AND vid = #{vid}")
    Boolean isVideoLikedByUser(@Param("uid") int uid, @Param("vid") int vid);

    @Insert("INSERT INTO `like`(uid, vid) VALUES(#{uid}, #{vid})")
    void likeVideo(@Param("uid") int uid, @Param("vid") int vid);

    @Update("UPDATE videolikenum SET likenum = likenum + 1 WHERE vid = #{vid}")
    void incrementLikeNum(@Param("vid") int vid);

    @Delete("DELETE FROM `like` WHERE uid = #{uid} AND vid = #{vid}")
    void unlikeVideo(@Param("uid") int uid, @Param("vid") int vid);

    @Update("UPDATE videolikenum SET likenum = likenum - 1 WHERE vid = #{vid}")
    void decrementLikeNum(@Param("vid") int vid);
}
