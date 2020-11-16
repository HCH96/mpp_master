package org.tech.mobileprogrammingproject.Daily;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import static org.tech.mobileprogrammingproject.Daily.firstPage.dateTime;

import org.tech.mobileprogrammingproject.Monthly.Calender;
import org.tech.mobileprogrammingproject.Monthly.User;
import org.tech.mobileprogrammingproject.R;
import org.tech.mobileprogrammingproject.Monthly.Search;
import org.tech.mobileprogrammingproject.Monthly.Stats;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator3;
/*
    황교민 2020.09.15
    1. 원형 indicator, swipe기능 구현.
    2. 세개 페이지를 Fragment(first page, second page, third page)로 생성함.
    3. viewpager이 버전이 업데이트 되었음.

    황교민 2020.09.28
    1. navigation drawer을 fragment로 구현함.

    황교민 2020.10.05
    1. navigation drawer을 intent로 구현. -> fragment로 구현하면 어려움이 있기 때문에 intent로 변경함.

 */
public class MainActivity extends FragmentActivity{
    private static final int NUM_PAGES = 3;
    private ViewPager2 viewPager;
    private ScreenSlidePagerAdapter pagerAdapter;
    CircleIndicator3 indicator;

    firstPage firstpage = new firstPage();
    secondPage secondpage = new secondPage();
    thirdPage thirdpage = new thirdPage();

    DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.drawer_layout);
        viewPager = findViewById(R.id.pager);


        pagerAdapter = new ScreenSlidePagerAdapter(this);
        pagerAdapter.addItem(firstpage);
        pagerAdapter.addItem(secondpage);
        pagerAdapter.addItem(thirdpage);

        indicator = (CircleIndicator3) findViewById(R.id.indicator);
        viewPager.setAdapter(pagerAdapter);
        indicator.setViewPager(viewPager);

        viewPager.setOffscreenPageLimit(3);
    }
    // drawer을 통해서 월별 할일 페이지로 이동
    public void goToCalander(View v){
        Intent intent = new Intent(getApplicationContext(), Calender.class);
        startActivity(intent);
    }
    // drawer을 통해서 검색 페이지로 이동
    public void goToSearch(View v){
        Intent intent = new Intent(getApplicationContext(), Search.class);
        startActivity(intent);
    }
    // drawer을 통해서 통계페이지로 이동
    public void goToStatcs(View v){
        Intent intent = new Intent(getApplicationContext(), Stats.class);
        intent.putExtra("month",dateTime.substring(3,5));
        startActivity(intent);
    }


    @Override
    public void onBackPressed(){
        if (viewPager.getCurrentItem() == 0){
            super.onBackPressed();
        }else{
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter{
        public ArrayList<Fragment> items = new ArrayList<Fragment>();
        public ScreenSlidePagerAdapter(FragmentActivity fa){
            super(fa);
        }

        public void addItem(Fragment item){
            items.add(item);
        }

        @Override
        public Fragment createFragment(int position){
            return items.get(position);
        }

        @Override
        public int getItemCount(){
            return items.size();
        }
    }
}