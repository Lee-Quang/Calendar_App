package com.example.btl_ltdt_2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_ltdt_2.SQL.SQLiteManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;

public class EventView extends AppCompatActivity {
    private TextView Name, ChiTiet, DateStart, DateEnd,TimeStart, TimeEnd, ColorBox,TimeReminder, Repeat,User;
    private LocalDate dateStart, dateEnd, date;
    private LocalTime timeStart, timeEnd,timeReminder;
    private int color;
    private Dialog dialog;
    private TextView DeleteOnly, DeleteAll, Cancel;
    private int ID;
    private int position;
    private int IDRepeat;
    private SQLiteManager sqLiteManager;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initWidget();
        seeView();
        DialogAction();
    }

    private void initWidget() {
        Name = findViewById(R.id.NameET);
        ChiTiet = findViewById(R.id.ChiTietET);
        DateStart = findViewById(R.id.btnDataPicker1);
        DateEnd = findViewById(R.id.btnDataPicker2);
        TimeStart = findViewById(R.id.btnTimePicker1);
        TimeEnd = findViewById(R.id.btnTimePicker2);
        ColorBox = findViewById(R.id.colorBox);
        dialog = new Dialog(EventView.this);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.color_box));
        dialog.setCancelable(false);
        DeleteOnly = dialog.findViewById(R.id.DeleteOnly);
        DeleteAll = dialog.findViewById(R.id.DeleteAll);
        Cancel = dialog.findViewById(R.id.Cancel);
        TimeReminder = findViewById(R.id.remindertx);
        Repeat = findViewById(R.id.Repeat);
        User = findViewById(R.id.User);
        toolbar = findViewById(R.id.toolbarView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if(itemID == android.R.id.home)
        {
            finish();
        }
        if (itemID == R.id.xoaMenu)
        {
            XoaAction();
        }
        if (itemID == R.id.updateMenu)
        {
            UpdateViewAction();
        }
        return true;
    }

    private void seeView() {
        Intent intent = getIntent();
        if(intent!= null)
        {   int day = 0;
           ArrayList<Event> events = Event.eventsForDate(CalendarUtils.selectedDate);
           // ArrayList<Event> events = Event.eventsList;
           position  = intent.getIntExtra("Position",0);
            String name = events.get(position).getName();
            Name.setText(name);
            String chiTiet = events.get(position).getChiTiet();
            ChiTiet.setText(chiTiet);
            date = events.get(position).getDate();
            dateStart = events.get(position).getDateStart();
            dateEnd = events.get(position).getDateEnd();
            DateStart.setText(makeDateString(day,dateStart.getDayOfMonth(),dateStart.getMonth().getValue(),dateStart.getYear()));
            DateEnd.setText(makeDateString(day,dateEnd.getDayOfMonth(),dateEnd.getMonth().getValue(),dateEnd.getYear()));
            timeStart = events.get(position).getTime();
            timeEnd = events.get(position).getTime2();
            TimeStart.setText(CalendarUtils.formattedTime(timeStart));
            TimeEnd.setText(CalendarUtils.formattedTime(timeEnd));
            color = events.get(position).getColor();
            ColorBox.setBackgroundTintList(ColorStateList.valueOf(color));
            ID = events.get(position).getID();
            timeReminder = events.get(position).getTimeReminder();
            TimeReminder.setText(CalendarUtils.formattedTime(timeReminder) + " before");
            IDRepeat = events.get(position).getRepeat();
            User.setText(events.get(position).getUser().toString());
            setRepeat();
        }
    }
    public void setRepeat()
    {    Repeat.setText("Không lặp lại");
        if(IDRepeat == 1)
        {
            Repeat.setText("Hằng ngày");
        }
        if(IDRepeat == 2)
        {
            Repeat.setText("Hằng tuần");
        }
        if(IDRepeat == 3)
        {
            Repeat.setText("Hằng tháng");
        }
    }
    private String makeDateString(int day,int dayOfMonth, int month, int year) {
        day = LocalDate.of(year,month,dayOfMonth).getDayOfWeek().getValue()+1;
        if(day == 8)
            return "CN" + ", Ng"+dayOfMonth + ", Tg" + month +" " + year;
        else
            return "T"+day+ ", Ng"+dayOfMonth + ", Tg" + month +" " + year;
    }

//Xoa
    public void XoaAction() {
        dialog.show();
        DeleteAll.setVisibility(View.INVISIBLE);
        if(IDRepeat != 0)
        {
            DeleteAll.setVisibility(View.VISIBLE);
        }
    }
    public void DialogAction()
    {   sqLiteManager = new SQLiteManager(this);
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        DeleteOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EventView.this,"Xoa tc", Toast.LENGTH_SHORT).show();
                DeleteOnlyACtion();
                finish();
            }
        });

            DeleteAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(EventView.this, "Xoa tc", Toast.LENGTH_SHORT).show();
                    DeleteAllAction();
                    finish();
                }
            });

    }
    @SuppressLint("SuspiciousIndentation")
    public void DeleteOnlyACtion() {
        if (dateStart.isBefore(dateEnd)) {
            LocalDate date1 = dateStart;
            for (int i = 0; i < Event.eventsList.size(); ++i) {
                if (Event.eventsList.get(i).getDate().equals(date1)) {
                    if (Event.eventsList.get(i).getID() == ID) {
                        sqLiteManager.deleteData(CalendarUtils.formattedDateSQL(Event.eventsList.get(i).getDate())
                                ,Event.eventsList.get(i).getID());
                        Event.eventsList.remove(Event.eventsList.get(i));
                        i = i-1;
                        date1 = date1.plusDays(1);
                    }
                }
                if (date1.isAfter(dateEnd)) {break;}
            }
        }
        else {
            for (int i = 0; i < Event.eventsList.size(); i++) {
                if (Event.eventsList.get(i).getDate().equals(date)) {
                    if(Event.eventsList.get(i).getID() == ID) {
                        sqLiteManager.deleteData(CalendarUtils.formattedDateSQL(Event.eventsList.get(i).getDate())
                                ,Event.eventsList.get(i).getID());
                        Event.eventsList.remove(Event.eventsList.get(i));
                        break;
                    }
                }
           }
      }
    }
    public void DeleteAllAction() {

        for(int i = 0;i<Event.eventsList.size();i++)
        {
            if(Event.eventsList.get(i).getID() == ID )
            {   sqLiteManager.delteDataAll(Event.eventsList.get(i).getID());
                Event.eventsList.remove(Event.eventsList.get(i));
                i--;
            }

        }
    }
// Update
    public void UpdateViewAction() {
        Toast.makeText(EventView.this,"update tc", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EventView.this, EventUpdateView.class);
        intent.putExtra("Position",position );
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        seeView();
    }
}