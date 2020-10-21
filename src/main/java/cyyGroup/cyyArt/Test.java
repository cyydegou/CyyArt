package cyyGroup.cyyArt;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import cyyGroup.cyyArt.until.HttpUntil;

public class Test {
	public static void main(String[] args) throws UnsupportedEncodingException {
		String url = "http://192.168.2.62:8080/NRMP/liveCourseInterface/queryQuestion?courseName=新概念青少入门级AB重难点双师辅导与答疑班&name=我的衣橱 和玩具&type=1";
		url = URLEncoder.encode(url, "UTF-8");
		String string = HttpUntil.get(url, null, null);
		System.out.println(string);
	}
}
