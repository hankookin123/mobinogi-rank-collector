package com.collector.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.collector.dto.rank.DailyPowerDto;
import com.collector.etc.TodayDate09;
import com.collector.viewService.MainViewService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class ApiController {
	
	private final TodayDate09 todayDate09;
	private final MainViewService mainViewService;

	@GetMapping("/")
	public List<DailyPowerDto> MainView() {
		String baseTime = todayDate09.getDateString();
		return mainViewService.dailyPowerSelect(baseTime);
	}
}
