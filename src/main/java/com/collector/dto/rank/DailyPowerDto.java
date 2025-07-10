package com.collector.dto.rank;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DailyPowerDto {
	private Integer id;
    private Date dataDate;
    private Integer serverId;
    private Integer jobId;
    private Integer powerTop;
    private Integer powerBottom;
    private Integer powerMedian;
    private Integer powerAverage;
    private Integer powerTotal;
    private Float powerVar;
    private Float powerSd;
}
