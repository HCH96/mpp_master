package org.tech.mobileprogrammingproject.FIREBASEDB;

import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DailyDB {
    public String content; // daily할일의 내용을 저장
    public int state; // daily할일의 상태(완료 - 3 / ~완료 - 0, 1, 2)를 저장
    public long date; // daily할일의 날짜를 저장하는 필드(cal로 받아온 후 long으로 변환)
    public int startTime; // 시작 시간을 저장 (시간 + 분 ex. 10시 45분 -> 1045)
    public int endTime; // 종료시간을 저장 (시간 + 분 ex. 10시 45분 -> 1045)
    public String catalog; // 카테고리 지정(미정, 운동, 공부, 과제)
    public String createDate; // 생성 시간을 저장 -> 외부에서 접근 가능한 key
    public int timeline; // 시간대를 설정(아침 - 0, 점심 - 1, 저녁 - 2)

    public DailyDB(){

    }

    public DailyDB(String createDate, String content, int state, long date, int startTime, int endTime, String catalog, int timeline){
        this.createDate = createDate;
        this.content = content;
        this.timeline = timeline;
        this.state = state;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.catalog = catalog;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("createDate", createDate);
        result.put("content", content);
        result.put("state", state);
        result.put("date", date);
        result.put("startTime", startTime);
        result.put("endTime", endTime);
        result.put("catalog", catalog);
        return result;
    }
}