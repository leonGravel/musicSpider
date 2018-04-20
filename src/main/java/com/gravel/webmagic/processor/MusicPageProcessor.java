package com.gravel.webmagic.processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.gravel.entity.Comment;
import com.gravel.entity.Music;
import com.gravel.webmagic.downloader.MyProxyProvider;
import com.gravel.webmagic.pipeline.MusicPipeline;
import com.gravel.service.MusicService;
import com.gravel.utils.MusicUtils;

import com.gravel.utils.UserAgentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;

/**
 * Created by Ezio on 2017/6/27.
 */
@Component
public class MusicPageProcessor implements PageProcessor {
	// 正则表达式\\. \\转义java中的\ \.转义正则中的.
	// 主域名
	public static final String BASE_URL = "http://music.163.com/";
	// 匹配专辑URL
	public static final String ALBUM_URL = "http://music\\.163\\.com/playlist\\?id=\\d+";
	// 匹配歌曲URL
	public static final String MUSIC_URL = "http://music\\.163\\.com/song\\?id=\\d+";
	// 初始地址, 褐言喜欢的音乐id 148174530
	public static final String START_URL = "http://music.163.com/playlist?id=148174530";
	public static final int ONE_PAGE = 100;
	private int timestamp = (int) (new Date().getTime()/1000);
	private final String authHeader = authHeader("ZF201841982132k9HuP", "1b08e7d255d6412bb8b539e8196a9d2f", timestamp);
	private Site site = Site.me()
			.setDomain("http://music.163.com")
			.setSleepTime(1000)
			.setRetryTimes(30)
            .setDisableCookieManagement(false)
			.setCharset("utf-8")
			.setTimeOut(30000)
			.addHeader("Proxy-Authorization", authHeader)
			.setUserAgent(UserAgentUtil.getRandomUserAgent());

	@Override
	public Site getSite() {
		return site;
	}

	@Autowired
	MusicService mMusicService;

	@Override
	public void process(Page page) {
		// 根据URL判断页面类型
		if (page.getUrl().regex(ALBUM_URL).match()) {
			System.out.println("歌曲总数----->" + page.getHtml().xpath("//span[@id='playlist-track-count']/text()").toString());
			// 爬取歌曲URl加入队列
			page.addTargetRequests(page.getHtml().xpath("//div[@id=\"song-list-pre-cache\"]").links().regex(MUSIC_URL).all());
		} else {
			String url = page.getUrl().toString();
			Music music = new Music();
			// 单独对AJAX请求获取评论数, 使用JSON解析返回结果
			String songId = url.substring(url.indexOf("id=") + 3);
			int commentCount = getComment(page, songId, 0);
			// music 保存到数据库
			music.setSongId(songId);
			music.setCommentCount(commentCount);
			music.setTitle(page.getHtml().xpath("//em[@class='f-ff2']/text()").toString());
			music.setAuthor(page.getHtml().xpath("//p[@class='des s-fc4']/span/a/text()").toString());
			music.setAlbum(page.getHtml().xpath("//p[@class='des s-fc4']/a/text()").toString());
			music.setURL(url);
			//page.putField("music", music);
			mMusicService.addMusic(music);
		}
	}

	private int getComment(Page page, String songId, int offset) {
		int commentCount;
		String s = MusicUtils.crawlNormalAjaxUrl(songId,authHeader, offset);

		if (s.contains("503 Service Temporarily Unavailable")||s.contains("Cheating")) {
			commentCount = -1;
		} else {
			JSONObject jsonObject = JSON.parseObject(s);
			commentCount = (Integer) JSONPath.eval(jsonObject, "$.total");
			for (; offset < commentCount; offset = offset + ONE_PAGE) {
				JSONObject obj = JSON.parseObject(MusicUtils.crawlNormalAjaxUrl(songId,authHeader, offset));
				List<Integer> commentIds = (List<Integer>) JSONPath.eval(obj, "$.comments.commentId");
				List<String> contents = (List<String>) JSONPath.eval(obj, "$.comments.content");
				List<Integer> likedCounts = (List<Integer>) JSONPath.eval(obj, "$.comments.likedCount");
				List<String> nicknames = (List<String>) JSONPath.eval(obj, "$.comments.user.nickname");
				List<Long> times = (List<Long>) JSONPath.eval(obj, "$.comments.time");
				List<Comment> comments = new ArrayList<>();
				for (int i = 0; i < contents.size(); i++) {
					// 保存到数据库
					Comment comment = new Comment();
					comment.setCommentId(commentIds.get(i));
					comment.setSongId(songId);
					comment.setContent(MusicUtils.filterEmoji(contents.get(i)));
					comment.setLikedCount(likedCounts.get(i));
					comment.setNickName(nicknames.get(i));
					comment.setTime(MusicUtils.stampToDate(times.get(i)));
					comments.add(comment);
					mMusicService.addComment(comment);
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		return commentCount;
	}


	public void start(MusicPageProcessor processor, MusicPipeline musicPipeline) {

		long start = System.currentTimeMillis();
		final String ip = "forward.xdaili.cn";//这里以正式服务器ip地址为准
		final int port = 80;//这里以正式服务器端口地址为准

		//以下订单号，secret参数 须自行改动

		HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
		httpClientDownloader.setProxyProvider(MyProxyProvider.from(new Proxy(ip,port)));


		Spider.create(processor)
				.addUrl(START_URL)
				.setDownloader(httpClientDownloader)
				.thread(5)
//				.addPipeline(netEaseMusicPipeline)
				.run();
		long end = System.currentTimeMillis();
		System.out.println("爬虫结束,耗时--->" + MusicUtils.parseMillisecone(end - start));

	}


	/**
	 * http://www.xdaili.cn/usercenter/order
	 * 讯代理 买了10W 的
	 * @param orderno
	 * @param secret
	 * @param timestamp
	 * @return
	 */

	public static String authHeader(String orderno, String secret, int timestamp){
		//拼装签名字符串
		String planText = String.format("orderno=%s,secret=%s,timestamp=%d", orderno, secret, timestamp);

		//计算签名
		String sign = org.apache.commons.codec.digest.DigestUtils.md5Hex(planText).toUpperCase();

		//拼装请求头Proxy-Authorization的值
		String authHeader = String.format("sign=%s&orderno=%s&timestamp=%d", sign, orderno, timestamp);
		return authHeader;
	}

}
