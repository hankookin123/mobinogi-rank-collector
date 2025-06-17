package com.collector.rank;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CrawlingConnectionTest {
	
	@Value("${COOKIE_INFO}") private String cookie_info;
	
	@Test
	public void fetchRankPage51() {
	    try {
	        URL url = new URL("https://mabinogimobile.nexon.com/Ranking/List/rankdata");
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setDoOutput(true);
	        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
	        conn.setRequestProperty("Origin", "https://mabinogimobile.nexon.com");
	        conn.setRequestProperty("Referer", "https://mabinogimobile.nexon.com/Ranking/List?t=1");
	        conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        conn.setRequestProperty("Cookie", cookie_info);
	        // POST 파라미터 작성
	        String postData = "t=1&pageno=50&s=1&c=2&search=";
	        try (OutputStream os = conn.getOutputStream()) {
	            byte[] input = postData.getBytes("UTF-8");
	            os.write(input, 0, input.length);
	        }

	        int code = conn.getResponseCode();
	        System.out.println("응답 코드: " + code);

	        // 응답 내용 출력
	        try (BufferedReader br = new BufferedReader(
	                new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
	            String line;
	            while ((line = br.readLine()) != null) {
	                System.out.println(line);
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


}
