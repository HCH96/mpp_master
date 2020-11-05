package org.tech.mobileprogrammingproject.Login;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.tech.mobileprogrammingproject.SQLDB.DatabaseHelper;
import org.tech.mobileprogrammingproject.R;

public class register extends AppCompatActivity {
    DatabaseHelper dbHelper;
    SQLiteDatabase database;
    EditText pw, check,id;
    Button goRegister;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        id = (EditText) findViewById(R.id.userId);
        pw = (EditText) findViewById(R.id.userpw);
        check = (EditText) findViewById(R.id.userpwcheck);
        goRegister = (Button) findViewById(R.id.register);

        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();

        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = id.getText().toString();
                String userInput = pw.getText().toString();
                String userInputCheck = check.getText().toString();

                boolean isValidId = true;
                Cursor cursor = database.rawQuery("select id from user", null);
                int num_of_records = cursor.getCount();
                for (int i = 0 ; i < num_of_records ; i++){
                    cursor.moveToNext();
                    if(userId.equals(cursor.getString(0))){
                        isValidId = false;
                        break;
                    }
                }
                if(userInput.equals(userInputCheck) && isValidId){
                    Toast.makeText(getApplicationContext(), "회원가입 성공!", Toast.LENGTH_LONG).show();
                    String sql = "INSERT INTO user(id, pw) VALUES(?,?)";
                    Object[] params = {userId, userInput};
                    database.execSQL(sql,params);
                    Intent intent = new Intent(getApplicationContext(), login.class);
                    startActivity(intent);
                }else if(!isValidId){
                    Toast.makeText(getApplicationContext(), "아이디 중복!", Toast.LENGTH_LONG).show();
                    id.setText("");
                    pw.setText("");
                    check.setText("");
                }
                else{
                    Toast.makeText(getApplicationContext(), "비밀번호가 달라요!", Toast.LENGTH_LONG).show();
                    id.setText("");
                    pw.setText("");
                    check.setText("");
                }
            }
        });
    }
}
