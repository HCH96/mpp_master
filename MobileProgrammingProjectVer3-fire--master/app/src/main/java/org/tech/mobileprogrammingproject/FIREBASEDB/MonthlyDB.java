package org.tech.mobileprogrammingproject.FIREBASEDB;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/*
    황교민 2020.10.17
    1. SQLite로 데이터를 실시간으로 보여주기에 한계가 있어서 Firebase로 연동함.
 */
public class MonthlyDB {
    public String content; // monthly 할일의 내용을 담는 필드
    public long startPoint; // 시작 지점 설정(calendar -> long으로 변환) ex.2020.10.12부분을 long변환 (시간은 0시 기준으로 함)
    public long endPoint;// 종료 지점 설정(calendar -> long으로 변환) ex.2020.10.12부분을 long변환 (시간은 0시 기준으로 함)

    public MonthlyDB(){

    }

    public MonthlyDB(String content, long startPoint, long endPoint){
        this.content = content;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("content",content);
        result.put("startPoint",startPoint);
        result.put("endPoint",endPoint);
        return result;
    }
}
