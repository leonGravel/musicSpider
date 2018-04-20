package com.gravel.Mapper;

import com.gravel.entity.Comment;
import org.apache.ibatis.annotations.Insert;

/**
 * Created by gravel on 2018/4/20.
 */
public interface CommentMapper {
    @Insert("insert into comment (comment_id, content, liked_count, nickname, song_id, time) values (?, ?, ?, ?, ?, ?)")
    void insert(Comment comment);
}
