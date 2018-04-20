package com.gravel.webmagic.pipeline;

import com.gravel.Mapper.CommentMapper;
import com.gravel.Mapper.MusicMapper;
import com.gravel.entity.Comment;
import com.gravel.entity.Music;
import com.gravel.repository.CommentRepository;
import com.gravel.repository.MusicRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * Created by Ezio on 2017/6/28.
 */
@Component
public class MusicPipeline implements Pipeline {

	@Autowired
	CommentMapper CommentMapper;

	@Autowired
	MusicMapper MusicMapper;

	@Override
	public void process(ResultItems resultItems, Task task) {

		for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
			if (entry.getKey().equals("music")) {
				Music music = (Music) entry.getValue();
				System.out.println("mMusicDao--->null" + MusicMapper == null);
//				if (MusicMapper.countBySongId(music.getSongId()) == 0) {
					MusicMapper.insert(music);
//				}
			} else {
				Comment comment = (Comment) entry.getValue();
				System.out.println("mCommentDao--->null" + CommentMapper == null);
//				if (mCommentDao.countByCommentId(comment.getCommentId()) == 0) {
				CommentMapper.insert(comment);
//				}
			}

		}
	}


}
