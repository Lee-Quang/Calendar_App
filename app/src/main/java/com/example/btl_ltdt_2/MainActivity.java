package com.example.btl_ltdt_2;

import static com.example.btl_ltdt_2.CalendarUtils.MonthFromDate;
import static com.example.btl_ltdt_2.CalendarUtils.daysInMonthArray;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CalendarApdater.OnItemlistener{
    private BottomNavigationView bottomNavigationView;
    private TextView Monthtx;
    private RecyclerView caledarRecyclerView;
    private Button btnPre,btnNext;
    private FloatingActionButton floatingActionButton;
    private SQLiteManager sqLiteManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        CalendarUtils.selectedDate = LocalDate.now();
        initWidget();
        loadDbToMeme();
        bottomNavigation();
        setMonthView();
        onResume();
    }

    private void loadDbToMeme() {
        sqLiteManager = new SQLiteManager(this);
        int Max = sqLiteManager.IDEventMax();
        if(Max > 0)
        {
            IdEvent.ID = Max+1;
        }
        if(Event.eventsList.isEmpty())
        { sqLiteManager.dataEvent();}
    }


    private void initWidget() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        caledarRecyclerView = findViewById(R.id.CalendarRecyclerView);
        Monthtx = findViewById(R.id.Monthtx);
        btnPre = findViewById(R.id.btnPre);
        btnNext = findViewById(R.id.btnNext);
    }

    // Bottom Navigation

    private void bottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.calendarTab);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID = item.getItemId();
                if(itemID == R.id.calendarTab)
                {
                    return  true;
                }
                if(itemID == R.id.managerTab)
                {   Intent intent = new Intent(MainActivity.this, WeeklyViewActivity.class);
                    startActivity(intent);
                   // overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return  true;
                }
                if(itemID == R.id.settingTab)
                {   Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                    startActivity(intent);
                   // overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                    return  true;
                }
                return  true;
            }
        });
    }
    private void setMonthView()
    {
        Monthtx.setText(MonthFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);
        CalendarApdater calendarApdater = new CalendarApdater(daysInMonth,MainActivity.this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),7);
        caledarRecyclerView.setLayoutManager(layoutManager);
        caledarRecyclerView.setAdapter(calendarApdater);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //dataEvent();
     setMonthView();

}

    @Override
    public void onItemClick(int position, LocalDate date ) {
        if (date != null)
        {
            CalendarUtils.selectedDate = date;
            Intent intent = new Intent(MainActivity.this, WeeklyViewActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemLong(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        Intent intent = new Intent(MainActivity.this, EventEditView.class);
        startActivity(intent);

    }


    public void PrevMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void NextMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }
    public void addAction(View view) {
        Intent intent = new Intent(MainActivity.this, EventEditView.class);
        startActivity(intent);
       // Toast.makeText(this,"OK",Toast.LENGTH_SHORT).show();
    }
}
