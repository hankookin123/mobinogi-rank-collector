package com.collector.etc;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Component;

@Component
public class TodayDate09 {
	
	public String getDateString() {
		LocalDate today = LocalDate.now();
		LocalTime nowTime = LocalTime.now();
		LocalTime AM09 = LocalTime.of(9, 0);
		
		LocalDate getDate = nowTime.isBefore(AM09) ? today.minusDays(1) : today;
		DateTimeFormatter dateStr = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		return getDate.format(dateStr);
	}

}
