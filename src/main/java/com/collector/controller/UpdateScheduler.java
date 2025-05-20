package com.collector.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.collector.mainService.SearchLoofService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UpdateScheduler {
	
	private final SearchLoofService searchLoofService;
	
	@Scheduled(cron = "0 0 4 * * *", zone = "Asia/Seoul")
	public void dbUpdateSchedule() {
		try {
			searchLoofService.dbUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
