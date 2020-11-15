package org.tech.mobileprogrammingproject.Daily;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.tech.mobileprogrammingproject.FIREBASEDB.DailyDB;
import org.tech.mobileprogrammingproject.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import static org.tech.mobileprogrammingproject.Daily.firstPage.dateTime;

public class Status extends DialogFragment implements View.OnClickListener {

    private EditText et_startTime;
    private EditText et_endTime;
    private CheckBox cb_none;
    private Button bt_cancelDone;
    private Button bt_done;
    private Calendar currentTime;

    int startHour = 30;
    int startMin = 90;
    int endHour = 40;
    int endMin = 100;

    String createdDate;
    String content;
    String catalog;
    String dateTime;
    int timeline;
    Long dateLong;

    TimePickerDialog endTimePicker;

    DatabaseReference database = null;
    DailyDB dailydb = null;
    DailyDB timeUpdateDb = null;

    ArrayList<Integer> startArray = new ArrayList<>();
    ArrayList<Integer> endArray = new ArrayList<>();

    public static final String TAG_STATUS_DIALOG = "status_event";
    public static Status getInstance() {
        Status s = new Status();
        return s;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.status_popup, container, false);

        et_startTime = v.findViewById(R.id.startTime);
        et_endTime = v.findViewById(R.id.endTime);
        bt_cancelDone = v.findViewById(R.id.bt_cancelDone);
        bt_done = v.findViewById(R.id.bt_done);
        cb_none = v.findViewById(R.id.cb_none);

        et_startTime.setOnClickListener(this);
        et_startTime.setFocusable(false);
        et_endTime.setOnClickListener(this);
        et_endTime.setFocusable(false);
        bt_cancelDone.setOnClickListener(this);
        bt_done.setOnClickListener(this);

        database = FirebaseDatabase.getInstance().getReference();

        createdDate = getArguments().getString("createdDate");
        content = getArguments().getString("content");
        catalog = getArguments().getString("catalog");
        dateTime = getArguments().getString("dateTime");
        timeline = getArguments().getInt("timeline");
        dateLong = getArguments().getLong("dateLong");

