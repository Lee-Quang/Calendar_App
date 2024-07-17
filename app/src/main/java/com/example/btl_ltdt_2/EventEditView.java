package com.example.btl_ltdt_2;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.Date;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class EventEditView extends AppCompatActivity {
    private EditText NameET,ChiTietET;
    //private TextView eventTimeTx;
    private DatePickerDialog datePickerDialog, datePickerDialog1;
    private TextView btnDatePicker1, btnDatePicker2;
    private TimePickerDialog timePickerDialog, timePickerDialog2,reminderPickerDiaLog;
    private TextView btnTimePicker1, btnTimePicker2;
    private int defaultColor = Color.BLACK;
    private TextView colorBox,remindertx,deleteReminder;
    private int dayOfMonth1,month1,year1,dayOfMonth2,month2,year2;
    private int hours1,min1,hours2,min2,hourReminder,minuteReminder;
   private LocalTime timeReminder;
   private LocalDate date1,date2;
    private Spinner spnRepeat,spnUser;
    private int itemRepeatNum = 0;
    private SQLiteManager sqLiteManager;
    private int IDEvent = IdEvent.ID;
    private  AlarmManager alarmManager;
    private String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_event_edit_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initWidget();
        setDate();
        setTime();
        seeReminder();
        initDataPicker();
        initTimePicker();
        weekView();
        seeRepeat();
        seeUser();
        spnRepeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemRepeatNum = getItemRepeat().get(position).getNum();
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
    }

    private void initWidget() {
        NameET = findViewById(R.id.NameET);
        ChiTietET = findViewById(R.id.ChiTietET);
        // eventTimeTx = findViewById(R.id.eventTimetx);
        btnDatePicker1 = findViewById(R.id.btnDataPicker1);
        btnDatePicker2 = findViewById(R.id.btnDataPicker2);
        btnTimePicker1 = findViewById(R.id.btnTimePicker1);
        btnTimePicker2 = findViewById(R.id.btnTimePicker2);
        colorBox = findViewById(R.id.colorBox);
        remindertx = findViewById(R.id.remindertx);
        deleteReminder = findViewById(R.id.deleteReminder);

    }
    private void weekView() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM");
        btnDatePicker1.setText(getToDayDate());
        btnDatePicker2.setText(getToDayDate());
        btnTimePicker1.setText(getToTimeDate());
        btnTimePicker2.setText(getToTimeDate());
        colorBox.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
        createNotificationChannel();
    }

    public void SaveAction(View view) {
        String eventName = NameET.getText().toString().trim();
        String chiTiet = ChiTietET.getText().toString().trim();
        LocalDate dateStart = date1;
        LocalDate dateEnd = date2;
        LocalTime time1 = LocalTime.of(hours1,min1);
        LocalTime time2 = LocalTime.of(hours2,min2);
        int color = defaultColor;
       sqLiteManager = new SQLiteManager(this);
        if(dateStart.equals(dateEnd) == false)
        {   LocalDate date = dateStart;
            while(dateStart.isAfter(dateEnd)==false)
            {
                Event event2 = new Event(eventName,chiTiet,date,dateStart,dateEnd,time1,time2,timeReminder,color,IDEvent,itemRepeatNum,user);
                Event.eventsList.add(event2);
                sqLiteManager.addEvent(event2);
                date = date.plusDays(1);

                if(date.isAfter(dateEnd))
                    break;
            }
        }
        else
        { Event event = new Event(eventName,chiTiet,dateStart,dateStart,dateEnd,time1,time2,timeReminder,color,IDEvent,itemRepeatNum,user);
            Event.eventsList.add(event);
            sqLiteManager.addEvent(event);
            int hours = time1.getHour() - timeReminder.getHour() ;
            int minute = time1.getMinute() - timeReminder.getMinute();
            setTimer(hours,minute);
            SenderClass.sendDataToReceiver(this,eventName,chiTiet);
          }
        if(itemRepeatNum != 0)
        {setRepeat();}
        IdEvent.ID ++;
        finish();
    }
