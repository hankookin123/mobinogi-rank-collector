package com.collector.mainService;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.collector.dao.RankDao;
import com.collector.dto.rank.RankingListDto;
import com.collector.etc.ClassCode;

// DB에 등록하는곳
@Service
public class SearchLoofService {
	
	private RankCollectService rankCollectService;
	private RankDao dao;
	
	// 이렇게 객체를 new로 안만들고 파라미터로 받아오는 것을 의존성 주입이라하고,
	// 테스트 용이성,결합도 감소 같은 설명들은 rankdao랑 연결시킨 것이 아니라 '받아서 사용한다'라고 코드를 짜서다.
	public SearchLoofService(RankDao dao, RankCollectService rankCollectService) {
		this.dao = dao;
		this.rankCollectService = rankCollectService;
	}
	
	private int pageCount = 50;
	
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
	public void dbUpdate() throws Exception {
		
		Random random =new Random();
		StringBuilder err_msg = new StringBuilder(); // 에러 구간 위치
		
		LocalDateTime now1 = LocalDateTime.now();
		System.out.println("데이터수집 시작 : " + now1);
		
		// 데이안서버(서버번호: 1)의 데이터만 수집.
		int total_search = 0; //총 검색 횟수
		int err_count = 0;  // 총 에러 횟수
		
		for (Integer code : ClassCode.직업_코드맵.values()) {
			List<RankingListDto> rankingList = new ArrayList<>();
			for (int i = 1; i <= pageCount; i++) {
				try {
					rankCollectService.rankCollect(1, i, 1, code, null, rankingList);
					total_search++;
					Thread.sleep(random.nextInt(500)+2500);
					System.out.println("총 횟수 : " + total_search + "  /  " 
							+ ClassCode.코드_직업맵.get(code) + " 페이지 : " + i);
				} catch (InterruptedException  e) {
					// 인터럽 복구 해줘야 한다?
					Thread.currentThread().interrupt();
				} catch (Exception e) {
					err_count ++;
					err_msg.append("/ ").append(ClassCode.코드_직업맵.get(code))
					.append(" ").append(i).append("~").append(i+20).append("등 ");
					e.printStackTrace();
				}
			}
			
			dao.deianInsert(rankingList);
		}		
		
		LocalDateTime now2 = LocalDateTime.now();
		System.out.println("--- 데이터수집 시작 : " + now1);
		System.out.println("--- 데이터수집 종료 : " + now2);
		System.out.println("--- 검색한 페이지 수 : " + total_search);
		System.out.println("--- 검색 오류 횟수 : " + err_count);
		System.out.println("--- 검색 오류 등수 : " + err_msg);
	}
	
}
