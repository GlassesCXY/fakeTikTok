package com.qiu.apifinal.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RecommendMapper {

    @Select("SELECT vln.vid " +
            "FROM videolikenum vln " +
            "LEFT JOIN history h ON vln.vid = h.vid AND h.uid = #{uid} " +
            "WHERE h.vid IS NULL " +
            "ORDER BY vln.likenum DESC " +
            "LIMIT 1")
    Integer findMostLikedUnwatchedVideo(@Param("uid") int uid);

}
