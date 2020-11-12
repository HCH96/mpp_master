package org.tech.mobileprogrammingproject.Monthly;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import org.tech.mobileprogrammingproject.Daily.MainActivity;
import org.tech.mobileprogrammingproject.Daily.firstPage;
import org.tech.mobileprogrammingproject.FIREBASEDB.DailyDB;
import org.tech.mobileprogrammingproject.R;

import java.util.ArrayList;

// TODO: 2020-11-12 한 달 단위로 초기화(month값이 바뀔 경우)
public class Stats extends AppCompatActivity {

    PieChart pieChart; // 차트 생성
    int category0_sumTime; // category0의 시간합계를 저장할 변수
    int category1_sumTime; // category1의 시간합계를 저장할 변수
    int category2_sumTime; // category2의 시간합계를 저장할 변수
    int category3_sumTime; // category3의 시간합계를 저장할 변수

    // Firebase DatabaseReference
    DatabaseReference database = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats);

        // firebase 연결
        database = FirebaseDatabase.getInstance().getReference();

        // pieChart 생성
        pieChart = (PieChart) findViewById(R.id.piechart);

        /*
        통계 구현할 data 생성 (from Firebase)
        - 완료 카테고리(timeline == 3)에 데이터가 추가될 경우 startTime과 endTime을 받아옴
        - 이 때 시간 정보를 분으로 변환해서 저장
        */
        database.child("daily").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childData : snapshot.getChildren()) {
                    database.child("daily").child(childData.getKey()).child("3").addValueEventListener(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot2) {
                                    for (DataSnapshot timeData : snapshot2.getChildren()) {
                                        DailyDB doneDB = timeData.getValue(DailyDB.class);
                                        int startTime;
                                        int endTime;

                                        if((int)(Math.log10(doneDB.startTime)+1) == 2) startTime = ((doneDB.startTime / 10) * 60 + doneDB.startTime - (doneDB.startTime / 10));
                                        else startTime = ((doneDB.startTime / 100) * 60 + doneDB.startTime - (doneDB.startTime / 100));

                                        if((int)(Math.log10(doneDB.endTime)+1) == 2) endTime = ((doneDB.endTime / 10) * 60 + doneDB.endTime - (doneDB.endTime / 10));
                                        else endTime = ((doneDB.endTime / 100) * 60 + doneDB.endTime - (doneDB.endTime / 100));

                                        // category 별로 분류하여 합계 시간을 누적하여 저장해줌
                                        if (doneDB.catalog.equals("미정")) category0_sumTime += endTime - startTime;
                                        else if (doneDB.catalog.equals("공부")) category1_sumTime += endTime - startTime;
                                        else if (doneDB.catalog.equals("과제")) category2_sumTime += endTime - startTime;
                                        else if (doneDB.catalog.equals("운동")) category3_sumTime += endTime - startTime;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // pie chart 기본값 설정
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);


        /*
        통계 값 넣어주기
        value: category별 수행 시간
        label: category
        */
        ArrayList<PieEntry> yValues = new ArrayList<>();
        yValues.add(new PieEntry(category0_sumTime, "미정"));
        yValues.add(new PieEntry(category1_sumTime, "공부"));
        yValues.add(new PieEntry(category2_sumTime, "과제"));
        yValues.add(new PieEntry(category3_sumTime, "운동"));

        // pieChart 설명
        Description description = new Description();
        description.setText("월 별 통계"); // label 지정
        description.setTextSize(18);
        pieChart.setDescription(description);

        // 그래프 세팅
        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        //  PieChart에 표시되는 value 값 형태 지정
        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);
        pieChart.setData(data);
    }

    // MainActivity와 연결
    public void goToMain(View v) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }


}