//DatePicker
    private void setDate()
    {
        int year = CalendarUtils.selectedDate.getYear();
        int month = CalendarUtils.selectedDate.getMonth().getValue();
        int day = CalendarUtils.selectedDate.getDayOfMonth();
        year1 = year;
        month1 = month;
        dayOfMonth1 = day;
        year2 = year1;
        month2 = month1;
        dayOfMonth2 = dayOfMonth1;
        date1 = LocalDate.of(year,month,day);
        date2 = LocalDate.of(year,month,day);
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
                date1 = LocalDate.of(year,month,dayOfMonth);
                btnDatePicker1.setText(date);
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
                date2 = LocalDate.of(year2,month2,dayOfMonth2);
                btnDatePicker2.setText(datex);
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
                date2 = LocalDate.of(year,month,dayOfMonth);
                btnDatePicker2.setText(date);
                initDataPicker();
            }
        };
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this,style,dateSetListener,year1,month1-1,dayOfMonth1);
        datePickerDialog1 = new DatePickerDialog(this,style,dateSetListener2,year2,month2-1,dayOfMonth2);
    }

    private String makeDateString(int day,int dayOfMonth, int month, int year) {
        day = LocalDate.of(year,month,dayOfMonth).getDayOfWeek().getValue()+1;
        if(day == 8)
            return "CN" + ", Ng"+dayOfMonth + ", Tg" + month +" " + year;
        else
            return "T"+day+ ", Ng"+dayOfMonth + ", Tg" + month +" " + year;
    }
    private String getToDayDate() {
        int year = CalendarUtils.selectedDate.getYear();
        int month = CalendarUtils.selectedDate.getMonth().getValue();
        int dayofMonth = CalendarUtils.selectedDate.getDayOfMonth();
        int dayofWeek = CalendarUtils.selectedDate.getDayOfWeek().getValue();
        return makeDateString(dayofWeek,dayofMonth,month,year);
    }
    public void openDate1(View view) {
        datePickerDialog.show();
    }

    public void openDate2(View view) {
        datePickerDialog1.show();
    }

    //TimePicker
    private void setTime()
    {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        hours1 =hour;
        min1 = minute;
        hours2 = hours1;
        min2 = min1;

    }
    private void initTimePicker() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hours1 = hourOfDay;
                min1 = minute;
                if(date2.equals(date1)) {
                    if (hours2 < hourOfDay) {
                        hours2 = hourOfDay;
                    } else if (hours2 == hourOfDay) {
                        if (min2 < minute) {
                            min2 = minute;
                        }

                    }
                    String time2 = makeTimeString(hours2,min2);
                    btnTimePicker2.setText(time2);
                }
                String time = makeTimeString(hourOfDay,minute);
                btnTimePicker1.setText(time);
                initTimePicker();
            }
        };
        TimePickerDialog.OnTimeSetListener timeSetListener2 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                if(date2.equals(date1))
                {
                    if(hourOfDay<hours1)
                    {
                        hourOfDay = hours1;
                    } else if (minute<min1) {
                        minute = min1;
                    }
                }
                hours2 = hourOfDay;
                min2 = minute;
                String time = makeTimeString(hours2,min2);
                btnTimePicker2.setText(time);
                initTimePicker();
            }
        };

        int style = AlertDialog.THEME_HOLO_LIGHT;
        timePickerDialog = new TimePickerDialog(this,style,timeSetListener,hours1,min1,true);
        timePickerDialog2 = new TimePickerDialog(this,style,timeSetListener2,hours2,min2,true);
    }

    private String makeTimeString(int hourOfDay, int minute) {

        return DateTimeFormatter.ofPattern("HH : mm").format(LocalTime.of(hourOfDay,minute));
    }
    private String getToTimeDate() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        return makeTimeString(hour,minute);
    }

    public void openTime1(View view) {
        timePickerDialog.show();
    }

    public void openTime2(View view) { timePickerDialog2.show();
    }

    //Color

    public void colorPick(View view) {
        openColorPicker();

    }

    private void openColorPicker() {
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(EventEditView.this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
                colorBox.setBackgroundTintList(ColorStateList.valueOf(defaultColor));
            }
        });
        ambilWarnaDialog.show();
    }
    public void backAction(View view) { finish();
    }

    // Reminder
    public void seeReminder()
    {
        remindertx.setText("Set reminder");
        hourReminder = 0;
        minuteReminder = 0;
        timeReminder = LocalTime.of(hourReminder,minuteReminder);
        deleteReminder.setVisibility(View.INVISIBLE);
        initReminder();

    }
    public void setAddReminder(View view) {
        reminderPickerDiaLog.show();
        deleteReminder.setVisibility(View.VISIBLE);
    }
    public void initReminder() {
        TimePickerDialog.OnTimeSetListener reminderSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hourReminder = hourOfDay;
                minuteReminder = minute;
                String reminder = makeReminderString(hourReminder, minuteReminder);
                remindertx.setText(reminder);
                timeReminder = LocalTime.of(hourReminder,minuteReminder);
                initReminder();
            }
        };
        int style = AlertDialog.THEME_HOLO_LIGHT;
        reminderPickerDiaLog = new TimePickerDialog(this,style,reminderSetListener,hourReminder,minuteReminder,true);
    }

    private String makeReminderString(int hourOfDay, int minute) {

        return DateTimeFormatter.ofPattern("HH : mm").format(LocalTime.of(hourOfDay,minute)) + " " + "before";
    }
    public void DeleteReminderAction(View view) {
        seeReminder();
    }

  private void createNotificationChannel() {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          CharSequence name = "Reminder Channel";
          String description = "Channel for reminder notifications";
          int importance = NotificationManager.IMPORTANCE_DEFAULT;
          NotificationChannel channel = new NotificationChannel("201", name, importance);
          channel.setDescription(description);
          NotificationManager notificationManager = getSystemService(NotificationManager.class);
          notificationManager.createNotificationChannel(channel);
      }
  }
    private void setTimer(int hoursReminder, int minuteReminder) {
        Intent intent = new Intent(EventEditView.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Lấy AlarmManager
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Đặt báo động vào thời điểm đã thiết lập
        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.YEAR, LocalDate.now().getYear());
//        calendar.set(Calendar.MONTH, LocalDate.now().getMonthValue()); // Tháng bắt đầu từ 0 (tháng 1 là 0)
//        calendar.set(Calendar.DAY_OF_MONTH, LocalDate.now().getDayOfMonth()); // Ngày
        calendar.set(Calendar.HOUR_OF_DAY, hoursReminder); // Giờ (24 giờ)
        calendar.set(Calendar.MINUTE, minuteReminder); // Phút
        //calendar.set(Calendar.SECOND, 0); // Giây

        long alarmTime = calendar.getTimeInMillis();
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, alarmTime,AlarmManager.INTERVAL_DAY,pendingIntent);
        }
    //Repeat
    private List<ItemRepeat>getItemRepeat()
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
    public void seeUser()
    { spnUser = findViewById(R.id.spinnerUser);
        UserAdapter userAdapter= new UserAdapter(EventEditView.this,R.layout.user_list,getItemUser());
        spnUser.setAdapter(userAdapter);
    }
    public void seeRepeat()
    { spnRepeat = findViewById(R.id.spinnerRepeat);
       ItemRepeatApdater itemAdapter= new ItemRepeatApdater(EventEditView.this,R.layout.item_repeat_list,getItemRepeat());
        spnRepeat.setAdapter(itemAdapter);
    }
    public void setRepeat()
    {   String eventName = NameET.getText().toString();
        String chiTiet = ChiTietET.getText().toString();
        LocalDate dateStart = date1;
        LocalDate dateEnd = date2;
        LocalTime time1 = LocalTime.of(hours1,min1);
        LocalTime time2 = LocalTime.of(hours2,min2);
        int yearMax =dateEnd.getYear()+20;
        int color = defaultColor;
        if(itemRepeatNum == 1)
        {
            while(dateEnd.getYear() <= yearMax )
            {  dateStart = dateStart.plusDays(1);
                dateEnd = dateEnd.plusDays(1);
                LocalDate date = dateStart;
                if(dateStart.isEqual(dateEnd)==false) {
                    while (dateStart.isAfter(dateEnd) == false) {
                        Event event2 = new Event(eventName,chiTiet ,date, dateStart, dateEnd, time1, time2,timeReminder,color,IDEvent,itemRepeatNum,user);
                        Event.eventsList.add(event2);
                        sqLiteManager.addEvent(event2);
                        date = date.plusDays(1);
                        if (date.isAfter(dateEnd))
                            break;
                    }
                }
                else
                {
                    Event event2 = new Event(eventName,chiTiet, dateStart, dateStart, dateEnd, time1, time2,timeReminder, color,IDEvent,itemRepeatNum,user);
                    Event.eventsList.add(event2);
                    sqLiteManager.addEvent(event2);
                }
            }
        }
        // Hang tuan
        if(itemRepeatNum == 2)
        {
            while(dateEnd.getYear() <= yearMax )
            {  dateStart = dateStart.plusDays(7);
                dateEnd = dateEnd.plusDays(7);
                LocalDate date = dateStart;
                if(dateStart.isEqual(dateEnd)==false) {
                    while (dateStart.isAfter(dateEnd) == false) {
                        Event event2 = new Event(eventName, chiTiet,date, dateStart, dateEnd, time1, time2,timeReminder,color,IDEvent,itemRepeatNum,user);
                        Event.eventsList.add(event2);
                        sqLiteManager.addEvent(event2);
                        date = date.plusDays(1);
                        if (date.isAfter(dateEnd))
                            break;
                    }
                }
                else
                {
                    Event event2 = new Event(eventName,chiTiet, dateStart, dateStart, dateEnd, time1, time2,timeReminder,color,IDEvent,itemRepeatNum,user);
                    Event.eventsList.add(event2);
                    sqLiteManager.addEvent(event2);
                }
            }
        }
        //Hang thang max sau 15 nam tinh tu ngay dat

            if(itemRepeatNum == 3)
            {
                while(dateEnd.getYear() <= yearMax )
                {  dateStart = dateStart.plusMonths(1);
                    dateEnd = dateEnd.plusMonths(1);
                    LocalDate date = dateStart;
                    if(dateStart.isEqual(dateEnd)==false) {
                        while (dateStart.isAfter(dateEnd) == false) {
                            Event event2 = new Event(eventName,chiTiet, date, dateStart, dateEnd, time1, time2,timeReminder,color,IDEvent,itemRepeatNum,user);
                            Event.eventsList.add(event2);
                            sqLiteManager.addEvent(event2);
                            date = date.plusDays(1);
                            if (date.isAfter(dateEnd))
                                break;
                        }
                    }
                    else
                    {
                        Event event2 = new Event(eventName,chiTiet, dateStart, dateStart, dateEnd, time1, time2,timeReminder,color,IDEvent,itemRepeatNum,user);
                        Event.eventsList.add(event2);
                        sqLiteManager.addEvent(event2);
                    }
                }
            }
    }
}