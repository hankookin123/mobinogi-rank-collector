package com.collector;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.collector.dao.RankDao;
import com.collector.mainService.SearchLoofService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "DEVICE_ID", havingValue = "web_craw_pc")
public class UpdateScheduler {
	
	private final SearchLoofService searchLoofService;
	private final RankDao rankDao;
	
	@Scheduled(cron = "0 0 4 * * *", zone = "Asia/Seoul")
	public void dbUpdateSchedule() {
		try {
			searchLoofService.dbUpdate();
			rankDao.generateDailyPowerStats();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
