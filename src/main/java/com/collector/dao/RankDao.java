package com.collector.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.collector.dto.rank.RankingListDto;

@Mapper
public interface RankDao {
	
	public void deianInsert(@Param("list") List<RankingListDto> list);
	public void generateDailyPowerStats();
	public void generateDailyPowerStatsTest();
	public void userInsert(@Param("dto") RankingListDto dto);
}
