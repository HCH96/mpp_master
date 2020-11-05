package org.tech.mobileprogrammingproject.FIREBASEDB;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
/*
    황교민 2020.10.17
    1. SQLite로 데이터를 실시간으로 보여주기에 한계가 있어서 Firebase로 연동함.
 */
public class MonthlyDB {
    public String content;
    public long startPoint;
    public long endPoint;

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
