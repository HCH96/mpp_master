package org.tech.mobileprogrammingproject.Monthly;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;

import org.tech.mobileprogrammingproject.Daily.MainActivity;
import org.tech.mobileprogrammingproject.R;

import java.util.ArrayList;

public class Stats extends AppCompatActivity {

    PieChart pieChart;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats);

        pieChart = (PieChart)findViewById(R.id.piechart);

        // pie chart 기본값 설정
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        /*
        TODO: 통계 값 넣어주기
            yValues.add(new PieEntry(value, label)) 의 형식으로 값 추가
            value : 통계값
            label : firstPage Spinner에서 지정한 Category

         */

        /*
        TODO: 월 별 통계 혹은 기타 제공할 통계 형식 정해서 상황에 맞게 서술해주기
            Description description = new Description();
            description.setText(); : label 지정
            description.setTextSize(); : Font Size 지정
            pieChart.setDescription(description);
         */

        /*
        TODO: 그래프 모양 설정
            (참고)
            PieDataSet dataSet = new PieDataSet(value, lable);
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);
            dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
         */

        /*
        TODO: PieChart에 표시되는 value 값 형태 지정
            (예)
            PieData data = new PieData((dataSet));
            data.setValueTextSize(10f);
            data.setValueTextColor(Color.YELLOW);
        */

//        pieChart.setData(data);

    }

    public void goToMain(View v){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}