        database.child("daily").child(Long.toString(dateLong)).child("3").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot childData : snapshot.getChildren()){
                    DailyDB currDailyDB = childData.getValue(DailyDB.class);
                    startArray.add(currDailyDB.startTime);
                    endArray.add(currDailyDB.endTime);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setCancelable(false);
        return v;
    }


    @Override
    public void onClick(View v) {

        et_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        et_startTime.setText(selectedHour + "시 " + selectedMinute + "분");
                        startHour = selectedHour;
                        startMin = selectedMinute;
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Start Time");
                mTimePicker.show();
            }
        });

        et_endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                endTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int endSelectedHour, int endSelectedMinute) {
                        et_endTime.setText(endSelectedHour + "시 " + endSelectedMinute + "분");
                        endHour = endSelectedHour;
                        endMin = endSelectedMinute;
                    }
                }, hour, minute, false);

                endTimePicker.setTitle("End Time");
                endTimePicker.show();
            }
        });

        cb_none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_startTime.getText().clear();
                et_endTime.getText().clear();
            }
        });

        bt_cancelDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "취소되었습니다.", Toast.LENGTH_LONG).show();
                DailyDB cancelDb = new DailyDB();
                cancelDb.createDate = createdDate;
                cancelDb.content = content;
                cancelDb.state = 0;
                cancelDb.timeline = timeline;
                cancelDb.catalog = catalog;
                cancelDb.date = dateLong;

                //database.child("daily").child(Long.toString(dateLong)).child(String.valueOf(timeline)).child(createdDate).removeValue();
                //database.child("daily").child(Long.toString(dateLong)).child(String.valueOf(timeline)).child(createdDate).setValue(cancelDb);
                dismiss();
            }
        });

        bt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startHour == 30 && endHour == 40 && cb_none.isChecked() == false) {
                    et_startTime.setText("시간 정보가 입력되지 않았습니다.");
                    et_startTime.setTextColor(Color.parseColor("#7bb0db"));
                    et_startTime.setTextSize(15);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            et_startTime.getText().clear();
                            et_startTime.setTextColor(Color.parseColor("#000000"));
                            et_startTime.setTextSize(18);
                        }
                    }, 2000);
                } else if (startHour != 30 && endHour == 40) {
                    et_endTime.setText("종료 시간이 입력되지 않았습니다.");
                    et_endTime.setTextColor(Color.parseColor("#7bb0db"));
                    et_endTime.setTextSize(15);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            et_endTime.getText().clear();
                            et_endTime.setTextColor(Color.parseColor("#000000"));
                            et_endTime.setTextSize(18);
                        }
                    }, 2000);
                } else if (startHour == 30 && endHour != 40) {
                    et_startTime.setText("시작 시간이 입력되지 않았습니다.");
                    et_startTime.setTextColor(Color.parseColor("#7bb0db"));
                    et_startTime.setTextSize(15);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            et_startTime.getText().clear();
                            et_startTime.setTextColor(Color.parseColor("#000000"));
                            et_startTime.setTextSize(18);
                        }
                    }, 2000);
                } else if(endHour < startHour && cb_none.isChecked() == false || endHour == startHour && endMin < startMin && cb_none.isChecked() == false){
                    et_endTime.setText("시작 시간보다 빠릅니다!\n시간을 재설정해주세요.");
                    et_endTime.setTextColor(Color.parseColor("#ff8682"));
                    et_endTime.setTextSize(15);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            et_endTime.setText(endHour + "시 " + endMin + "분");
                            et_endTime.setTextColor(Color.parseColor("#000000"));
                            et_endTime.setTextSize(18);
                        }
                    }, 2000);
                } else if(endHour == startHour && endMin == startMin && cb_none.isChecked() == false){
                    et_endTime.setText("시작 시간과 같습니다!\n시간을 재설정해주세요.");
                    et_endTime.setTextColor(Color.parseColor("#ff8682"));
                    et_endTime.setTextSize(15);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            et_endTime.setText(endHour + "시 " + endMin + "분");
                            et_endTime.setTextColor(Color.parseColor("#000000"));
                            et_endTime.setTextSize(18);
                        }
                    }, 2000);
                } else if (cb_none.isChecked() == true){
                    timeUpdateDb = new DailyDB();
                    timeUpdateDb.createDate = createdDate;
                    timeUpdateDb.content = content;
                    timeUpdateDb.state = 0;
                    timeUpdateDb.timeline = 3;
                    timeUpdateDb.catalog = catalog;
                    timeUpdateDb.date = dateLong;
                    timeUpdateDb.startTime = 0;
                    timeUpdateDb.endTime = 0;

                    database.child("daily").child(Long.toString(timeUpdateDb.date)).child(String.valueOf(timeline)).child(timeUpdateDb.createDate).removeValue();

                    database.child("daily").child(Long.toString(timeUpdateDb.date)).child("3").child(timeUpdateDb.createDate).removeValue();
                    database.child("daily").child(Long.toString(timeUpdateDb.date)).child("3").child(timeUpdateDb.createDate).setValue(timeUpdateDb);

                    dismiss();
                } else {
                    timeUpdateDb = new DailyDB();
                    timeUpdateDb.createDate = createdDate;
                    timeUpdateDb.content = content;
                    timeUpdateDb.state = 0;
                    timeUpdateDb.timeline = 3;
                    timeUpdateDb.catalog = catalog;
                    timeUpdateDb.date = dateLong;
                    timeUpdateDb.startTime = startHour*100 + startMin;
                    timeUpdateDb.endTime = endHour*100 + endMin;
                    boolean isValid = true;
                    for(int i = 0 ; i < startArray.size() ; i++) {
                        if ((timeUpdateDb.startTime <= startArray.get(i) && startArray.get(i) <= timeUpdateDb.endTime) | (timeUpdateDb.startTime <= endArray.get(i) && endArray.get(i) <= timeUpdateDb.endTime) | (timeUpdateDb.startTime >= startArray.get(i) && timeUpdateDb.endTime <= endArray.get(i))){
                            isValid = false;
                            break;
                        }
                    }
                    if(!isValid){
                        et_endTime.setText("시간이 중복되었습니다!\n시간을 재설정해주세요.");
                        et_endTime.setTextColor(Color.parseColor("#ff8682"));
                        et_endTime.setTextSize(15);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                et_endTime.setText(endHour + "시 " + endMin + "분");
                                et_endTime.setTextColor(Color.parseColor("#000000"));
                                et_endTime.setTextSize(18);
                            }
                        }, 2000);
                    }
                    if(isValid) {
                        database.child("daily").child(Long.toString(timeUpdateDb.date)).child(String.valueOf(timeline)).child(timeUpdateDb.createDate).removeValue();

                        database.child("daily").child(Long.toString(timeUpdateDb.date)).child("3").child(timeUpdateDb.createDate).removeValue();
                        database.child("daily").child(Long.toString(timeUpdateDb.date)).child("3").child(timeUpdateDb.createDate).setValue(timeUpdateDb);

                        dismiss();
                    }
                }
            }
        });

    }
}
