package com.example.btl_ltdt_2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.btl_ltdt_2.SQL.SQLiteManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class EventUpdateView extends AppCompatActivity {
    private TextView NameET, ChiTietET, DateStart, DateEnd, TimeStart, TimeEnd, TimeReminder, ColorBox, DeleteReminder;
    private TextView UpdateOnly, UpdateAll, Cancel;
    private Spinner spnRepeat,spnUser;
    private LocalDate date, dateStart, dateEnd, dateStartBD, dateEndBD;
    private LocalTime timeStart, timeEnd, timeReminder;
    private int colorSet, ID, IDRepeat, IDRepeatBD;
    private Dialog dialog;
    private DatePickerDialog dateStratPickerDialog, dateEndPickerDialog;
    private TimePickerDialog timeStartPickerDialog, timeEndPickerDialog, timeRimderPickerDialog;
    private int dayOfMonth1, month1, year1;
    private int dayOfMonth2, month2, year2;
    private int hours1, minute1, hours2,minute2, hourReminder,minuteReminder;
    private int position;
    private SQLiteManager sqLiteManager;
    private String userBD;
    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_update_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
          initWidget();
         seeView();
         setDate();
         setTime();
         initDataPicker();
         initTimePicker();
        spnRepeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               IDRepeat = getItemRepeat().get(position).getNum();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spnUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                user = getItemUser().get(position).getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        DialogUpdateAction();
    }

    private void initWidget() {
        NameET = findViewById(R.id.NameET);
        ChiTietET = findViewById(R.id.ChiTietET);
        DateStart = findViewById(R.id.btnDataPicker1);
        DateEnd = findViewById(R.id.btnDataPicker2);
        TimeStart = findViewById(R.id.btnTimePicker1);
        TimeEnd = findViewById(R.id.btnTimePicker2);
        ColorBox = findViewById(R.id.colorBox);
        TimeReminder = findViewById(R.id.remindertx);
        DeleteReminder = findViewById(R.id.deleteReminder);
        spnRepeat = findViewById(R.id.spinnerRepeat);
        spnUser = findViewById(R.id.spinnerUser);
        sqLiteManager = new SQLiteManager(this);
        ViewDialog();
    }

    private void seeView() {
        Intent intent = this.getIntent();
        if (intent != null) {
            int day = 0;
            ArrayList<Event> events = Event.eventsForDate(CalendarUtils.selectedDate);
            position = intent.getIntExtra("Position", 0);
            String name = events.get(position).getName();
            userBD = events.get(position).getUser().trim();
            NameET.setText(name);
            String chiTiet = events.get(position).getChiTiet();
            ChiTietET.setText(chiTiet);
            date = events.get(position).getDate();
            dateStart = events.get(position).getDateStart();
            dateStartBD = events.get(position).getDateStart();
            dateEnd = events.get(position).getDateEnd();
            dateEndBD = events.get(position).getDateEnd();
            DateStart.setText(makeDateString(day, dateStart.getDayOfMonth(), dateStart.getMonth().getValue(), dateStart.getYear()));
            DateEnd.setText(makeDateString(day, dateEnd.getDayOfMonth(), dateEnd.getMonth().getValue(), dateEnd.getYear()));
            timeStart = events.get(position).getTime();
            timeEnd = events.get(position).getTime2();
            TimeStart.setText(CalendarUtils.formattedTime(timeStart));
            TimeEnd.setText(CalendarUtils.formattedTime(timeEnd));
            colorSet = events.get(position).getColor();
            ColorBox.setBackgroundTintList(ColorStateList.valueOf(colorSet));
            ID = events.get(position).getID();
            timeReminder = events.get(position).getTimeReminder();
            TimeReminder.setText(CalendarUtils.formattedTime(timeReminder) + " before" );
            IDRepeat = events.get(position).getRepeat();
            IDRepeatBD = events.get(position).getRepeat();
            seeRepeat();
            seeUser();
        }

    }
    //Date
    private void setDate()
    {
       dayOfMonth1 = dateStart.getDayOfMonth();
       month1 = dateStart.getMonth().getValue();
       year1 = dateStart.getYear();
       dayOfMonth2 = dateEnd.getDayOfMonth();
       month2 = dateEnd.getMonth().getValue();
       year2 = dateEnd.getYear();
    }
    private void initDataPicker() {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                dayOfMonth1 = dayOfMonth;
                year1 = year;
                month1 = month;
                int day = 0;
                String date = makeDateString( day,dayOfMonth, month, year);
                dateStart = LocalDate.of(year,month,dayOfMonth);
                DateStart.setText(date);
                if(year2<year)
                {
                    year2 = year;
                } else if (year2 == year) {
                    if(month2<month)
                    {
                        month2 = month;
                    } else if (month2 == month) {
                        if(dayOfMonth2<dayOfMonth)
                            dayOfMonth2 = dayOfMonth;
                    }

                }
                String datex = makeDateString(day,dayOfMonth2,month2,year2);
                dateEnd = LocalDate.of(year2,month2,dayOfMonth2);
                DateEnd.setText(datex);
                initDataPicker();
            }
        };
        DatePickerDialog.OnDateSetListener dateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                if(year<year1)
                {
                    year = year1;
                } else if (year == year1) {
                    if(month<month1)
                    {
                        month = month1;
                    } else if (month == month1) {
                        if(dayOfMonth<dayOfMonth1)
                            dayOfMonth = dayOfMonth1;
                    }

                }
                dayOfMonth2 = dayOfMonth;
                year2 = year;
                month2 = month;
                int day = 0;
                String date = makeDateString(day,dayOfMonth,month,year);
                dateEnd = LocalDate.of(year,month,dayOfMonth);
                DateEnd.setText(date);
                initDataPicker();
            }
        };
        int style = AlertDialog.THEME_HOLO_LIGHT;
        dateStratPickerDialog = new DatePickerDialog(this,style,dateSetListener,year1,month1-1,dayOfMonth1);
        dateEndPickerDialog = new DatePickerDialog(this,style,dateSetListener2,year2,month2-1,dayOfMonth2);
    }

    public void openDate1(View view) {
        dateStratPickerDialog.show();
    }

    public void openDate2(View view) {
        dateEndPickerDialog.show();
    }
    // Time Picker
    private void setTime()
    {
        hours1 =timeStart.getHour();
        minute1 = timeStart.getMinute();
        hours2 = timeEnd.getHour();
        minute2 = timeEnd.getMinute();

    }
    private void initTimePicker() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hours1 = hourOfDay;
                minute1 = minute;
                if(dateEnd.equals(dateStart)) {
                    if (hours2 < hourOfDay) {
                        hours2 = hourOfDay;
                    } else if (hours2 == hourOfDay) {
                        if (minute2 < minute) {
                            minute2 = minute;
                        }

                    }
                    String time2 = makeTimeString(hours2,minute2);
                    TimeEnd.setText(time2);
                }
                String time = makeTimeString(hourOfDay,minute);
                TimeStart.setText(time);
                initTimePicker();
            }
        };
        TimePickerDialog.OnTimeSetListener timeSetListener2 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                if(dateEnd.equals(dateStart))
                {
                    if(hourOfDay<hours1)
                    {
                        hourOfDay = hours1;
                    } else if (minute<minute1) {
                        minute = minute1;
                    }
                }
                hours2 = hourOfDay;
                minute2 = minute;
                String time = makeTimeString(hours2,minute2);
                DateEnd.setText(time);
                initTimePicker();
            }
        };

        int style = AlertDialog.THEME_HOLO_LIGHT;
        timeStartPickerDialog = new TimePickerDialog(this,style,timeSetListener,hours1,minute1,true);
        timeEndPickerDialog = new TimePickerDialog(this,style,timeSetListener2,hours2,minute1,true);
    }

    private String makeTimeString(int hourOfDay, int minute) {

        return DateTimeFormatter.ofPattern("HH : mm").format(LocalTime.of(hourOfDay,minute));
    }

    public void openTime1(View view) {
        timeStartPickerDialog.show();
    }

    public void openTime2(View view) { timeEndPickerDialog.show();
    }

    //Color
    public void colorPick(View view) {
        openColorPicker();
    }

    private void openColorPicker() {
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(EventUpdateView.this, colorSet, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                colorSet = color;
                ColorBox.setBackgroundTintList(ColorStateList.valueOf(colorSet));
            }
        });
        ambilWarnaDialog.show();
    }

    // Repeat
    private List<ItemRepeat> getItemRepeat()
    {
        List<ItemRepeat> list = new ArrayList<>() ;
        list.add(new ItemRepeat("Không lặp lại",0));
        list.add(new ItemRepeat("Hằng ngày",1));
        list.add(new ItemRepeat("Hằng tuần",2));
        list.add(new ItemRepeat("Hằng tháng",3));
        return list;
    }
    private List<User>getItemUser()
    {   List<User> users = new ArrayList<>();
        users.add(new User(" "));
        users.add(new User("A"));
        users.add(new User("B"));
        users.add(new User("C"));
        users.add(new User("D"));
        return users;
    }
    public void seeRepeat()
    {
        ItemRepeatApdater itemAdapter= new ItemRepeatApdater(EventUpdateView.this,R.layout.item_repeat_list,getItemRepeat());
        spnRepeat.setAdapter(itemAdapter);
        spnRepeat.setSelection(IDRepeat);
    }
    public void seeUser()
    {
        UserAdapter userAdapter= new UserAdapter(EventUpdateView.this,R.layout.user_list,getItemUser());
        spnUser.setAdapter(userAdapter);
        spnUser.setSelection(getIndex(userBD));
    }
    private int getIndex(String user)
    {
        for( int i = 0;i<getItemUser().size();i++)
        {
            if(getItemUser().get(i).getName().equals(user))
            {
                return i;
            }
        }
        return 0 ;
    }
    private String makeDateString(int day,int dayOfMonth, int month, int year) {
        day = LocalDate.of(year,month,dayOfMonth).getDayOfWeek().getValue()+1;
        if(day == 8)
            return "CN" + ", Ng"+dayOfMonth + ", Tg" + month +" " + year;
        else
            return "T"+day+ ", Ng"+dayOfMonth + ", Tg" + month +" " + year;
    }
