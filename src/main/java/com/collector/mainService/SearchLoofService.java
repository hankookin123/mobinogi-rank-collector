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
	private int errQuit = 0;
	
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
        //options.addArguments("--headless"); // 필요 시 제거해 디버깅 가능
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
	    int maxRetries = 3;
	    while (true) {
	        try {
	            if (webDriver != null) {
	                // 에러 횟수에 비례해 대기
	                Thread.sleep(errQuit * 10000L);
	                
	                webDriver.quit();
	                errQuit = 0;       // 정상 종료 시 초기화
	                Thread.sleep(7000L);  // 추가 대기
	            }
	            // 새 드라이버 할당
	            webDriver = getDriver();
	            return webDriver;  // 성공 시 종료
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	            System.out.println("Interrupted during restartDriver");
	            // 인터럽트 시 종료하거나 재시도 여부 검토 가능
	            return webDriver;
	        } catch (Exception e) {
	            errQuit++;
	            System.out.println("WebDriver 종료 중 오류 발생: " + e.getMessage() + " / 재시도 " + errQuit);
	            if (errQuit > maxRetries) {
	                System.out.println("최대 재시도 횟수 초과, 종료 시도 중단");
	                // 필요 시 여기서 예외 throw 하거나 기본 상태 유지, 종료 처리
	                break;
	            }
	            // 짧은 재시도 딜레이
	            try {
	                Thread.sleep(5000L);
	            } catch (InterruptedException ie) {
	                Thread.currentThread().interrupt();
	            }
	        }
	    }
	    return webDriver;
	}
	
	/*종료 시 인터럽트 발생을 조용히 처리

InterruptedException 예외 시 로깅 간소화 또는 제거하고, 재귀/반복 종료 시 더 이상 재시도하지 않도록 처리

restartDriver() 종료 로직 개선

종료 시 시도 횟수를 제한하고 인터럽트 발생 시 즉시 종료하도록 설계

Thread.sleep() 호출 횟수 최소화

스프링 애플리케이션 종료 시점에 WebDriver 적절히 종료하도록 별도 관리

스프링의 @PreDestroy 같은 어노테이션을 활용해 종료 시 WebDriver가 안정적으로 종료되도록 조치

위치와 시점에 따라 restartDriver() 호출 분리

종료와 재시작을 명확히 구분하고 종료 실패 시 재시도 제한*/
	
	
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
						if(total_search % 25 == 0 || retryCount >= 2) {
							webDriver = restartDriver();
						}
						Thread.sleep(3000L);
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
							retryCount++;
							continue;
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
						retryCount = maxRetries;
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
					Thread.sleep(12000L);
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
