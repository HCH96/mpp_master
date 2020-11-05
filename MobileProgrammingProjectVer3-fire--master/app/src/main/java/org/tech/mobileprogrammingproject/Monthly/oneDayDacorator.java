package org.tech.mobileprogrammingproject.Monthly;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Date;
/*
  2020.10.27 황교민
  1. 오늘 날짜를 초록색으로 표시함.
 */
public class oneDayDacorator implements DayViewDecorator {
    private CalendarDay date;
    public oneDayDacorator(){
        date = CalendarDay.today();
    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(1.4f));
        view.addSpan(new ForegroundColorSpan(Color.GREEN));
    }

    public void setDate(Date date){
        this.date = CalendarDay.from(date);
    }
}

