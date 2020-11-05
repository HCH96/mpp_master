package org.tech.mobileprogrammingproject.FIREBASEDB;

import java.util.Date;

public class MemoDB {
    public String createdDate;
    public String memo;

    public MemoDB(){

    }

    public MemoDB(String createdDate, String memo){
        this.createdDate = createdDate;
        this.memo = memo;
    }
}
