package com.collector;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.collector.dao.RankDao;
import com.collector.mainService.SearchLoofService;

import lombok.RequiredArgsConstructor;

// CHECK_DEVICE_ID 확인하고 스케줄러 빈에 등록
@Component
@Profile("device-n150")
@RequiredArgsConstructor
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
