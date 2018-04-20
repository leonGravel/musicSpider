package com.gravel.repository;

import com.gravel.entity.Comment;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *  Created by gravel on 2018/4/20.
 */

public interface CommentRepository extends JpaRepository<Comment, Integer> {
	int countByCommentId(int commentId);
}
