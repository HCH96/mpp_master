package org.tech.mobileprogrammingproject.SQLDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static String NAME = "mobileProgrammingDB.db";
    // 버전 관리를 위해서 설정함.
    public static int VERSION = 1;

    public DatabaseHelper(Context context){
        super(context, NAME, null, VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        String daily_sql = "create table if not exists " + "daily" + "("+
                "id integer PRIMARY KEY autoincrement, "+
                "created_at date, "+
                "content text, "+
                "state integer, "+
                "start_time time, "+
                "end_time time)";

        String monthly_sql = "create table if not exists " + "monthly" + "("+
                "id integer PRIMARY KEY autoincrement, "+
                "state integer, "+
                "content text, "+
                "start_point date, "+
                "end_point date, "+
                "cycle integer)";

        String memo_sql = "create table if not exists " + "memo" + "("+
                "created_at date PRIMARY KEY, "+
                "memo text)";

        db.execSQL(daily_sql);
        db.execSQL(monthly_sql);
        db.execSQL(memo_sql);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        if(newVersion > 1){
            db.execSQL("DROP TABLE IF EXISTS user");
            db.execSQL("DROP TABLE IF EXISTS daily");
            db.execSQL("DROP TABLE IF EXISTS monthly");
            db.execSQL("DROP TABLE IF EXISTS memo");
        }
    }
}
