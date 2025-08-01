package com.collector.viewService;

import java.util.List;

import org.springframework.stereotype.Service;

import com.collector.dao.ViewDao;
import com.collector.dto.rank.DailyPowerDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainViewService {
	
	private final ViewDao dao;
	
	public List<DailyPowerDto> dailyPowerSelect(String baseTime) {
		return dao.dailyPowerSelect(baseTime);
	}
}
