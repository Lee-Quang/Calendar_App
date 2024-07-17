package com.example.btl_ltdt_2;

import static com.example.btl_ltdt_2.CalendarUtils.daysInWeekArray;
import static com.example.btl_ltdt_2.CalendarUtils.formattedWeek;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.btl_ltdt_2.SQL.SQLiteManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class WeeklyViewActivity extends AppCompatActivity implements WeeklyApdater.OnItemlistener {
    private BottomNavigationView bottomNavigationView;
    private TextView Weeklytx;
    private RecyclerView caledarRecyclerView;
    private ListView eventListView;
    private Button btnPre, btnNext;
    private SQLiteManager sqLiteManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weekly_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initWidget();
        bottomNavigation();
        setWeekView();
        onResume();
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int i = position;
                Toast.makeText(getApplicationContext(), "OK" + i, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(WeeklyViewActivity.this, EventView.class);
                intent.putExtra("Position",i);
                 startActivity(intent);
            }
        });
    }


    private void initWidget() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        caledarRecyclerView = findViewById(R.id.calendarRecyclerView);
        Weeklytx = findViewById(R.id.Weeklytx);
        btnNext = findViewById(R.id.btnNext);
        btnPre = findViewById(R.id.btnPre);
        eventListView = findViewById(R.id.eventListView);
    }

    // Bottom Navigation

    private void bottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.managerTab);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID = item.getItemId();
                if (itemID == R.id.calendarTab) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    //  overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return true;
                }
                if (itemID == R.id.managerTab) {
                    return true;
                }
                if (itemID == R.id.settingTab) {
                    Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                    startActivity(intent);
                    //overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return true;
                }
                return true;
            }
        });
    }

    private void setWeekView() {
        Weeklytx.setText(formattedWeek(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);
        WeeklyApdater weeklyApdater = new WeeklyApdater(days, WeeklyViewActivity.this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        caledarRecyclerView.setLayoutManager(layoutManager);
        caledarRecyclerView.setAdapter(weeklyApdater);
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
        onResume();
    }

    public void PrevWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
        onResume();
    }


    public void NextWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
        onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        seeEvent();
    }

    private void seeEvent() {

      //  ArrayList<Event> dailyEvents = EventDataBase.getInstance(this).eventDAO().getListEvent();
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        // ArrayList<Event> dailyEvents = Event.eventsList;
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }


    public void addAction(View view) {
        Intent intent = new Intent(WeeklyViewActivity.this, EventEditView.class);
        startActivity(intent);
    }

}