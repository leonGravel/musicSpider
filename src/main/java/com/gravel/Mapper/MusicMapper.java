package com.gravel.Mapper;

import com.gravel.entity.Music;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created by gravel on 2018/4/20.
 */
@Mapper
public interface MusicMapper {
        @Insert("INSERT INTO `music`( `songUrl`, `album`, `author`, `commentCount`, `songId`, `title`) VALUES (#{songUrl},#{album},#{author},#{commentCount},#{songId},#{title})")
        void insert(Music music);

        @Select("select count(1) from music where songId = #{songId}")
        Integer checkSongIsExit(String songId);
}
