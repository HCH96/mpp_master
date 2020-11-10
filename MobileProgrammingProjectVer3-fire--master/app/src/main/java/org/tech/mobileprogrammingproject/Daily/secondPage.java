package org.tech.mobileprogrammingproject.Daily;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.tech.mobileprogrammingproject.FIREBASEDB.DailyDB;
import org.tech.mobileprogrammingproject.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


//임시 데이터 12시 34분 >> 1234 (24시간제)
class Data {
    public String content;
    public int startTime;
    public int endTime;

    public Data(){
    }

    public Data(String content, int startTime, int endTime){
        this.content = content;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}


public class secondPage extends Fragment {
    public static String dateTime = "";
    // null Action에 대해서 같은 색으로 처리하기 위해서
    public ArrayList<Integer> nullActionIdx = new ArrayList<>();
    // 몇개의 할일 덩어리가 있는지 파악하기 위해서
    public int idx;

    Data data1 = new Data("공부", 1414 , 1630 );
    Data data2 = new Data("TV", 1739, 1830);
    Data data3 = new Data("Youtube", 1900, 2200);
    Data data4 = new Data("잠자기", 10,1000);

    ArrayList<String> timetable = new ArrayList<>(144);


    private void setting(Data data) {

        int starttime = ((data.startTime/100)*60 +(data.startTime-(data.startTime/100)*100))/10;
        int endtime = ((data.endTime/100)*60 + (data.endTime-(data.endTime/100)*100))/10;
        String content = data.content;

        for(int i =starttime; i<endtime; i++){
            timetable.set(i,content);
        }
    }


    private ArrayList<PieEntry> setpiedata(){

        for(int i =0; i<144; i++){
            timetable.add(" ");
        }
        setting(data1);
        setting(data2);
        setting(data3);
        setting(data4);

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        nullActionIdx.clear();
        return (ViewGroup) inflater.inflate(R.layout.secondpage, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        PieDataSet pieDataSet = new PieDataSet(setpiedata(), "오늘 한 일");
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        /*
            2020.11.06 황교민
            null Action에 대해서 같은 색상으로 표시하기 위해서 nullActionIdx를 받아옴.
            nullActionIdx에 해당하는 곳에는 하나의 color를 할당하고, 나머지는 미리 생성한 colorForAct에서 순차적으로 color을 받아옴.
            next Thing :
            1. 시간이 만약에 잘못 입력되었으면 어떻게 할 것인지(아마도 firstpage에서 처리할 듯 하다.)
            2. 만약에 표시할 수 없을 정도로 작은 할일은 어떻게 처리할 것인가(즉 10분 미만의 할일)
             => 아마도 layout을 하나 더 만들어서 동적으로 할일을 표시하면 가능할 듯 하다.
         */
        ArrayList<Integer> colorForAct = new ArrayList<>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colorForAct.add(c);
        int colorIdx = 0;
        for(int i = 0 ; i < idx + 1 ; i++){
            if(nullActionIdx.contains(i)) {
                colors.add(ColorTemplate.JOYFUL_COLORS[0]);
            }
            else {
                colors.add(colorForAct.get(colorIdx++));
            }
        }
        /*
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        */
        pieDataSet.setColors(colors);

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(0);

        PieChart pieChart;
        pieChart = getView().findViewById(R.id.picChart);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setDrawEntryLabels(true);
        pieChart.setRotationEnabled(false);
        pieChart.setUsePercentValues(false);
        pieChart.setCenterTextSize(25);
        pieChart.setHoleRadius(30);
        pieChart.setData(pieData);

        Description description = new Description();
        description.setText("오늘 한 일"); //라벨
        description.setTextSize(15);
        pieChart.setDescription(description);
    }
}