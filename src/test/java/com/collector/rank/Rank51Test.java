package com.collector.rank;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.jupiter.api.Test;


public class Rank51Test {
	
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
	        conn.setRequestProperty("Cookie", "PCID=17448949728827285030046; _ga=GA1.1.249914702.1744894973; _fbp=fb.1.1744894973830.10761588743978751; _ga=GA1.3.249914702.1744894973; NXLATC=0; mmenc=; mmcreators=; _cfuvid=AM97XTR_3qDRLwfCfw1WGRFQ3gb6FnsJUuRCifdSklM-1748431778001-0.0.1.1-604800000; _iflocale=ko-KR; _ifplatform=krpc; isCafe=false; _ifplatform_selector=krpc; A2SK=act:174844108924388738; gnbLogo=null; mmtoken=_NKJgb4FwGb_6taNneyxjn4M1C6v0gQL_db7_kU3tnseuGomCBpI24CqJ8AGgMpQWhiJ82DqC8iN_Ghy63WK3lfD3uhCrGX0t_5UVe7KkNA1:FfaRF8-YxnCD8SFZiehg1sT9Wwr7op9qVuw41JHpwOUbILSpGI6KZknoeOG0NvzdkP9lYx-oMBVsYbDsFLr_J5FNUuMcS5moxES5Wgn7Xf01");

	        // POST 파라미터 작성
	        String postData = "t=1&pageno=51&s=1&c=2&search=";
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
