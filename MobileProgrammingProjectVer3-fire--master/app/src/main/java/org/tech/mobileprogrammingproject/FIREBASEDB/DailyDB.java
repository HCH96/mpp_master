package org.tech.mobileprogrammingproject.FIREBASEDB;

import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DailyDB {
    public String content;
    public int state;
    public long date;
    public int startTime;
    public int endTime;
    public String time;
    public String catalog;
    public String createDate;
    public int timeline;

    public DailyDB(){

    }

    public DailyDB(String createDate, String content, int state, long date, int startTime, int endTime, String time, String catalog, int timeline){
        this.createDate = createDate;
        this.content = content;
        this.timeline = timeline;
        this.state = state;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.catalog = catalog;
        this.time = time;
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
        result.put("time", time);
        return result;
    }
}