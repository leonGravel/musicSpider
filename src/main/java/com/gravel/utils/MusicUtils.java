package com.gravel.utils;


import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;

/**
 * Created by gravel on 2018/4/20.
 */
public class MusicUtils {
	/**
	 * 使用非加密API获取网易云的评论
	 * @param songId
	 * @param offset
	 * @return
	 */
	public static String crawlNormalAjaxUrl(String songId,String authHeader, int offset) {
		String jsonComment = "";
		try {
		 jsonComment = Jsoup.
				 connect("http://music.163.com/api/v1/resource/comments/R_SO_4_" + songId + "/?limit=100&offset="+offset)
//				.header("Referer", "http://music.163.com/")
//				.header("Host", "music.163.com")
                 .header("Proxy-Authorization", authHeader)
                 .userAgent(UserAgentUtil.getRandomUserAgent())
				.ignoreContentType(true)
				.get().select("body").text().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonComment;
	}



	public static String parseMillisecone(long millisecond) {
		String time = null;
		try {
			long yushu_day = millisecond % (1000 * 60 * 60 * 24);
			long yushu_hour = (millisecond % (1000 * 60 * 60 * 24))
					% (1000 * 60 * 60);
			long yushu_minute = millisecond % (1000 * 60 * 60 * 24)
					% (1000 * 60 * 60) % (1000 * 60);
			@SuppressWarnings("unused")
			long yushu_second = millisecond % (1000 * 60 * 60 * 24)
					% (1000 * 60 * 60) % (1000 * 60) % 1000;
			if (yushu_day == 0) {
				return (millisecond / (1000 * 60 * 60 * 24)) + "天";
			} else {
				if (yushu_hour == 0) {
					return (millisecond / (1000 * 60 * 60 * 24)) + "天"
							+ (yushu_day / (1000 * 60 * 60)) + "时";
				} else {
					if (yushu_minute == 0) {
						return (millisecond / (1000 * 60 * 60 * 24)) + "天"
								+ (yushu_day / (1000 * 60 * 60)) + "时"
								+ (yushu_hour / (1000 * 60)) + "分";
					} else {
						return (millisecond / (1000 * 60 * 60 * 24)) + "天"
								+ (yushu_day / (1000 * 60 * 60)) + "时"
								+ (yushu_hour / (1000 * 60)) + "分"
								+ (yushu_minute / 1000) + "秒";

					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	/*
	 * 将时间戳转换为时间
     */
	public static String stampToDate(long s) {
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt = s;
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}

	/**
	 * 将emoji表情替换成*
	 *
	 * @param source
	 * @return 过滤后的字符串
	 */
	public static String filterEmoji(String source) {
		if (StringUtils.isNotBlank(source)) {
			return source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
		} else {
			return source;
		}
	}
}
