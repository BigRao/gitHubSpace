package com.inspur.bjzx.scenesecurityserve.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;


import net.sf.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;


import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendResult {
	private static final Logger logger = LoggerFactory.getLogger(SendResult.class);
	public static String sendPostRequest(JSONObject jsonObj, String url) throws IOException {
		CloseableHttpClient httpClient = null;
		try{
			httpClient = HttpClients.createDefault();
			HttpPost post = new HttpPost(url);
			post.setHeader("Content-type", "application/json; charset=utf-8");
			post.setHeader("Connection", "Close");
			StringEntity entity = new StringEntity(jsonObj.toString(), StandardCharsets.UTF_8);
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			post.setEntity(entity);
			// 执行请求操作，并拿到结果（同步阻塞）
			CloseableHttpResponse response = httpClient.execute(post);
			return EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			logger.error(Arrays.toString(e.getStackTrace()));
		}finally {
			assert httpClient != null;
			httpClient.close();
		}
		return null;
	}
	public static String sendPostRequest(String string, String url) throws IOException {
		CloseableHttpClient httpClient = null;
		try{
			httpClient = HttpClients.createDefault();
			HttpPost post = new HttpPost(url);
			post.setHeader("Content-type", "application/json; charset=utf-8");
			post.setHeader("Connection", "Close");
			StringEntity entity = new StringEntity(string, StandardCharsets.UTF_8);
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			post.setEntity(entity);
			// 执行请求操作，并拿到结果（同步阻塞）
			CloseableHttpResponse response = httpClient.execute(post);
			return EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			logger.error(Arrays.toString(e.getStackTrace()));
		}finally {
			assert httpClient != null;
			httpClient.close();
		}
		return null;
	}

}
