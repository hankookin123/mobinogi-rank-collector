package com.collector.mainService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.collector.dto.rank.RankingListDto;
import com.collector.etc.ClassCode;
import com.collector.etc.SeleniumDriverManager;

@Service
@ConditionalOnProperty(name = "DEVICE_ID", havingValue = "web_craw_pc")
public class RankCollectServiceSelenium {
	// 싱글톤으로 작동. 요청온 순서대로 크롤링.
    private static final Logger logger = LoggerFactory.getLogger(RankCollectServiceSelenium.class);
    private final SeleniumDriverManager seleniumDriverManager;
    private final Object webDiverLock = new Object();
    
    public RankCollectServiceSelenium(SeleniumDriverManager seleniumDriverManager) {
    	this.seleniumDriverManager = seleniumDriverManager;
    }
    
    public RankingListDto userInfo(int t, int pageno, int s, int c, String search) {
    	synchronized (webDiverLock) {
    		WebDriver driver = seleniumDriverManager.getWebDriver();
        	JavascriptExecutor jsExec = (JavascriptExecutor) driver;
            String script =
                "return fetch('https://mabinogimobile.nexon.com/Ranking/List/rankdata', {"
                + "method: 'POST',"
                + "headers: {"
                + "'Content-Type': 'application/x-www-form-urlencoded',"
                + "'X-Requested-With': 'XMLHttpRequest',"
                + "'Origin': 'https://mabinogimobile.nexon.com',"
                + "'Referer': 'https://mabinogimobile.nexon.com/Ranking/List'"
                + "},"
                + "body: 't=" + t + "&pageno=" + pageno + "&s=" + s + "&c=" + c + "&search="+search+"'"
                + "}).then(response => response.text());";

            String responseHtml = (String) jsExec.executeScript(script);

            Document doc = Jsoup.parse(responseHtml);
        	
            Elements rankItems = doc.select("ul.list > li.item");
            if (rankItems.isEmpty()) {
                logger.error("랭킹 데이터가 없습니다. 파라미터를 다시 확인하세요. t={}, pageno={}, s={}, c={}, search={}",
                        t, pageno, s, c, search);
                System.out.println("랭킹 데이터가 없습니다.");
            }
            RankingListDto dto = null;
            for (Element item : rankItems) {
            	String charName = item.select("dl:has(dt:matches(캐릭터명)) dd").attr("data-charactername");
            	System.out.println(charName);
            	if(search.equals(charName)) {
            		String rankText = item.select("dl:has(dt:matches(\\d+위)) dt").text();
            		int rankInt = Integer.parseInt(rankText.replaceAll("[^\\d]", ""));
            		
            		String server = item.select("dl:has(dt:matches(서버명)) dd").text();
            		int serverInt = ClassCode.서버_코드맵.get(server);
            		
            		String clazz = item.select("dl:has(dt:matches(클래스)) dd").text();
            		int clazzInt = ClassCode.직업_코드맵.get(clazz);
            		
            		String powerStr = item.select("dl:has(dt:matches(전투력)) dd").text().replace(",", "");
            		int powerInt = Integer.parseInt(powerStr);
            		
            		dto = new RankingListDto(rankInt, serverInt, charName, clazzInt, powerInt);
            		
            		return dto;
            	}
            }
            return dto;
		}
    }
    
}
