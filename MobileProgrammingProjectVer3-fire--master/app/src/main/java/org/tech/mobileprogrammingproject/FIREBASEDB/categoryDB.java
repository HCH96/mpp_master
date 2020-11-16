package org.tech.mobileprogrammingproject.FIREBASEDB;

public class categoryDB {
    public String categoryName; // 미지정, 운동, 과제, 공부 카테고리를 저장하기 위한 필드

    public categoryDB(){

    }

    public categoryDB(String categoryName){
        this.categoryName = categoryName;
    }
}
