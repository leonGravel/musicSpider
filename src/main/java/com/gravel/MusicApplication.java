package com.gravel;

import com.gravel.webmagic.pipeline.MusicPipeline;

import com.gravel.webmagic.processor.MusicPageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;

@RestController
@SpringBootApplication
public class MusicApplication {


	public static void main(String[] args) {
		SpringApplication.run(MusicApplication.class, args);

	}
}
