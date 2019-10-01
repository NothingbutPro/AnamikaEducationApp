package com.anamika;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import adapter.TimetableFragmentAdapter;

/**
 * Created by Rajesh Dabhi on 20/6/2017.
 */

public class TimetableActivity extends CommonAppCompatActivity {

    private ListView lv_timetable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.color_31));
        }

        //lv_timetable = (ListView)findViewById(R.id.lv_timetable);
        //loadTimetableData("1");

        ViewPager vp_timetable = (ViewPager) findViewById(R.id.vp_timetable);
        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_day);

        vp_timetable.setAdapter(new TimetableFragmentAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(vp_timetable);

        /*tabLayout.addTab(tabLayout.newTab().setText("MON"));
        tabLayout.addTab(tabLayout.newTab().setText("TUE"));
        tabLayout.addTab(tabLayout.newTab().setText("WED"));
        tabLayout.addTab(tabLayout.newTab().setText("THU"));
        tabLayout.addTab(tabLayout.newTab().setText("FRI"));
        tabLayout.addTab(tabLayout.newTab().setText("SAT"));
        tabLayout.addTab(tabLayout.newTab().setText("SUN"));*/

        tabLayout.getTabAt(0).setText("MON");
        tabLayout.getTabAt(1).setText("TUE");
        tabLayout.getTabAt(2).setText("WED");
        tabLayout.getTabAt(3).setText("THU");
        tabLayout.getTabAt(4).setText("FRI");
        tabLayout.getTabAt(5).setText("SAT");
        tabLayout.getTabAt(6).setText("SUN");


        /*tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tabLayout.getSelectedTabPosition();
                position++;
                loadTimetableData(""+position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });*/

    }

    /*private void loadTimetableData(String day_id){
        TimetableAdapter adapter = new TimetableAdapter(this, new JSONArray(),day_id);
        lv_timetable.setAdapter(adapter);
    }*/

}