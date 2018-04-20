package com.gravel;

import com.gravel.webmagic.pipeline.MusicPipeline;

import com.gravel.webmagic.processor.MusicPageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class MusicApplication {

	@Autowired
    MusicPageProcessor mProcessor;
	@Autowired
    MusicPipeline mPipeline;

	@GetMapping("/")
	public String index() {
		new Thread(() -> mProcessor.start(mProcessor, mPipeline)).start();

		return "爬虫开启";
	}

	public static void main(String[] args) {
		SpringApplication.run(MusicApplication.class, args);

	}
}
