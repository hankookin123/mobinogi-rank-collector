package com.collector.mainService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import com.collector.dao.RankDao;
import com.collector.dto.rank.RankingListDto;
import com.collector.etc.ClassCode;

// DB에 등록하는곳
@Service
public class SearchLoofService {
	
	private RankCollectService rankCollectService;
	private RankDao dao;
	private WebDriver webDriver = null;
	
	
	// 이렇게 객체를 new로 안만들고 파라미터로 받아오는 것을 의존성 주입이라하고,
	// 테스트 용이성,결합도 감소 같은 설명들은 rankdao랑 연결시킨 것이 아니라 '받아서 사용한다'라고 코드를 짜서다.
	public SearchLoofService(RankDao dao, RankCollectService rankCollectService) {
		this.dao = dao;
		this.rankCollectService = rankCollectService;
	}
	
	private int pageCount = 50;
	
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
	public WebDriver getDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 필요 시 제거해 디버깅 가능
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("user-agent=Mozilla/5.0"); 
        webDriver = new ChromeDriver(options);
        webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        webDriver.get("https://mabinogimobile.nexon.com/Ranking/List");
		return webDriver;
	}
	
	public WebDriver restartDriver() {
		webDriver.quit();
		getDriver();
		return webDriver;
	}
	
	public void collectRank() {
		
		LocalDateTime now1 = LocalDateTime.now();
		System.out.println("데이터수집 시작 : " + now1);
		
		// 데이안서버(서버번호: 1)의 데이터만 수집.
		int total_search = 0; //총 검색 횟수
		webDriver = getDriver();						
		
		for (Integer code : ClassCode.직업_코드맵.values()) {
			List<RankingListDto> rankingList = new ArrayList<>();
			for (int i = 1; i <= pageCount; i++) {
				int maxRetries = 3;
			    int retryCount = 0;
				while (retryCount < maxRetries) {
					total_search++;
					try {
						if(total_search % 25 == 0 & retryCount >= 2) {
							webDriver = restartDriver();
							Thread.sleep(7000);
						}
						Thread.sleep(3000);
						JavascriptExecutor jsExec = (JavascriptExecutor) webDriver;
						String script =
								"return fetch('https://mabinogimobile.nexon.com/Ranking/List/rankdata', {"
										+ "method: 'POST',"
										+ "headers: {"
										+ "'Content-Type': 'application/x-www-form-urlencoded',"
										+ "'X-Requested-With': 'XMLHttpRequest',"
										+ "'Origin': 'https://mabinogimobile.nexon.com',"
										+ "'Referer': 'https://mabinogimobile.nexon.com/Ranking/List'"
										+ "},"
										+ "body: 't=" + 1 + "&pageno=" + i + "&s=" + 1 + "&c=" + code + "&search='"
										+ "}).then(response => response.text());";
						// (1, i, 1, code, null, rankingList)
						String responseHtml = (String) jsExec.executeScript(script);
						
						Document doc = Jsoup.parse(responseHtml);
						
						Elements rankItems = doc.select("ul.list > li.item");
						if (rankItems.isEmpty()) {
							System.out.println("랭킹 데이터가 없습니다.");
						}
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
							RankingListDto dto = new RankingListDto(rank, 1, charName, code, power_int);
							rankingList.add(dto);
							System.out.printf("%s, %s,  %s, %s, %s\n", rank, server, charName, clazz,
									power_int);
							
						}
						
						System.out.println("총 횟수 : " + total_search + "  /  " 
								+ ClassCode.코드_직업맵.get(code) + " 페이지 : " + i);
						retryCount =5;
					} catch (InterruptedException  e) {
						// 인터럽 복구 해줘야 한다?
						Thread.currentThread().interrupt();
					} catch (Exception e) {
						retryCount++;
						e.printStackTrace();
					}
				}
				
			}
			
			dao.deianInsert(rankingList);
		}		
		
		LocalDateTime now2 = LocalDateTime.now();
		System.out.println("--- 데이터수집 시작 : " + now1);
		System.out.println("--- 데이터수집 종료 : " + now2);
		System.out.println("--- 검색한 페이지 수 : " + total_search);
		
	}
	
	public void dbUpdate() throws Exception {
		
		Random random =new Random();
		StringBuilder err_msg = new StringBuilder(); // 에러 구간 위치
		
		LocalDateTime now1 = LocalDateTime.now();
		System.out.println("데이터수집 시작 : " + now1);
		
		// 데이안서버(서버번호: 1)의 데이터만 수집.
		int total_search = 0; //총 검색 횟수
		int err_count = 0;  // 총 에러 횟수
		
		for (Integer code : ClassCode.직업_코드맵.values()) {
			List<RankingListDto> rankingList = new ArrayList<>();
			for (int i = 1; i <= pageCount; i++) {
				try {
					rankCollectService.rankCollect(1, i, 1, code, null, rankingList);
					total_search++;
					Thread.sleep(12000);
					System.out.println("총 횟수 : " + total_search + "  /  " 
							+ ClassCode.코드_직업맵.get(code) + " 페이지 : " + i);
				} catch (InterruptedException  e) {
					// 인터럽 복구 해줘야 한다?
					Thread.currentThread().interrupt();
				} catch (Exception e) {
					err_count ++;
					err_msg.append("/ ").append(ClassCode.코드_직업맵.get(code))
					.append(" ").append(i).append("~").append(i+20).append("등 ");
					e.printStackTrace();
				}
			}
			
			dao.deianInsert(rankingList);
		}		
		
		LocalDateTime now2 = LocalDateTime.now();
		System.out.println("--- 데이터수집 시작 : " + now1);
		System.out.println("--- 데이터수집 종료 : " + now2);
		System.out.println("--- 검색한 페이지 수 : " + total_search);
		System.out.println("--- 검색 오류 횟수 : " + err_count);
		System.out.println("--- 검색 오류 등수 : " + err_msg);
	}
	
}
