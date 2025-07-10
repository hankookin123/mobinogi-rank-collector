package com.collector.rank;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

import com.collector.etc.TodayDate09;

@SpringBootTest
public class EtcTest {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private TodayDate09 date09;
	
	@Test
	public void device_id_test() {
		String device_id = env.getProperty("device_id");
		System.out.println(device_id);
	}
	
	@Test
	public void timeTest() {
		String dateString = date09.getDateString();
		System.out.println("---"+dateString+"---");
	}

}
