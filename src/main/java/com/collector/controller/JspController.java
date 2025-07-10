package com.collector.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.collector.dto.rank.DailyPowerDto;
import com.collector.etc.TodayDate09;
import com.collector.viewService.MainViewService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class JspController {
	
	private final TodayDate09 todayDate09;
	private final MainViewService mainViewService;

	@GetMapping("/main")
	public String mainView(Model model) {
		String baseTime = todayDate09.getDateString();
		List<DailyPowerDto> daily_power = mainViewService.dailyPowerSelect(baseTime);
		
		model.addAttribute("daily_power",daily_power);
		return "mainView";
	}
}
