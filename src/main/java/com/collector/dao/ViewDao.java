package com.collector.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.collector.dto.rank.DailyPowerDto;
import com.collector.dto.rank.DpTop1000ClassDto;

@Mapper
public interface ViewDao {

	public List<DailyPowerDto> dailyPowerSelect(
			@Param("baseTime") String baseTime);
	public List<DpTop1000ClassDto> dpTop1000Class(
			@Param("baseTime") String baseTime);
}
