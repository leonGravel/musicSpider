package com.gravel.repository;

import com.gravel.entity.Music;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 *  Created by gravel on 2018/4/20.
 */
public interface MusicRepository extends JpaRepository<Music, Integer> {
	int countBySongId(String songId);

}
