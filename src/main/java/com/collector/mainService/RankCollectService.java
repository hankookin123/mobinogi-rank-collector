package com.collector.mainService;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.collector.dto.rank.RankingListDto;

// 홈페이지에서 데이터 긁어옴.
@Service
public class RankCollectService {
	
	@Value("${COOKIE_INFO}") private String cookie_info;
	
	private static final Logger logger =
			LoggerFactory.getLogger(RankCollectService.class);

	public List<RankingListDto> rankCollect(
			int t, int pageno, int s, int c, String search, List<RankingListDto> rankingList
			) throws Exception {
		// 전투력랭킹, 페이지번호(20개 순위), 서버, 직업
		// url에 파라미터 넣고 post 형식으로 요청보냄. get요청 보내면 s,c(서버,직업) 값을 못넣음.
		try {
			// 쿠키정보 안보내면 오류남
			URL url = new URL("https://mabinogimobile.nexon.com/Ranking/List/rankdata");
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setDoOutput(true);
	        conn.setRequestProperty("User-Agent", "Mozilla/5.0");
	        conn.setRequestProperty("Origin", "https://mabinogimobile.nexon.com");
	        conn.setRequestProperty("Referer", "https://mabinogimobile.nexon.com/Ranking/List");
	        conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        conn.setRequestProperty("Cookie",cookie_info);
	        
	        String postData = "t=" + t + "&pageno=" + pageno + "&s=" + s + "&c=" + c + "&search=";
	        try (OutputStream os = conn.getOutputStream()) {
	            byte[] input = postData.getBytes("UTF-8");
	            os.write(input, 0, input.length);
	        }

	        
	        try (InputStream responseStream = conn.getInputStream()) {
	            Document doc = Jsoup.parse(responseStream, "UTF-8", "https://mabinogimobile.nexon.com/Ranking/List/rankdata");
	            // ... 이하 파싱 로직
	            Elements rankItems = doc.select("ul.list > li.item");
	        
				if (rankItems.isEmpty()) {
					System.out.println("랭킹 데이터가 없습니다. 파라미터를 다시 확인하세요.");
					logger.error("랭킹 데이터가 없습니다. 파라미터를 다시 확인하세요. t={}, pageno={}, s={}, c={}, search={}",
							t, pageno, s, c, search);
				}
				
				// dto에 데이터 추가
				for (Element item : rankItems) {
					String rankText = item.select("dl:has(dt:matches(\\d+위)) dt").text(); 
					int rank = Integer.parseInt(rankText.replaceAll("[^\\d]", "")); 
					String server = item.select("dl:has(dt:matches(서버명)) dd").text(); 
					String charName = item.select("dl:has(dt:matches(캐릭터명)) dd").attr("data-charactername"); 
					String clazz = item.select("dl:has(dt:matches(클래스)) dd").text(); 
					String power_str = item.select("dl:has(dt:matches(전투력)) dd").text();
					
					power_str = power_str.replace(",", "");
					int power_int = Integer.parseInt(power_str);
					
					// dto는 캐릭터 하나하나의 데이터, rankingList는 어레이리스트.
					RankingListDto dto = new RankingListDto(rank, s, charName, c, power_int);
					rankingList.add(dto);
	//			  System.out.printf("%s, %s,  %s, %s, %s\n", rank, server, charName, clazz,
	//			  power_int);
					
				}
	        }
			
		} catch (Exception e) {
			logger.error("데이터 수집 중 RankCollectService 오류남: {}", e.getMessage(), e );
		}
		
		return rankingList;
	}
}
