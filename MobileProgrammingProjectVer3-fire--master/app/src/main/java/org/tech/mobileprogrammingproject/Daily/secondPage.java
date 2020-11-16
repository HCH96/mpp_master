package org.tech.mobileprogrammingproject.Daily;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.tech.mobileprogrammingproject.FIREBASEDB.DailyDB;
import org.tech.mobileprogrammingproject.R;
import static org.tech.mobileprogrammingproject.Daily.firstPage.dateTime;
import java.util.ArrayList;
import java.util.Calendar;


public class secondPage extends Fragment {

    // null Action에 대해서 같은 색으로 처리하기 위해서
    public static ArrayList<Integer> nullActionIdx = new ArrayList<>();

    // 몇개의 할일 덩어리가 있는지 파악하기 위해서
    public int idx;
    static DatabaseReference database = null;
    public static PieChart pieChart;
    Calendar cal = Calendar.getInstance();
    ViewGroup rootView;
    static ArrayList<String> timetable = new ArrayList<>(144);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        rootView = (ViewGroup) inflater.inflate(R.layout.secondpage, container, false);
        nullActionIdx.clear();
        pieChart = rootView.findViewById(R.id.picChart);
        /*
            2020.11.06 황교민
            null Action에 대해서 같은 색상으로 표시하기 위해서 nullActionIdx를 받아옴.
            nullActionIdx에 해당하는 곳에는 하나의 color를 할당하고, 나머지는 미리 생성한 colorForAct에서 순차적으로 color을 받아옴.
            next Thing :
            1. 시간이 만약에 잘못 입력되었으면 어떻게 할 것인지(아마도 firstpage에서 처리할 듯 하다.)
            2. 만약에 표시할 수 없을 정도로 작은 할일은 어떻게 처리할 것인가(즉 10분 미만의 할일)
             => 아마도 layout을 하나 더 만들어서 동적으로 할일을 표시하면 가능할 듯 하다.
         */
        return rootView;
    }
    public static void changeState(String date) {
        timetable.clear();
        for (int i = 0; i < 144; i++) {
            timetable.add(" ");
        }
        FirebaseDatabase mdata = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mdata.getReference("daily/" + date + "/3");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Snapshot : snapshot.getChildren()) {
                    DailyDB get = Snapshot.getValue(DailyDB.class);

                    int starttime = ((get.startTime / 100) * 60 + (get.startTime - (get.startTime / 100) * 100)) / 10;
                    int endtime = ((get.endTime / 100) * 60 + (get.endTime - (get.endTime / 100) * 100)) / 10;
                    String content = get.content;

                    for (int i = starttime; i < endtime; i++) {
                        timetable.set(i, content);
                    }

                }
                ArrayList<PieEntry> piedata = new ArrayList<>();
                float piesize = 0f;
                String precontent = timetable.get(0);
                int idxSub = 0;
                nullActionIdx.clear();
                if (precontent.equals(" ")) nullActionIdx.add(idxSub);
                // 처음에 덩어리를 생성하여 오류가 계속 발생하였습니다.
                //piedata.add(new PieEntry(piesize, precontent));
                for (int i = 0; i < timetable.size(); i++) {
                    if (precontent != timetable.get(i)) {
                        piedata.add(new PieEntry(piesize, precontent));
                        piesize = 0f;
                        precontent = timetable.get(i);
                        idxSub++;
                        if (precontent.equals(" ")) nullActionIdx.add(idxSub);
                    } else {
                        piesize = piesize + 1f;
                        precontent = timetable.get(i);
                    }
                }
                piedata.add(new PieEntry(piesize, precontent));
                PieDataSet pieDataSet = new PieDataSet(piedata, "오늘 한 일");
                pieDataSet.setSliceSpace(3f);
                pieDataSet.setSelectionShift(5f);

                ArrayList<Integer> colors = new ArrayList<Integer>();
                ArrayList<Integer> colorForAct = new ArrayList<>();
                for (int c : ColorTemplate.LIBERTY_COLORS)
                    colorForAct.add(c);
                int colorIdx = 0;
                for (int i = 0; i < idxSub + 1; i++) {
                    if (nullActionIdx.contains(i)) {
                        colors.add(Color.parseColor("#f6f6f6"));
                    } else {
                        colors.add(colorForAct.get(colorIdx++));
                    }
                }

                pieDataSet.setColors(colors);

                PieData pieData = new PieData(pieDataSet);
                pieData.setValueTextSize(0);
                pieChart.clear();
                //pieChart = findViewById(R.id.picChart);
                pieChart.setEntryLabelColor(Color.BLACK);
                pieChart.setDrawEntryLabels(true);
                pieChart.setRotationEnabled(false);
                pieChart.setUsePercentValues(false);
                pieChart.setCenterTextSize(25);
                pieChart.setHoleRadius(30);
                pieChart.setData(pieData);

                /*
                2020.11.14 김지원
                pieChart UI 수정
                */
                pieChart.setDrawHoleEnabled(false); // 차트 가운데 hole 제거
                pieChart.getLegend().setEnabled(false); // 범례 안 보이도록 설정
                pieDataSet.setSliceSpace(0.0f); // 그래프 조각 사이 여백 설정
                pieChart.getDescription().setEnabled(false); // 설명 레이블 제거


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

/*
    private ArrayList<PieEntry> setpiedata(){
        timetable.clear();
        for(int i =0; i<144; i++){
            timetable.add(" ");
        }
        FirebaseDatabase mdata = FirebaseDatabase.getInstance();
        DatabaseReference mRef = mdata.getReference("daily/"+dateTime+"/3");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot Snapshot : snapshot.getChildren()){
                    DailyDB get = Snapshot.getValue(DailyDB.class);
                    int starttime = ((get.startTime/100)*60 +(get.startTime-(get.startTime/100)*100))/10;
                    int endtime = ((get.endTime/100)*60 + (get.endTime-(get.endTime/100)*100))/10;
                    String content = get.content;
                    for(int i =starttime; i<endtime; i++){
                        timetable.set(i,content);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        ArrayList<PieEntry> piedata = new ArrayList<>();
        float piesize = 0f;
        String precontent = timetable.get(0);
        idx = 0;
        if(precontent.equals(" ")) nullActionIdx.add(idx);
        // 처음에 덩어리를 생성하여 오류가 계속 발생하였습니다.
        //piedata.add(new PieEntry(piesize, precontent));
        for(int i =0; i<timetable.size(); i++){
            if(precontent != timetable.get(i)){
                piedata.add(new PieEntry(piesize, precontent));
                piesize =0f;
                precontent = timetable.get(i);
                idx++;
                if(precontent.equals(" ")) nullActionIdx.add(idx);
            }else{
                piesize = piesize+1f;
                precontent = timetable.get(i);
            }
        }
        piedata.add(new PieEntry(piesize,precontent));
        return piedata;
    }
 */
}
