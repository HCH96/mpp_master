package org.tech.mobileprogrammingproject.Daily;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.tech.mobileprogrammingproject.FIREBASEDB.DailyDB;
import org.tech.mobileprogrammingproject.R;

import java.util.ArrayList;
import java.util.Calendar;

public class TodoAddedDialog extends DialogFragment implements View.OnClickListener {

    /*
    1. variables :
        bt_cancel : (등록) 취소 버튼
        bt_listUp : 할 일 추가 버튼
        et_todo : dialog에서 입력받은 할 일(EditText)

    2. functions:



*/

    private Button bt_cancel;
    private Button bt_listUp;
    private EditText et_todo;
    DatabaseReference database = null;
    private RadioGroup timeGroup;
    private LinearLayout todolist;
    Spinner spinner;
    DailyDB dailydb = null;
    Calendar cal;
    EditText content;

    public static final String TAG_EVENT_DIALOG = "dialog_event";
    public static TodoAddedDialog getInstance() {
        TodoAddedDialog e = new TodoAddedDialog();
        return e;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.todo_popup, container, false);
        spinner = (Spinner) v.findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> adapterArray = ArrayAdapter.createFromResource(v.getContext(), R.array.category_list, android.R.layout.simple_spinner_item);
        adapterArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterArray);
        cal = Calendar.getInstance();
        content = v.findViewById(R.id.add_todo);
        database = FirebaseDatabase.getInstance().getReference();

        bt_cancel = v.findViewById(R.id.bt_cancel);
        bt_listUp = v.findViewById(R.id.bt_listUp);
        et_todo = v.findViewById(R.id.add_todo);

        todolist = v.findViewById(R.id.todolist);
        timeGroup = v.findViewById(R.id.time_group);
        bt_cancel.setOnClickListener(this);
        bt_listUp.setOnClickListener(this);
        setCancelable(false);
        return v;
    }


    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.bt_listUp: //확인 버튼을 눌렀을 때
                if (et_todo.getText().toString().equals("")) {
                    Toast.makeText(v.getContext(), "할 일이 입력되지 않았습니다.", Toast.LENGTH_LONG).show();
                    break;
                }
                if (timeGroup.getCheckedRadioButtonId() == R.id.time1) {
                    dailydb = new DailyDB();
                    dailydb.content = content.getText().toString();
                    dailydb.state = 0;
                    dailydb.catalog = spinner.getSelectedItem().toString();
                    dailydb.date = getArguments().getInt("year") * 1000 + getArguments().getInt("month") * 100 + getArguments().getInt("day");
                    database.child("daily").child(Long.toString(dailydb.date)).child("0").child(cal.getTime().toString()).setValue(dailydb);

                    dismiss();
                    break;
                }
                else if(timeGroup.getCheckedRadioButtonId() == R.id.time2){
                    dailydb = new DailyDB();
                    dailydb.content = content.getText().toString();
                    dailydb.state = 0;
                    dailydb.catalog = spinner.getSelectedItem().toString();
                    dailydb.date = getArguments().getInt("year") * 1000 + getArguments().getInt("month") * 100 + getArguments().getInt("day");
                    database.child("daily").child(Long.toString(dailydb.date)).child("1").child(cal.getTime().toString()).setValue(dailydb);

                    dismiss();
                    break;
                } else if (timeGroup.getCheckedRadioButtonId() == R.id.time3){
                    dailydb = new DailyDB();
                    dailydb.content = content.getText().toString();
                    dailydb.state = 0;
                    dailydb.catalog = spinner.getSelectedItem().toString();
                    dailydb.date = getArguments().getInt("year") * 1000 + getArguments().getInt("month") * 100 + getArguments().getInt("day");
                    database.child("daily").child(Long.toString(dailydb.date)).child("2").child(cal.getTime().toString()).setValue(dailydb);
                    dismiss();
                    break;
                }

            case R.id.bt_cancel: //취소 버튼을 눌렀을 때
                dismiss();
                break;
        }
    }
}
