package com.collector.rank;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.collector.dao.RankDao;
import com.collector.dto.rank.RankingListDto;
import com.collector.mainService.RankCollectServiceSelenium;
import com.collector.mainService.SearchLoofService;

@SpringBootTest
public class CrawlingConnectionTest {
	
	@Value("${COOKIE_INFO}") private String cookie_info;
	
	@Autowired
	private RankCollectServiceSelenium rankCollectServiceSelenium;
	
	@Mock
	private RankDao dao;

	@InjectMocks
	private SearchLoofService searchLoofService;
	
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
	        String postData = "t=1&pageno=0&s=0&c=0&search="+"연우야";
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
	
	@Test
	public void 셀레니움분리() {
		int t = 1;         // 전투력 타입?
        int pageno = 0;
        int s = 0;         // 예: 서버 코드 (ex. 데이안)
        int c = 0;// 예: 직업 코드 (ex. 전사)
        String search = "연우야";
        
        //RankingListDto dto = rankCollectServiceSelenium.userInfo(t, pageno, s, c, search);
        //System.out.println("검색 결과: " + dto.getCharName() + " / Power: " + dto.getPower());
	}
	
	@Test
	public void 서치루프_셀레니움() {
		
		searchLoofService.collectRank();
	}

}
