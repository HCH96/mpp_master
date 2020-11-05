package org.tech.mobileprogrammingproject.Daily;
import static org.tech.mobileprogrammingproject.Daily.secondPage.dateTime;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.tech.mobileprogrammingproject.FIREBASEDB.MemoDB;
import org.tech.mobileprogrammingproject.R;

import java.text.SimpleDateFormat;

public class thirdPage extends Fragment{
    /*
    황교민 2020.09.22
    1. variables :
        contents : 사용자 메모 내용
        memo : 제목
        saveButton : 내용 저장 버튼
        clearButton : 내용 초기화 버튼
    2. functions:
        clearButton.setOnClickListener() : contents 내용을 초기화함.
        saveButton.setOnClickListener() : contents 내용을 화면에 Toast함 => 아직 DB를 구현하지 않았기 때문에 Toast로 구현함.

    * 메모 기능 추가

    황교민 2020.09.24
    * SQLite로 DB를 구현하였음. 메모장의 내용을 저장하도록 DB에 연결함.
    
    황교민 2020.10.24
    1. firebase db변경함.
     */

    static DatabaseReference database = null;
    MemoDB memodb = null;
    static EditText contents;
    TextView memo;
    Button saveButton, clearButton;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.thirdpage, container, false);

        memo = rootView.findViewById(R.id.title);
        memo.setPaintFlags(memo.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        contents = rootView.findViewById(R.id.contents);

        saveButton = rootView.findViewById(R.id.saveButton);
        clearButton = rootView.findViewById(R.id.clearButton);

        clearButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                contents.setText("");
            }
        });

        database = FirebaseDatabase.getInstance().getReference();

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                memodb = new MemoDB(dateTime, contents.getText().toString());
                database.child("memo").child(dateTime).setValue(memodb);
            }
        });
        changeMemo(dateTime);
        return rootView;
    }
    /*
        2020.11.01 황교민
        메모 내용을 바꾸는 메소드 생성.
     */
    public static void changeMemo(final String currDate){
        database.child("memo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isIn = false;
                for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                    MemoDB currmemoDB = childSnapshot.getValue(MemoDB.class);
                    if(currmemoDB.createdDate.equals(currDate)){
                        contents.setText(currmemoDB.memo);
                        isIn = true;
                        break;
                    }
                }
                if (!isIn){
                    contents.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
