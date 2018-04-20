package com.gravel.controller;

import com.gravel.webmagic.pipeline.MusicPipeline;
import com.gravel.webmagic.processor.MusicPageProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import us.codecraft.webmagic.Spider;

/**
 * Created by gravel on 2018/4/20.
 */
@RestController
public class IndexController {

    @Autowired
    MusicPageProcessor mProcessor;
    @Autowired
    MusicPipeline mPipeline;

    @GetMapping("/")
    public String index() {
        new Thread(() -> mProcessor.start(mProcessor, mPipeline)).start();

        return "爬虫开启";
    }


}
