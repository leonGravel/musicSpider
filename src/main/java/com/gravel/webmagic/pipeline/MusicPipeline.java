package com.gravel.webmagic.pipeline;

import com.gravel.Mapper.CommentMapper;
import com.gravel.Mapper.MusicMapper;
import com.gravel.entity.Comment;
import com.gravel.entity.Music;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * Created by gravel on 2017/6/28.
 */
@Component("MusicPipeline")
public class MusicPipeline implements Pipeline {

	@Autowired
    private CommentMapper commentMapper;

	@Autowired
    private MusicMapper musicMapper;

	@Override
	public void process(ResultItems resultItems, Task task) {

		for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
			if (entry.getKey().equals("music")) {
				Music music = (Music) entry.getValue();
//				System.out.println("mMusicDao--->null" + musicMapper == null);
				if (musicMapper.checkSongIsExit(music.getSongId()) == 0) {
                musicMapper.insert(music);
				}
			} else {
				List<Comment> commentList = (List<Comment> ) entry.getValue();
//				System.out.println("mCommentDao--->null" + commentMapper == null);

				for(Comment comment : commentList){
					if (commentMapper.checkCommentIsExit(comment.getCommentId()) == 0) {
                    commentMapper.insert(comment);
				}
				}
			}

		}
	}


}
