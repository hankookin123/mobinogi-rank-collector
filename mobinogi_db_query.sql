commit;

select count(*) from deian_server;
rollback;
show tables;

select * from job_code;
select * from server_code;
select * from job_code;

select count(*), job_id from deian_server where data_date = '2025-07-13' group by job_id;
select * from deian_server where data_date = '2025-07-13';

select count(*), data_date from deian_server group by data_date order by data_date desc;
select count(*), data_date from daily_power group by data_date order by data_date desc;
select * from daily_power;
select count(*), data_date from daily_power group by data_date order by data_date desc;

select id ,
	dp.data_date "날짜",
    sc.server_name "서버",
    jc.job_name "직업",
    dp.power_top "1등",
    dp.power_bottom "1000등",
    dp.power_median "500등",
    dp.power_average "평균",
    dp.power_total "총합",
    dp.power_var "분산",
    dp.power_sd "표준편차" 
from daily_power dp
join server_code sc on dp.server_id = sc.server_id
join job_code jc on dp.job_id = jc.job_id
order by dp.power_top desc;


select * from daily_power
where data_date = curdate();

select * from db_test;

-- db-test
create table db_test(
	id int,
    test_text varchar(100)
);

-- 직업 코드
create table job_code(
	job_id int primary key,
    job_name varchar(50) unique key not null
);

-- 서버 코드
create table server_code(
	server_id int primary key,
    server_name varchar(50) unique key not null
);

-- 데이안 서버 직업별 1000등까지
create table deian_server(
	id int auto_increment primary key,
	data_date date default (CURDATE()) not null,
	c_p_rank int not null,
    c_name varchar(50) not null,
    job_id int not null,
    c_power int not null,
    index idx_date_job_rank (data_date, job_id, c_p_rank)
);

-- 매일 통계
create table daily_power(
	id int auto_increment primary key,
	data_date date not null,
    server_id int not null,
    job_id int not null,
    power_top int not null,
    power_bottom int not null,
    power_median int not null,
    power_average int not null,
    power_total int not null,
    power_var float not null,
    power_sd float not null,
    UNIQUE KEY uq_data (data_date, server_code, job_code),
    INDEX idx_date_server_job (data_date, server_code, job_code)
);

-- 길드 정보
create table guild_info(
	id int primary key auto_increment,
    g_id varchar(50) unique not null,
    g_password varchar(255) not null,
    g_name varchar(50) not null,
    g_server int not null,
    g_text text
);
    
-- 유저 정보
create table user_info(
	id int primary key auto_increment,
    g_id int not null,
    update_time timestamp
);

-- 캐릭터 정보
create table character_info(
	id int primary key auto_increment,
    u_id int not null,
    c_name varchar(50) not null,
    c_no int not null
);

-- 전투력
create table character_power(
	id int primary key auto_increment,
    c_id int not null,
    c_power int not null,
    update_time timestamp
);

/*
delete from deian_server where id > 0;
delete from daily_power where data_date > '2025-07-11';

drop table deian_server;
drop table server_code;
drop table job_code;
drop table daily_power;
*/

-- 컬럼 명 변경
alter table daily_power change server_code server_id int not null;

-- 테이블 명 변경
alter table job_code rename to job_code;

-- 자동증가 재설정
alter table deian_server auto_increment = 1;

-- 날짜+클래스 , 랭킹 으로 인덱스 생성해서 나중에 조회나 통계낼때 빠르게?
create index idx_class_date on deian_server(job_id, insert_date);
create index idx_rank on deian_server(c_p_rank);

-- 인덱스 추가 삭제
drop index idx_date_job on deian_server;
create index idx_date_job_rank on deian_server (data_date, job_id, c_p_rank);
show index from deian_server;



SHOW CREATE TABLE deian_server;



-- --------------------------   서버, 직업코드
INSERT INTO job_code (job_id, job_name) VALUES (1285686831, '전사');
INSERT INTO job_code (job_id, job_name) VALUES (2077040965, '대검전사');
INSERT INTO job_code (job_id, job_name) VALUES (958792831, '검술사');
INSERT INTO job_code (job_id, job_name) VALUES (995607437, '궁수');
INSERT INTO job_code (job_id, job_name) VALUES (1468161402, '석궁사수');
INSERT INTO job_code (job_id, job_name) VALUES (1901800669, '장궁병');
INSERT INTO job_code (job_id, job_name) VALUES (1876490724, '마법사');
INSERT INTO job_code (job_id, job_name) VALUES (1452582855, '화염술사');
INSERT INTO job_code (job_id, job_name) VALUES (1262278397, '빙결술사');
INSERT INTO job_code (job_id, job_name) VALUES (323147599, '힐러');
INSERT INTO job_code (job_id, job_name) VALUES (1504253211, '사제');
INSERT INTO job_code (job_id, job_name) VALUES (204163716, '수도사');
INSERT INTO job_code (job_id, job_name) VALUES (1319349030, '음유시인');
INSERT INTO job_code (job_id, job_name) VALUES (413919140, '댄서');
INSERT INTO job_code (job_id, job_name) VALUES (956241373, '악사');
INSERT INTO job_code (job_id, job_name) VALUES (1443648579, '도적');
INSERT INTO job_code (job_id, job_name) VALUES (1790463651, '격투가');
INSERT INTO job_code (job_id, job_name) VALUES (1957076952, '듀얼블레이드');
INSERT INTO job_code (job_id, job_name) VALUES (589957914, '전격술사');

