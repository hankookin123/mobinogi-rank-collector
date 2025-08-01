package com.collector.rank;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;

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
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Test
	void jdbcConnectionTest() {
	    // 데이터 삽입
	    jdbcTemplate.update("INSERT INTO db_test (id, test_text) VALUES (?, ?)", 1, "Hello via JdbcTemplate");
	
	    // 데이터 조회
	    Map<String, Object> row = jdbcTemplate.queryForMap("SELECT * FROM db_test WHERE id = ?", 1);
	
	    assertThat(row).isNotNull();
	    assertThat(row.get("test_text")).isEqualTo("Hello via JdbcTemplate");
	
	    System.out.println("JDBC 연결 및 데이터 조회 성공: " + row.get("test_text"));
	}

}
