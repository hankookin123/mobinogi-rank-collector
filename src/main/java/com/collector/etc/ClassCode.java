package com.collector.etc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassCode {
	// 추후 직업클래스가 내부클래스 안으로 들어가게 되면 내부 클래스에도 static을 붙이는 게 좋다.
	
	// public static final은 상속받은 클래스에서 사용할 수 없다.
	// 상속해서 쓸려면 protected, public static final 으로 만들어야한다.
	// public static final 을 붙이면 인스턴스 생성 없이 <클래스이름.변수명>으로 바로 접근할 수 있다.
	public static final int 전사 = 1285686831;  
	public static final int 대검전사 = 2077040965  ; 
	public static final int 검술사 = 958792831   ;
	public static final int 궁수 = 995607437   ;
	public static final int 석궁사수 = 1468161402   ;
	public static final int 장궁병 = 1901800669   ;
	public static final int 마법사 = 1876490724   ;
	public static final int 화염술사 = 1452582855   ;
	public static final int 빙결술사 = 1262278397   ;
	public static final int 힐러 = 323147599   ;
	public static final int 사제 = 1504253211   ;
	public static final int 수도사 = 204163716   ;
	public static final int 음유시인 = 1319349030   ;
	public static final int 댄서 = 413919140   ;
	public static final int 악사 = 956241373   ;
	public static final int 도적 = 1443648579 ;
	public static final int 격투가 = 1790463651 ;
	public static final int 듀얼블레이드 = 1957076952 ;
	
	public static final int 데이안 = 1;
	public static final int 아이라 = 2;
	public static final int 던컨 = 3;
	public static final int 알리사 = 4;
	public static final int 메이븐 = 5;
	public static final int 라사 = 6;
	public static final int 칼릭스 = 7;
	
	//Arrays.asList 사용하면 add, remove 사용 불가, set으로 변경은 가능.
	// 직업코드리스트 지워도 됨.
	public static final List<Integer> 직업코드리스트 = Arrays.asList(
			전사, 대검전사, 검술사, 궁수, 석궁사수, 장궁병, 마법사, 화염술사, 빙결술사,
	        힐러, 사제, 수도사, 음유시인, 댄서, 악사, 도적, 격투가, 듀얼블레이드
			);
	
	// 가독성?을 위해서 위에 각 직업 변수는 살려둠.
	public static final Map<String, Integer> 직업_코드맵;
	static {
		직업_코드맵 = new HashMap<>();
		직업_코드맵.put("전사",전사);
		직업_코드맵.put("대검전사", 대검전사);
        직업_코드맵.put("검술사", 검술사);
        직업_코드맵.put("궁수", 궁수);
        직업_코드맵.put("석궁사수", 석궁사수);
        직업_코드맵.put("장궁병", 장궁병);
        직업_코드맵.put("마법사", 마법사);
        직업_코드맵.put("화염술사", 화염술사);
        직업_코드맵.put("빙결술사", 빙결술사);
        직업_코드맵.put("힐러", 힐러);
        직업_코드맵.put("사제", 사제);
        직업_코드맵.put("수도사", 수도사);
        직업_코드맵.put("음유시인", 음유시인);
        직업_코드맵.put("댄서", 댄서);
        직업_코드맵.put("악사", 악사);
        직업_코드맵.put("도적", 도적);
        직업_코드맵.put("격투가", 격투가);
        직업_코드맵.put("듀얼블레이드", 듀얼블레이드);
	}
	
	public static final Map<Integer, String> 코드_직업맵;
	static {
	    코드_직업맵 = new HashMap<>();
	    for (Map.Entry<String, Integer> entry : 직업_코드맵.entrySet()) {
	        코드_직업맵.put(entry.getValue(), entry.getKey());
	    }
	}
	
	public static final Map<String, Integer> 서버_코드맵;
	static {
		서버_코드맵 = new HashMap<>();
		서버_코드맵.put("데이안", 데이안);
		서버_코드맵.put("아이라", 아이라);
		서버_코드맵.put("던컨", 던컨);
		서버_코드맵.put("알리사", 알리사);
		서버_코드맵.put("메이븐", 메이븐);
		서버_코드맵.put("라사", 라사);
		서버_코드맵.put("칼릭스", 칼릭스);
	}
	
	public static final Map<Integer, String> 코드_서버맵;
	static {
		코드_서버맵 = new HashMap<>();
		for (Map.Entry<String, Integer> entry : 서버_코드맵.entrySet()) {
			코드_서버맵.put(entry.getValue(), entry.getKey());
		}
	}


}
