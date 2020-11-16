package org.tech.mobileprogrammingproject.Daily;
import static org.tech.mobileprogrammingproject.Daily.firstPage.dateTime;

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

    static DatabaseReference database = null; // firebase db연결을 위함.
    MemoDB memodb = null;
    static EditText contents;
    Button saveButton, clearButton;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.thirdpage, container, false);

        contents = rootView.findViewById(R.id.contents); // 메모 내용을 표시하는 곳

        saveButton = rootView.findViewById(R.id.saveButton); // 저장버튼
        clearButton = rootView.findViewById(R.id.clearButton); // 초기화 버튼

        clearButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                contents.setText("");
            }
        }); // 초기화 버튼 클릭시 동작 내용(기존의 입력된 내용을 삭제)

        database = FirebaseDatabase.getInstance().getReference(); // firebasedb연결

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                memodb = new MemoDB(dateTime, contents.getText().toString());
                database.child("memo").child(dateTime).setValue(memodb);
            }
        });// 저장버튼 클릭시 메모 내용 저장(db내)
        changeMemo(dateTime); // 저장된 db내용을 화면에 보여주는 메소드
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
                boolean isIn = false;// 해당 날짜의 메모가 있는지 확인하는 변수
                for(DataSnapshot childSnapshot : snapshot.getChildren()) {
                    MemoDB currmemoDB = childSnapshot.getValue(MemoDB.class);
                    if(currmemoDB.createdDate.equals(currDate)){
                        contents.setText(currmemoDB.memo); // 만약에 해당 날짜의 내용이 있으면 보여주는 기능
                        isIn = true;
                        break;
                    }
                }
                if (!isIn){
                    contents.setText(""); // 만약에 해당 날짜의 내용이 없으면 "" 으로 화면에 표시
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
