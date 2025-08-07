package com.collector.etc;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class SeleniumDriverManager {
	// 27회 검색하면 재시작됨. 유저 직접 검색 전용 셀레니움
	private WebDriver webDriver;
    private final AtomicInteger requestCount = new AtomicInteger(0);
    private final int MAX_REQUESTS = 27; // 27번 요청마다 재시작
	 
    @PostConstruct
    public void init() {
        launchBrowser();
    }

    public synchronized WebDriver getWebDriver() {
        // 27회 초과 시 재시작
        if (requestCount.incrementAndGet() > MAX_REQUESTS) {
            restartBrowser();
        }else if (webDriver == null) {
        	launchBrowser();
        }
        return webDriver;
    }

    public synchronized void launchBrowser() {
        if (webDriver != null) {
            webDriver.quit();
        }
        // 필요에 따라 ChromeDriver 위치 지정
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless"); // 필요 시 제거해 디버깅 가능
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("user-agent=Mozilla/5.0"); 
        webDriver = new ChromeDriver(options);
        webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        webDriver.get("https://mabinogimobile.nexon.com/Ranking/List");
        requestCount.set(1); // 첫 요청 카운트
    }

    public synchronized void restartBrowser() {
        if (webDriver != null) {
            webDriver.quit();
        }
        launchBrowser();
    }

    public synchronized void shutdown() {
        if (webDriver != null) {
            webDriver.quit();
        }
        webDriver = null;
        requestCount.set(0);
    }

}
