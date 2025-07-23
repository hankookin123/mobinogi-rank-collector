package com.collector.rank;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.collector.UpdateScheduler;
import com.collector.dao.RankDao;
import com.collector.dto.rank.RankingListDto;
import com.collector.mainService.RankCollectService;
import com.collector.mainService.SearchLoofService;

@SpringBootTest
public class UpdateSchedulerTest {
	
	@Value("${COOKIE_INFO}") private String cookie_info;

	@Autowired
    private RankDao rankDao;

    @Autowired
    private SearchLoofService searchLoofService;
    
    @Autowired
    private RankCollectService rankCollectService;
    
    @Autowired
    private RankDao dao;

//    @Autowired
//    private UpdateScheduler updateScheduler;
//    
//    @Test
//    public void testDbupdate() throws Exception {
//    	updateScheduler.dbUpdateSchedule();
//    	// ec2 커널링으로 db 접속하려는데 권한이 없다는 오류 지속적으로 발생.
//    }
    
    @Test
    public void searchLoofServiceTest() {
    	System.out.println("- - - - -  테스트 시작  - - - - -");
    	try {
    		// searchLoofService.setPageCount(2);
    		searchLoofService.dbUpdate();
    		// 프로시저에서 중앙값 할때 오프셋 499 되어있는데 테스트에서는 2번만 검색해서 중강값이 20이다.
        	// rankDao.generateDailyPowerStatsTest(); 
        	rankDao.generateDailyPowerStats();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	System.out.println("- - - - -  테스트 종료  - - - - -");
    }
    
    @Test
    public void errRankCollecte() {
    	List<RankingListDto> rankingList = new ArrayList<>();
    	try {
			rankCollectService.rankCollect(1, 35, 1, 1262278397, null, rankingList);
			dao.deianInsert(rankingList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	//int t, int pageno, int s, int c, String search, List<RankingListDto> rankingList
    	
    }
    
    @Test
    public void testCookieInfo() {
    	System.out.println("cookie_text : " + cookie_info);
    	
    }
}
