package org.tech.mobileprogrammingproject.Login;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.tech.mobileprogrammingproject.SQLDB.DatabaseHelper;
import org.tech.mobileprogrammingproject.Daily.MainActivity;
import org.tech.mobileprogrammingproject.R;


public class login extends AppCompatActivity {
    DatabaseHelper dbHelper;
    SQLiteDatabase database;
    EditText userInputId;
    EditText userInputPw;
    public static final int LOGIN_OK = 400;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        userInputId = (EditText)findViewById(R.id.userInputId);
        userInputPw = (EditText)findViewById(R.id.userInputPw);

        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();
    }
    /*
        로그인 함수 -> intent로 mainActivity연결
     */
    public void isValid(View v){

        Cursor cursor = database.rawQuery("select id, pw from user", null);
        int num_of_records = cursor.getCount();
        boolean isIn = false;
        for (int i = 0 ; i < num_of_records ; i++){
            cursor.moveToNext();
            if(userInputId.getText().toString().equals(cursor.getString(0)) && userInputPw.getText().toString().equals(cursor.getString(1))){
                isIn = true;
                break;
            }
        }
        if(!isIn){
            Toast.makeText(this.getApplicationContext(), "회원가입을 해주세요!", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent_login = new Intent(getApplicationContext(), MainActivity.class);
            startActivityForResult(intent_login, LOGIN_OK);
        }
    }
    /*
       회원가입 함수 -> intent로 register페이지 연결
    */
    public void getRegister(View v){
        Intent intent_register = new Intent(getApplicationContext(), register.class);
        startActivity(intent_register);
    }
}
