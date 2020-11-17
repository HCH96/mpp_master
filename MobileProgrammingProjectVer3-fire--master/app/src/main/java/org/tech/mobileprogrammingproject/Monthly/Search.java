package org.tech.mobileprogrammingproject.Monthly;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.tech.mobileprogrammingproject.Daily.MainActivity;
import org.tech.mobileprogrammingproject.FIREBASEDB.DailyDB;
import org.tech.mobileprogrammingproject.FIREBASEDB.MonthlyDB;
import org.tech.mobileprogrammingproject.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    private List<String> list; //데이터를 넣은 리스트변수(daily)
    private List<String> list2; //데이터를 넣은 리스트변수(monthly)
    private ListView listView; //검색결과를 보여줄 리스트변수(daily)
    private ListView listView2; //검색결과를 보여줄 리스트변수(monthly)
    private EditText editSearch; // 검색어를 입력할 input창
    private SearchAdapter adapter; // 리스트뷰에 연결할 어댑터 (daily)
    private SearchAdapter adapter2; // 리스트뷰에 연결할 어댑터 (monthly)
    private ArrayList<String> arraylist; //daily 데이터 저장
    private ArrayList<String> arraylist2; //monthly 데이터 저장

    //daily database reference
    private FirebaseDatabase mdata = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mdata.getReference("daily");

    //monthly database refreence
    private FirebaseDatabase mdata2 = FirebaseDatabase.getInstance();
    private DatabaseReference mRef2 = mdata2.getReference("monthly");

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);

        editSearch = (EditText) findViewById(R.id.editSearch);
        listView = (ListView) findViewById(R.id.listView);
        listView2 = (ListView) findViewById(R.id.listView2);
        arraylist = new ArrayList<String>();
        arraylist2 = new ArrayList<String>();

        //리스트 생성
        list = new ArrayList<String>();
        list2 = new ArrayList<String>();

        //리스트에 연동될 어댑터를 생성한다.
        adapter = new SearchAdapter(list, this);
        adapter2 = new SearchAdapter(list2, this);

        //리스트뷰에 어댑터를 연결한다
        listView.setAdapter(adapter);
        listView2.setAdapter(adapter2);

        //daily 데이터 가져오기
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arraylist.clear();
                for (DataSnapshot Snapshot : snapshot.getChildren()) {
                    for(DataSnapshot Snapshot2 : Snapshot.getChildren()){
                        for(DataSnapshot Snapshot3 : Snapshot2.getChildren()){
                            //daily database를 돌면서 원하는 데이터를 가공하여 string info에 저장한다.
                            DailyDB get = Snapshot3.getValue(DailyDB.class);
                            String[] info = {get.catalog, get.content, String.valueOf(get.date)};
                            info[2] = info[2].substring(3,7);
                            info[2] = insert(info[2], 2,"월   ");
                            info[2] = insert(info[2], 8,"일 ");
                            //문자열들을 합쳐 데이터를 저장해준다.
                            String result ="   " + setTextLength(info[0],20) +setTextLength(info[2],25) +setTextLength(info[1],10);
                            arraylist.add(result);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //monthly 데이터 가저오기
        mRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arraylist2.clear();
                for (DataSnapshot mSnapshot : snapshot.getChildren()) {
                    //monthly database를 돌면서 원하는 데이터를 가공하여 string minfo에 저장한다.
                    MonthlyDB get = mSnapshot.getValue(MonthlyDB.class);
                    DateFormat start = new SimpleDateFormat("MM월   dd일");
                    String str_start = start.format(get.startPoint);
                    DateFormat end = new SimpleDateFormat("MM월   dd일");
                    String str_end = end.format(get.endPoint);
                    String[] minfo = {get.content, str_start, str_end};
                    //문자열들을 합쳐 출력할 데이터를 저장해준다.
                    String result = "   " +setTextLength(minfo[1], 1) + "    ~    " + setTextLength(minfo[2], 30) + " " + setTextLength(minfo[0], 10);
                    arraylist2.add(result);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //input창에 문자를 입력할 때마다 호출되고 search메소드를 호출한다.
                String text = editSearch.getText().toString();
                search(text); // input에 입력된 값을 daily database에 검색한다.
                search2(text); //input에 입력된 값을 monthly database에 검색한다.
            }
        });

    }

    //문자열의 공백을 맞춰주는 메소드 (데이터를 합칠 때 공백을 맞추기 위해서 사용)
    private String setTextLength(String text, int length) {
        if(text.length()<length){
            int gap = length - text.length();
            for (int i=0; i<gap; i++){
                text = text + " ";
            }
        }
        return text;
    }

    //검색을 수행하는 메소드(daily)
    private void search(String charText) {

        //문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        //문자 입력이 없을 때는 아무것도 보여주지 않음
        if(charText.length()==0){
        }
        //문자를 입력했을 때
        else {
            //리스트의 모든 데이터를 검색한다.
            for(int i =0; i<arraylist.size(); i++){
                //arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 리턴한다.
                if(arraylist.get(i).toLowerCase().contains(charText)){
                    //검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
            }
        }
        //리스트 데이터가 변경되었으므로 어댑터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

    //검색을 수행하는 메소드(monthly)
    private void search2(String charText) {

        //문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list2.clear();

        //문자 입력이 없을때는 모든 데이터를 보여준다.
        if(charText.length()==0){
        //    list2.addAll(arraylist2);
        }
        //문자를 입력했을 때
        else {
            //리스트의 모든 데이터를 검색한다.
            for(int i =0; i<arraylist2.size(); i++){
                //arraylist2의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 리턴한다.
                if(arraylist2.get(i).toLowerCase().contains(charText)){
                    //검색된 데이터를 리스트에 추가한다.
                    list2.add(arraylist2.get(i));
                }
            }
        }
        //리스트 데이터가 변경되었으므로 어댑터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter2.notifyDataSetChanged();
    }


    //문자열사이에 문자를 추가하는 메소드(데이터를 가공할 때 문자 사이에 다른 문자를 추가하기 위해 사용)
    public static String insert(String strTarget, int loc, String strInsert){
        if ( strTarget == null ) {
            return strInsert;
        }

        if ( strInsert == null ) {
            return strTarget;
        }

        String result = null;

        try {
            StringBuffer strBuf = new StringBuffer();
            int lengthSize = strTarget.length();
            if (loc >= 0) {
                if(lengthSize < loc) {
                    loc = lengthSize;
                }
                strBuf.append(strTarget.substring(0, loc));
                strBuf.append(strInsert);
                strBuf.append(strTarget.substring(loc));
            } else {
                if (lengthSize < Math.abs(loc) ){
                    loc = lengthSize * (-1);
                }
                strBuf.append(strTarget.substring(0, (lengthSize - 1) + loc));
                strBuf.append(strInsert);
                strBuf.append(strTarget.substring((lengthSize - 1) + loc + strInsert.length()));
            }
            result = strBuf.toString();
        }catch( Exception e ) {
            System.out.println(e.getMessage());
            result = "error";
        }
        return result;
    }

    //메인으로 가기
    public void goToMain(View v){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}