INSERT INTO server_code (server_id, server_name) VALUES (1, '데이안');
INSERT INTO server_code (server_id, server_name) VALUES (2, '아이라');
INSERT INTO server_code (server_id, server_name) VALUES (3, '던컨');
INSERT INTO server_code (server_id, server_name) VALUES (4, '알리사');
INSERT INTO server_code (server_id, server_name) VALUES (5, '메이븐');
INSERT INTO server_code (server_id, server_name) VALUES (6, '라사');
INSERT INTO server_code (server_id, server_name) VALUES (7, '칼릭스');

-- -------------------------------------------------------------

drop PROCEDURE generate_daily_power_stats_test;

-- 일일 통계 프로시저
DELIMITER $$
CREATE PROCEDURE generate_daily_power_stats()
BEGIN
    INSERT INTO daily_power (
        data_date, server_id, job_id, 
        power_top, power_bottom, power_median, 
        power_average, power_total, power_var, power_sd
    )
    SELECT 
        ds.data_date,
        1 AS server_id,
        ds.job_id,
        MAX(ds.c_power),
        MIN(ds.c_power),
        (
            SELECT c_power 
            FROM deian_server 
            WHERE data_date = CURDATE() 
            AND job_id = ds.job_id 
            ORDER BY c_p_rank 
            LIMIT 1 OFFSET 499
        ),
        AVG(ds.c_power),
        SUM(ds.c_power),
        VARIANCE(ds.c_power),
        STDDEV(ds.c_power)
    FROM deian_server ds
    WHERE ds.data_date = CURDATE()
    GROUP BY ds.data_date, ds.job_id;
END $$
DELIMITER ;

-- 테스트용 일일 통계 프로시저
DELIMITER $$
CREATE PROCEDURE generate_daily_power_stats_test()
BEGIN
    INSERT INTO daily_power (
        data_date, server_id, job_id, 
        power_top, power_bottom, power_median, 
        power_average, power_total, power_var, power_sd
    )
    SELECT 
        ds.data_date,
        1 AS server_id,
        ds.job_id,
        MAX(ds.c_power),
        MIN(ds.c_power),
        (
            SELECT c_power 
            FROM deian_server 
            WHERE data_date = CURDATE() 
            AND job_id = ds.job_id 
            ORDER BY c_p_rank 
            LIMIT 1 OFFSET 19  -- 40개 중 20번째 값
        ),
        AVG(ds.c_power),
        SUM(ds.c_power),
        VARIANCE(ds.c_power),
        STDDEV(ds.c_power)
    FROM deian_server ds
    WHERE ds.data_date = CURDATE()
    GROUP BY ds.data_date, ds.job_id;
END $$
DELIMITER ;


call generate_daily_power_stats_test();

drop procedure generate_daily_power_stats_Test;


commit;


-- -----------------------------
SELECT 
  table_name AS 'Table',
  ROUND((data_length + index_length) / 1024 / 1024, 2) AS 'Size (MB)'
FROM information_schema.TABLES
WHERE table_schema = 'mobinogi_db';


SELECT * FROM daily_power WHERE data_date = "2025-07-10";

--  -----------------------
select count(*), job_id from deian_server where data_date = '2025-07-13' group by job_id order by id desc;

select t.id, d.c_p_rank
from test_1_1000 t
left join (select * from deian_server where data_date = '2025-07-13' and job_id = 1262278397) d on d.c_p_rank = t.id
where d.c_p_rank is null;

update deian_server set data_date = '2025-07-14' where data_date = '2025-07-15';



-- ---------------------------------
-- mapper test
SELECT
	        id ID,
	        data_date 날짜,
	        server_id 서버,
	        job_id 직업,
	        power_top 1등,
	        power_bottom 1000등,
	        power_median 500등,
	        power_average 평균,
	        power_total 총합,
	        power_var 분산,
	        power_sd 표준편차
	    FROM daily_power
	    WHERE data_date >= '2025-08-02';
        
        
-- 주 단위
SELECT job_id,
       DATE_FORMAT(data_date, '%Y-%u') AS week,
       AVG(power_average) AS avg_power,
       AVG(power_median) AS median_power,
       AVG(power_top) AS top_power,
       AVG(power_bottom) AS bottom_power,
       AVG(power_sd) AS sd_power
FROM daily_power
WHERE data_date >= '2025-08-01'
GROUP BY job_id, week
ORDER BY week;
