package com.collector.mainService;

import org.springframework.stereotype.Service;

import com.collector.dao.RankDao;
import com.collector.dto.rank.RankingListDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserInfoSaveService {
	
	private RankingListDto dto;
	private RankDao dao;
	private final RankCollectServiceSelenium rankCollectServiceSelenium;
	
	public void userInfoSave( int s, String search) {
		// (int t, int pageno, int s, int c, String search, int searchCount)
		try {
			//dto = rankCollectServiceSelenium.userInfo(1, 0, s, 0, search);
			// 유저정보 인서트 하려면 db테이블 먼저 만들고 삽입 해야함.
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
