package com.gravel.Mapper;

import com.gravel.entity.Music;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by leebr on 2018/4/20.
 */
@Mapper
public interface MusicMapper {
        @Insert("INSERT INTO 'music'.'music'( 'url', 'album', 'author', 'comment_count', 'song_id', 'title') VALUES (?,?,?,?,?,?)")
        void insert(Music music);
}
