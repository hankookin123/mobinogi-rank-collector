package com.collector.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.collector.dto.rank.DailyPowerDto;

@Mapper
public interface ViewDao {

	public List<DailyPowerDto> dailyPowerSelect(
			@Param("baseTime") String baseTime);
}
