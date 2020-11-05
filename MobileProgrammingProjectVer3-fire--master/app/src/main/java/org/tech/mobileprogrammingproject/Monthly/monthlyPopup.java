package org.tech.mobileprogrammingproject.Monthly;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.tech.mobileprogrammingproject.FIREBASEDB.MonthlyDB;
import org.tech.mobileprogrammingproject.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/*
    황교민 2020.10.17
    1. 할일을 데이터베이스에 저장하기 위해서 팝업 페이지를 만들었다.
    2. firebase와 연결함.
 */
public class monthlyPopup extends AppCompatActivity {
    Button btnR;
    Button btnC;
    EditText endDate;
    EditText content;
    EditText startDate;
    Button btnS;
    String startYear, startMonth, startDay;
    //DatabaseHelper dbHelper;
    //SQLiteDatabase database;
    DatabaseReference database = null;
    MonthlyDB monthlydb = null;
    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar calStart = Calendar.getInstance();
    Calendar calEnd = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //dbHelper = new DatabaseHelper(this);
        //database = dbHelper.getWritableDatabase();
        database = FirebaseDatabase.getInstance().getReference();

        setContentView(R.layout.monthlypopup);
        btnR = findViewById(R.id.register);
        btnC = findViewById(R.id.cancel);
        btnS = findViewById(R.id.selectDate);

        endDate = findViewById(R.id.enddate);
        content = findViewById(R.id.monthlycontent);
        startDate = findViewById(R.id.startdate);

        startYear = Integer.toString(getIntent().getIntExtra("year",0));
        if(getIntent().getIntExtra("month", 0) + 1 < 10){
            startMonth = "0"+Integer.toString(getIntent().getIntExtra("month", 0) + 1);
        }else {
            startMonth = Integer.toString(getIntent().getIntExtra("month", 0) + 1);
        }
        if(getIntent().getIntExtra("dayOfMonth",0) < 10){
            startDay = "0" + Integer.toString(getIntent().getIntExtra("dayOfMonth",0));
        }else {
            startDay = Integer.toString(getIntent().getIntExtra("dayOfMonth", 0));
        }

        startDate.setText(startYear+"-"+startMonth+"-"+startDay);
        endDate.setText(startYear+"-"+startMonth+"-"+startDay);

        btnS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate(v, startYear, startMonth, startDay);
            }
        });

        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "작업을 취소합니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(monthlyPopup.this, Calender.class);
                intent.putExtra("RESULT_OK",0);
                finish();
            }
        });

        btnR.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(content.getText().toString().equals("")){
                    Toast.makeText(monthlyPopup.this, "할일을 입력해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    Date start = null;
                    try {
                        start = transFormat.parse(startDate.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date end = null;
                    try {
                        end = transFormat.parse(endDate.getText().toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    System.out.println(start.toString());
                    System.out.println(end.toString());
                    calStart.setTime(start);
                    calEnd.setTime(end);
                    monthlydb = new MonthlyDB(content.getText().toString(),calStart.getTimeInMillis(), calEnd.getTimeInMillis());
                    Date id = new Date();
                    database.child("monthly").child(id.toString()).setValue(monthlydb);
                    finish();
                }
            }
        });
    }

    public void pickDate(View v, String year, String month, String dayOfMonth){
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Toast.makeText(getApplicationContext(), year + "년"+(month+1) + "월" + dayOfMonth + "일",Toast.LENGTH_SHORT).show();
                if(month + 1 < 10 && dayOfMonth < 10){
                    endDate.setText(year + "-0"+(month + 1) + "-0" + dayOfMonth);
                }else if(month + 1 < 10){
                    endDate.setText(year + "-0"+(month + 1) + "-" + dayOfMonth);
                }else if(dayOfMonth < 10){
                    endDate.setText(year + "-"+(month + 1) + "-0" + dayOfMonth);
                }else {
                    endDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                }
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(this, listener, Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(dayOfMonth));
        dialog.show();
    }
}
