package org.tech.mobileprogrammingproject.FIREBASEDB;

import java.util.Date;

public class MemoDB {
    public String createdDate; // 저장한 시간을 저장하는 필드 -> 외부에서 접근가능하도록 구분해주는 key
    public String memo; // 메모의 내용을 저장

    public MemoDB(){

    }

    public MemoDB(String createdDate, String memo){
        this.createdDate = createdDate;
        this.memo = memo;
    }
}
