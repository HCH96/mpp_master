package org.tech.mobileprogrammingproject.Daily;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.tech.mobileprogrammingproject.FIREBASEDB.DailyDB;
import org.tech.mobileprogrammingproject.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static org.tech.mobileprogrammingproject.Daily.thirdPage.changeMemo;
import static org.tech.mobileprogrammingproject.Daily.secondPage.dateTime;

public class firstPage extends Fragment implements DatePickerDialog.OnDateSetListener{

        /*
    1. variables :
        bt_date : 날짜 출력 및 DatePickerDialog 호출 버튼
        bt_add : 할 일 추가 버튼
        dateDialog : DatePickerDialog

    2. functions:
        bt_date.setOnClickListener : DatePickerDialog 호출
        onDateSet: DatePickerDialog에서 설정한 날짜로 Text 출력 값 변경

        bt_add.setOnClickListener: 할 일 추가 팝업 onClick 구현


     */
    private Button bt_date;
    private Button bt_add;
    private DatePickerDialog dateDialog;
    private LinearLayout showDaliyTodo;
    private CheckBox cb;
    // checkBox의 id를 담기 위해서
    ArrayList<String> itemIDArray = new ArrayList<>();

    DatabaseReference database = null;
    DailyDB dailydb = null;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");

    Calendar cal;
    ViewGroup rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = (ViewGroup) inflater.inflate(R.layout.firstpage, container, false);
        cal = Calendar.getInstance();
        bt_date = rootView.findViewById(R.id.bt_date);
        showDaliyTodo = rootView.findViewById(R.id.todolist);
        //firebase 연결.
        database = FirebaseDatabase.getInstance().getReference();

        // 어플 구동 시 초기 값 지정
        bt_date.setText((cal.get(Calendar.MONTH)+1) + "월 " + cal.get(Calendar.DATE) + "일");

        dateDialog = new DatePickerDialog(getContext(), this,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        bt_date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dateDialog.show();
            }
        });


        bt_add = rootView.findViewById(R.id.bt_add);
        bt_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //https://stackoverflow.com/questions/15459209/passing-argument-to-dialogfragment
                //fragment에 값 넣는 방법 intent에서 putExtra기능.
                TodoAddedDialog e = TodoAddedDialog.getInstance();

                Bundle args = new Bundle();
                args.putInt("year",cal.get(Calendar.YEAR));
                args.putInt("month", cal.get(Calendar.MONTH) + 1);
                args.putInt("day", cal.get(Calendar.DATE));
                e.setArguments(args);

                e.show(getChildFragmentManager(), TodoAddedDialog.TAG_EVENT_DIALOG);
//                Status s = Status.getInstance();
//                s.show(getChildFragmentManager(), Status.TAG_STATUS_DIALOG);
            }
        });
        setting();


                dateTime = Integer.toString(cal.get(Calendar.YEAR) * 1000 + (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DAY_OF_MONTH));
        return rootView;

    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        bt_date.setText((month+1) + "월 " + dayOfMonth + "일");

        database.child("daily").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemIDArray.clear();
                showDaliyTodo.removeViews(1,showDaliyTodo.getChildCount() - 1);
                dateTime = Integer.toString(dateDialog.getDatePicker().getYear() * 1000 + (dateDialog.getDatePicker().getMonth() + 1) * 100 + dateDialog.getDatePicker().getDayOfMonth());
                DataSnapshot today = snapshot.child(dateTime);
                for (int i = 0; i < 3; i++) {
                    DataSnapshot currData = today.child(Integer.toString(i));
                    for (DataSnapshot childData : currData.getChildren()) {
                        DailyDB currDailyDB = childData.getValue(DailyDB.class);
                        LinearLayout tr = new LinearLayout(getActivity());
                        tr.setOrientation(LinearLayout.HORIZONTAL);

                        TextView textView1 = new TextView(getContext());
                        if(i == 0) textView1.setBackgroundColor(Color.parseColor("#ccdeeb"));
                        else if(i == 1) textView1.setBackgroundColor(Color.parseColor("#B4D3E7"));
                        else textView1.setBackgroundColor(Color.parseColor("#92b5d8"));
                        textView1.setText("");
                        textView1.setGravity(Gravity.CENTER);
                        textView1.setPadding(3,5,3,5);
                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                        params1.weight = 0.15f;
                        textView1.setLayoutParams(params1);

                        TextView textView2 = new TextView(getContext());
                        textView2.setText(currDailyDB.content);
                        textView2.setGravity(Gravity.CENTER);
                        textView2.setPadding(3,5,3,5);
                        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                        params2.weight = 0.7f;
                        textView2.setLayoutParams(params2);

                        CheckBox cb = new CheckBox(getContext());
                        cb.setGravity(Gravity.CENTER);
                        cb.setPadding(3,5,3,5);
                        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                        params3.weight = 0.15f;
                        cb.setLayoutParams(params3);
                        cb.setId(itemIDArray.size() - 1);


                        tr.addView(textView1);
                        tr.addView(textView2);
                        tr.addView(cb);
                        showDaliyTodo.addView(tr);
                    }
                }
                // 2020.11.01 황교민
                // 사용자가 선택한 날짜를 공유하는 static 변수인 dateTime이 변경되었을 경우, fragment에서는 변화를 인식하지 못함.
                // changeMemo메소드를 import하여 memo의 내용을 수정하도록 함.
                changeMemo(dateTime);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setting(){
        database.child("daily").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemIDArray.clear();
                showDaliyTodo.removeViews(1,showDaliyTodo.getChildCount() - 1);
                final String currDate = Long.toString(cal.get(Calendar.YEAR) * 1000 + (cal.get(Calendar.MONTH) + 1) * 100 + cal.get(Calendar.DATE));
                DataSnapshot today = snapshot.child(currDate);
                for (int i = 0; i < 3; i++) {
                    DataSnapshot currData = today.child(Integer.toString(i));
                    for (DataSnapshot childData : currData.getChildren()) {
                        DailyDB currDailyDB = childData.getValue(DailyDB.class);
                        LinearLayout tr = new LinearLayout(getActivity());
                        tr.setOrientation(LinearLayout.HORIZONTAL);

                        TextView textView1 = new TextView(getContext());
                        if(i == 0) textView1.setBackgroundColor(Color.parseColor("#ccdeeb"));
                        else if(i == 1) textView1.setBackgroundColor(Color.parseColor("#B4D3E7"));
                        else textView1.setBackgroundColor(Color.parseColor("#92b5d8"));
                        textView1.setText("");
                        textView1.setGravity(Gravity.CENTER);
                        textView1.setPadding(3,5,3,5);
                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                        params1.weight = 0.15f;
                        textView1.setLayoutParams(params1);

                        TextView textView2 = new TextView(getContext());
                        textView2.setText(currDailyDB.content);
                        textView2.setGravity(Gravity.CENTER);
                        textView2.setPadding(3,5,3,5);
                        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                        params2.weight = 0.7f;
                        textView2.setLayoutParams(params2);

                        CheckBox cb = new CheckBox(getContext());
                        cb.setGravity(Gravity.CENTER);
                        cb.setPadding(3,5,3,5);
                        LinearLayout.LayoutParams params3 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
                        params3.weight = 0.15f;
                        cb.setLayoutParams(params3);
                        cb.setId(itemIDArray.size() - 1);


                        tr.addView(textView1);
                        tr.addView(textView2);
                        tr.addView(cb);
                        showDaliyTodo.addView(tr);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
