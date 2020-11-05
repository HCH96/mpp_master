package org.tech.mobileprogrammingproject.Daily;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.tech.mobileprogrammingproject.FIREBASEDB.DailyDB;
import org.tech.mobileprogrammingproject.R;

import java.util.Calendar;

public class Status extends DialogFragment implements View.OnClickListener {

    private EditText et_startTime;
    private EditText et_endTime;
    private CheckBox cb_none;
    private Button bt_cancelDone;
    private Button bt_done;
    private Calendar currentTime;

    DatabaseReference database = null;
    DailyDB dailydb = null;

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
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Start Time");
                mTimePicker.show();
            }
        });

        et_endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        et_endTime.setText(selectedHour + "시 " + selectedMinute + "분");
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("End Time");
                mTimePicker.show();
            }
        });

        bt_cancelDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "취소되었습니다.", Toast.LENGTH_LONG).show();
                dismiss();
            }
        });

        bt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* TODO: 2020-11-04
                 *   1. firebase 연결하기 - 시간정보 전달
                 *   2. 리스트 하단에 붙이기 (완료 표시를 위해 음영표시 혹은 모든 content 색상 회색으로 변경)
                 * */
                dismiss();
            }
        });

    }
}
