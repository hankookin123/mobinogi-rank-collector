package com.collector.dto.rank;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DpTop1000ClassDto {
	private Integer count_class;
    private String job_name;
    private Date data_date;
}
