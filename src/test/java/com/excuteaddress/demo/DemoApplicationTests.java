package com.excuteaddress.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DemoApplicationTests {

	@Test
	public void contextLoads() {
String searchStr = "用户来电投诉湛江市霞山区龙脊村金太阳幼儿园网络非常不好，打电话还有上网同样受到影响，经系统查询用户上网功能正常，用户表示周围人也有同样情况，而且在里面外面网络一样不好，并且这种情况出现很久了，用户要求我司可以安排人员处理一下这种情况，并给予一个合理答复，请跟进，谢谢！";


		Pattern abs = Pattern.compile("市.+(市|区|县)(.{1,5}镇)?(.{1,5}街道)?(.{1,5}片区)?(.{1,10}大道)?(.{1,10}村)?(.{1,10}社区)?(.{1,10}小区)?(.{1,10}街)?(.{1,10}路)?");
		Matcher matcher = abs.matcher(searchStr);
		while(matcher.find()){
			System.out.println(matcher.group());
		}

	}

}
