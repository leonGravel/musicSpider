package com.gravel.Mapper;

import com.gravel.entity.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created by gravel on 2018/4/20.
 */
@Mapper
public interface CommentMapper {
    @Insert("insert into `comment`( `commentId`, `content`, `likedCount`, `nickname`, `songId`, `time`) values (#{commentId},#{content},#{likedCount},#{nickName},#{songId},#{time})")
    void insert(Comment comment);

    @Select("select count(1) from comment where commentId = #{commentId}")
    Integer checkCommentIsExit(int commentId);
}
