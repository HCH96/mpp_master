package org.tech.mobileprogrammingproject.FIREBASEDB;

import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DailyDB {
    public String content;
    public int state;
    public long date;
    public long startTime;
    public long endTime;
    public String catalog;

    public DailyDB(){

    }

    public DailyDB(Date createDate, String content, int state, long date, long startTime, long endTime, String catalog){
        this.content = content;
        this.state = state;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.catalog = catalog;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("state", state);
        result.put("date", date);
        result.put("startTime", startTime);
        result.put("endTime", endTime);
        result.put("catalog", catalog);
        return result;
    }
}
