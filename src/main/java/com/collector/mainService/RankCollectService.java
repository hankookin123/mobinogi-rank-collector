package com.collector.mainService;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

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
			String url = "https://mabinogimobile.nexon.com/Ranking/List/rankdata";
		    
		    // RestClient 빌더 생성
		    RestClient restClient = RestClient.builder()
		        .baseUrl(url)
		        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
		        .defaultHeader("X-Requested-With", "XMLHttpRequest")
		        .defaultHeader("User-Agent", 
		            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
		        .defaultHeader("Cookie", cookie_info)
		        .build();

		    // 폼 데이터 구성
		    MultiValueMap<String, Object> formData = new LinkedMultiValueMap<>();
		    formData.add("t", String.valueOf(t));
		    formData.add("pageno", String.valueOf(pageno));
		    formData.add("s", String.valueOf(s));
		    formData.add("c", String.valueOf(c));
		    formData.add("search", search == null ? "" : search);

		    // 요청 실행
		    String responseBody = restClient.post()
		        .body(formData)
		        .retrieve()
		        .body(String.class);
			// 응답받는 html 코드 확인 System.out.println("응답 본문: " + response.getBody());
			Document doc = Jsoup.parse(responseBody);
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
			
		} catch (Exception e) {
			logger.error("데이터 수집 중 RankCollectService 오류남: {}", e.getMessage(), e );
		}
		
		return rankingList;
	}
}