// Reminder
public void seeReminder()
{
    TimeReminder.setText("Set reminder");
    hourReminder = 0;
    minuteReminder = 0;
    timeReminder = LocalTime.of(hourReminder,minuteReminder);
    DeleteReminder.setVisibility(View.INVISIBLE);
    initReminder();
}
    public void setAddReminder(View view) {
        timeRimderPickerDialog.show();
        DeleteReminder.setVisibility(View.VISIBLE);
    }
    public void initReminder() {
        TimePickerDialog.OnTimeSetListener reminderSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hourReminder = hourOfDay;
                minuteReminder = minute;
                String reminder = makeTimeString(hourReminder, minuteReminder);
                TimeReminder.setText(reminder);
                timeReminder = LocalTime.of(hourReminder,minuteReminder);
                initReminder();
            }
        };
        int style = AlertDialog.THEME_HOLO_LIGHT;
        timeRimderPickerDialog = new TimePickerDialog(this,style,reminderSetListener,hourReminder,minuteReminder,true);
    }
    public void DeleteReminderAction(View view) {
        seeReminder();
    }
    public void BackUpdateAction(View view) {
        finish();
    }
    //Dialog update
    public void ViewDialog()
    {   dialog = new Dialog(EventUpdateView.this);
        dialog.setContentView(R.layout.dialog_update);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.color_box));
        dialog.setCancelable(false);
        UpdateOnly = dialog.findViewById(R.id.UpdateOnly);
        UpdateAll = dialog.findViewById(R.id.UdapteAll);
        Cancel = dialog.findViewById(R.id.Cancel);


    }
    // Save Update
    public void SaveAction(View view) {
        dialog.show();
        UpdateOnly.setVisibility(View.INVISIBLE);
        if(IDRepeat == IDRepeatBD || IDRepeatBD == 0)
        {
            UpdateOnly.setVisibility(View.VISIBLE);
        }
    }
    public void DialogUpdateAction()
    {
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        UpdateOnly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateOnlyAction();
                setRepeat();
                ketThuc();
            }
        });

        UpdateAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EventUpdateView.this,"Up tc ", Toast.LENGTH_SHORT).show();
                UpdateOnlyAction();
                DeleteAfter();
                setRepeat();
                ketThuc();
            }
        });
    }
    public void UpdateOnlyAction() {
        if (dateEnd.isBefore(dateStartBD) == false || dateStart.isAfter(dateEndBD) == false) {
            if (dateStart.isBefore(dateEnd)) {
                UpdateDateStart();
                UpdateDateStartToEnd();
                UpdateDateEnd();
            } else {
                if (dateStartBD.equals(dateEndBD)) {
                    for (int i = 0; i < Event.eventsList.size(); i++) {
                        if (Event.eventsList.get(i).getDate().equals(date)) {
                            if (Event.eventsList.get(i).getID() == ID) {
                                LocalDate dateSQL = Event.eventsList.get(i).getDate();
                                date = dateStart;
                                Update(i);
                                sqLiteManager.update_data(Event.eventsList.get(i), dateSQL);
                            }
                        }
                    }
                }
                if (dateStartBD.isBefore(dateEndBD)) {

                    for (int i = 0; i < Event.eventsList.size(); i++) {
                        if (Event.eventsList.get(i).getID() == ID) {
                            if (Event.eventsList.get(i).getDate().equals(date)) {
                                LocalDate dateSQL = date;
                                date = dateStart;
                                Update(i);
                                sqLiteManager.update_data(Event.eventsList.get(i), dateSQL);
                            } else {
                                String Date = CalendarUtils.formattedDateSQL(Event.eventsList.get(i).getDate());
                                sqLiteManager.deleteData(Date, ID);
                                Event.eventsList.remove(Event.eventsList.get(i));
                                i--;
                                date = date.plusDays(1);
                            }
                        }
                    }
                }
            }
        }
        else
        {
            DeleteAll();
            Add();
        }
    }
    public void UpdateDateStartToEnd() {
        if (dateStart.isBefore(dateStartBD) == false && dateEnd.isAfter(dateEndBD) == false) {
            date = dateStart;
            for (int i = 0; i < Event.eventsList.size(); i++) {
                if (Event.eventsList.get(i).getDate().equals(date)) {
                    if (Event.eventsList.get(i).getID() == ID) {
                        LocalDate dateSQL = Event.eventsList.get(i).getDate();
                        Update(i);
                        sqLiteManager.update_data(Event.eventsList.get(i),dateSQL);
                        date = date.plusDays(1);
                    }
                }
                if (date.isAfter(dateEnd)) break;
            }
        }
    }
    public void UpdateDateStart() {
        if (dateStart.isBefore(dateStartBD)) {
            date = dateStart;
            while (date.isBefore(dateStartBD)) {
                String eventName = NameET.getText().toString();
                String chiTiet = ChiTietET.getText().toString();
                Event event = new Event(eventName, chiTiet, date, dateStart, dateEnd, timeStart, timeEnd, timeReminder, colorSet, ID, IDRepeat,user);
                Event.eventsList.add(event);
                sqLiteManager.addEvent(event);
                date = date.plusDays(1);
            }
        }
        if (dateStart.isAfter(dateStartBD)) {
            date = dateStartBD;
            for (int i = 0; i < Event.eventsList.size(); i++) {
                if (Event.eventsList.get(i).getDate().equals(date)) {
                    if (Event.eventsList.get(i).getID() == ID) {
                        if (date.isBefore(dateStart)) {
                            String Date = CalendarUtils.formattedDateSQL(Event.eventsList.get(i).getDate());
                            sqLiteManager.deleteData(Date,ID);
                            Event.eventsList.remove(Event.eventsList.get(i));
                            i--;
                            date = date.plusDays(1);
                        }
                    }

                }
                if (date.equals(dateStart)) break;
            }
        }
    }
    public void UpdateDateEnd() {
        if (dateEnd.isBefore(dateEndBD)) {
            date = dateEnd;
            for (int i = 0; i < Event.eventsList.size(); i++) {
                if (Event.eventsList.get(i).getDate().equals(date)) {
                    if (Event.eventsList.get(i).getID() == ID) {
                        String Date = CalendarUtils.formattedDateSQL(Event.eventsList.get(i).getDate());
                        sqLiteManager.deleteData(Date, ID);
                        Event.eventsList.remove(Event.eventsList.get(i));
                        i--;
                        date = date.plusDays(1);
                    }
                }
                if (date.equals(dateEndBD)) break;
            }
        }
        if (dateEnd.isAfter(dateEndBD)) {
            date =dateEndBD;
            while (date.isAfter(dateEnd)==false) {
                String eventName = NameET.getText().toString();
                String chiTiet = ChiTietET.getText().toString();
                Event event = new Event(eventName, chiTiet, date, dateStart, dateEnd, timeStart, timeEnd, timeReminder, colorSet, ID, IDRepeat,user);
                Event.eventsList.add(event);
                sqLiteManager.addEvent(event);
                date = date.plusDays(1);
            }
        }
    }


    public void Update(int i)
    {
        Event.eventsList.get(i).setName(NameET.getText().toString());
        Event.eventsList.get(i).setChiTiet(ChiTietET.getText().toString());
        Event.eventsList.get(i).setDate(date);
        Event.eventsList.get(i).setDateStart(dateStart);
        Event.eventsList.get(i).setDateEnd(dateEnd);
        Event.eventsList.get(i).setTime(timeStart);
        Event.eventsList.get(i).setTime2(timeEnd);
        Event.eventsList.get(i).setTimeReminder(timeReminder);
        Event.eventsList.get(i).setUser(user);
        Event.eventsList.get(i).setColor(colorSet);
        Event.eventsList.get(i).setRepeat(IDRepeat);
    }
    public void ketThuc()
    {
        Intent i = new Intent(EventUpdateView.this,WeeklyViewActivity.class);
        startActivity(i);
    }
    public void DeleteAll() {
    date = dateStartBD;
        for(int i = 0;i<Event.eventsList.size();i++)
        {   if(Event.eventsList.get(i).getDate().equals(date)) {
            if (Event.eventsList.get(i).getID() == ID) {
                String Date = CalendarUtils.formattedDateSQL(Event.eventsList.get(i).getDate());
                sqLiteManager.deleteData(Date, ID);
                Event.eventsList.remove(Event.eventsList.get(i));
                i--;
                date = date.plusDays(1);
            }
        }
            if (date.isAfter(dateEndBD)) break;
        }
    }
    public void Add()
    {   String eventName = NameET.getText().toString();
        String chiTiet = ChiTietET.getText().toString();
        if(dateStart.equals(dateEnd) == false)
        {   LocalDate date = dateStart;
            while(dateStart.isAfter(dateEnd)==false)
            {
                Event event = new Event(eventName, chiTiet, date, dateStart, dateEnd, timeStart, timeEnd, timeReminder, colorSet, ID, IDRepeat,user);
                Event.eventsList.add(event);
                date = date.plusDays(1);
                sqLiteManager.addEvent(event);
                if(date.isAfter(dateEnd))
                    break;
            }
        }
        else
        {   Event event = new Event(eventName, chiTiet, date, dateStart, dateEnd, timeStart, timeEnd, timeReminder, colorSet, ID, IDRepeat,user);
            Event.eventsList.add(event);
            sqLiteManager.addEvent(event);
            SenderClass.sendDataToReceiver(this,eventName,chiTiet);
        }
    }
    public void DeleteAfter()
    {
        date = dateEnd;
        for(int i = 0;i<Event.eventsList.size();i++)
        {   if(Event.eventsList.get(i).getDate().isAfter(dateEnd)) {
            if (Event.eventsList.get(i).getID() == ID) {
                String Date = CalendarUtils.formattedDateSQL(Event.eventsList.get(i).getDate());
                sqLiteManager.deleteData(Date, ID);
                Event.eventsList.remove(Event.eventsList.get(i));
                i--;
                date = date.plusDays(1);
            }
        }
        }
    }
    public void setRepeat()
    {   String eventName = NameET.getText().toString();
        String chiTiet = ChiTietET.getText().toString();
        int yearMax =dateEnd.getYear()+20;
        if(IDRepeat == 1)
        {
            while(dateEnd.getYear() <= yearMax )
            {  dateStart = dateStart.plusDays(1);
                dateEnd = dateEnd.plusDays(1);
                LocalDate date = dateStart;
                if(dateStart.isEqual(dateEnd)==false) {
                    while (dateStart.isAfter(dateEnd) == false) {
                        Event event = new Event(eventName, chiTiet, date, dateStart, dateEnd, timeStart, timeEnd, timeReminder, colorSet, ID, IDRepeat,user);
                        Event.eventsList.add(event);
                        sqLiteManager.addEvent(event);
                        date = date.plusDays(1);
                        if (date.isAfter(dateEnd))
                            break;
                    }
                }
                else
                {
                    Event event = new Event(eventName, chiTiet, date, dateStart, dateEnd, timeStart, timeEnd, timeReminder, colorSet, ID, IDRepeat,user);
                    Event.eventsList.add(event);
                    sqLiteManager.addEvent(event);
                }
            }
        }
        // Hang tuan
        if(IDRepeat == 2)
        {
            while(dateEnd.getYear() <= yearMax )
            {  dateStart = dateStart.plusDays(7);
                dateEnd = dateEnd.plusDays(7);
                LocalDate date = dateStart;
                if(dateStart.isEqual(dateEnd)==false) {
                    while (dateStart.isAfter(dateEnd) == false) {
                        Event event = new Event(eventName, chiTiet, date, dateStart, dateEnd, timeStart, timeEnd, timeReminder, colorSet, ID, IDRepeat,user);
                        Event.eventsList.add(event);
                        sqLiteManager.addEvent(event);
                        date = date.plusDays(1);
                        if (date.isAfter(dateEnd))
                            break;
                    }
                }
                else
                {
                    Event event = new Event(eventName, chiTiet, date, dateStart, dateEnd, timeStart, timeEnd, timeReminder, colorSet, ID, IDRepeat,user);
                    Event.eventsList.add(event);
                    sqLiteManager.addEvent(event);
                }
            }
        }
        //Hang thang max sau 15 nam tinh tu ngay dat

        if(IDRepeat == 3)
        {
            while(dateEnd.getYear() <= yearMax )
            {  dateStart = dateStart.plusMonths(1);
                dateEnd = dateEnd.plusMonths(1);
                LocalDate date = dateStart;
                if(dateStart.isEqual(dateEnd)==false) {
                    while (dateStart.isAfter(dateEnd) == false) {
                        Event event = new Event(eventName, chiTiet, date, dateStart, dateEnd, timeStart, timeEnd, timeReminder, colorSet, ID, IDRepeat,user);
                        Event.eventsList.add(event);
                        sqLiteManager.addEvent(event);
                        date = date.plusDays(1);
                        if (date.isAfter(dateEnd))
                            break;
                    }
                }
                else
                {
                    Event event = new Event(eventName, chiTiet, date, dateStart, dateEnd, timeStart, timeEnd, timeReminder, colorSet, ID, IDRepeat,user);
                    Event.eventsList.add(event);
                    sqLiteManager.addEvent(event);
                }
            }
        }
    }
}