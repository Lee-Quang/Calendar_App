package com.example.btl_ltdt_2.SQL;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.btl_ltdt_2.CalendarUtils;
import com.example.btl_ltdt_2.Event;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class SQLiteManager extends SQLiteOpenHelper {
    private static SQLiteManager sqLiteManager;
    private static final String DATABASE_NAME = "Event.db";
    private static final int DATABASE_VISION = 1;
    private static final String TABLE_NAME = "EventTB";
    private static final String COLUMN_STT = "STT";
    private static final String COLUMN_Title = "Title";
    private static final String COLUMN_ChiTiet = "ChiTiet";
    private static final String COLUMN_Date = "Date";
    private static final String COLUMN_DateStart = "DateStart";
    private static final String COLUMN_DateEnd = "DateEnd";
    private static final String COLUMN_TimeStart = "TimeStart";
    private static final String COLUMN_TimeEnd = "TimeEnd";
    private static final String COLUMN_TimeReminder = "TimeReminder";
    private static final String COLUMN_Color = "Color";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_Repeat = "Repeat";
    private static final String COLUMN_User = "User";
    private Context context;

    public SQLiteManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VISION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TABLE_NAME
                        + "( STT INTEGER PRIMARY KEY AUTOINCREMENT ,"
                        + COLUMN_Title + " NTEXT,"
                        + COLUMN_ChiTiet + " NTEXT,"
                        + COLUMN_Date + " NTEXT,"
                        + COLUMN_DateStart + " NTEXT,"
                        + COLUMN_DateEnd + " NTEXT,"
                        + COLUMN_TimeStart + " NTEXT,"
                        + COLUMN_TimeEnd + " NTEXT,"
                        + COLUMN_TimeReminder + " NTEXT,"
                        + COLUMN_User + " NTEXT,"
                        + COLUMN_Color + " INTEGER,"
                        + COLUMN_ID + " INTEGER,"
                        + COLUMN_Repeat + " INTEGER);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addEvent(Event event) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_Title, event.getName());
        cv.put(COLUMN_ChiTiet, event.getChiTiet());
        cv.put(COLUMN_Date, CalendarUtils.formattedDateSQL(event.getDate()));
        cv.put(COLUMN_DateStart, CalendarUtils.formattedDateSQL(event.getDateStart()));
        cv.put(COLUMN_DateEnd, CalendarUtils.formattedDateSQL(event.getDateEnd()));
        cv.put(COLUMN_TimeStart, CalendarUtils.formattedTime(event.getTime()));
        cv.put(COLUMN_TimeEnd, CalendarUtils.formattedTime(event.getTime2()));
        cv.put(COLUMN_TimeReminder, CalendarUtils.formattedTime(event.getTimeReminder()));
        cv.put(COLUMN_User,event.getUser());
        cv.put(COLUMN_Color, event.getColor());
        cv.put(COLUMN_ID, event.getID());
        cv.put(COLUMN_Repeat, event.getRepeat());
        long result = db.insert(TABLE_NAME, null, cv);
        db.close();
        return result;
    }

    public void dataEvent() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                String Name = cursor.getString(1);
                String ChiTiet = cursor.getString(2);
                String Date = cursor.getString(3);
                String DateStart = cursor.getString(4);
                String DateEnd = cursor.getString(5);
                String TimeStart = cursor.getString(6);
                String TimeEnd = cursor.getString(7);
                String TimeReminder = cursor.getString(8);
                String User = cursor.getString(9);
                int Color = cursor.getInt(10);
                int IDEvent = cursor.getInt(11);
                int IDRepeat = cursor.getInt(12);
                LocalDate date = CalendarUtils.parseDateSQl(Date);
                LocalDate dateStart = CalendarUtils.parseDateSQl(DateStart);
                LocalDate dateEnd = CalendarUtils.parseDateSQl(DateEnd);
                LocalTime timeStart = CalendarUtils.parseTimeSQl(TimeStart);
                LocalTime timeEnd = CalendarUtils.parseTimeSQl(TimeEnd);
                LocalTime timeReminder = CalendarUtils.parseTimeSQl(TimeReminder);
                Event event = new Event(Name, ChiTiet, date, dateStart, dateEnd, timeStart, timeEnd, timeReminder, Color, IDEvent, IDRepeat,User);
                Event.eventsList.add(event);
            }
        }
    }

    public void deleteData(String Date, int IDEvent) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = "Date = ? AND ID = ?";
        String[] whereArgs = {Date, String.valueOf(IDEvent)};
        db.delete(TABLE_NAME, whereClause, whereArgs);
    }
    public void delteDataAll(int IDEvent)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "ID = ?", new String[]{String.valueOf(IDEvent)});
    }
    @SuppressLint("Range")
    public int IDEventMax ()
    {
        int maxVal = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null)
        {  cursor = db.rawQuery(" SELECT MAX (" + COLUMN_ID + ") AS max_val FROM "+ TABLE_NAME,null);}
            if (cursor.moveToFirst()) {
                maxVal = cursor.getInt(cursor.getColumnIndex("max_val"));
            }
        cursor.close();
        db.close();
        return maxVal;
    }
    public void update_data(Event event,LocalDate date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_Title, event.getName());
        cv.put(COLUMN_ChiTiet, event.getChiTiet());
        cv.put(COLUMN_Date, CalendarUtils.formattedDateSQL(event.getDate()));
        cv.put(COLUMN_DateStart, CalendarUtils.formattedDateSQL(event.getDateStart()));
        cv.put(COLUMN_DateEnd, CalendarUtils.formattedDateSQL(event.getDateEnd()));
        cv.put(COLUMN_TimeStart, CalendarUtils.formattedTime(event.getTime()));
        cv.put(COLUMN_TimeEnd, CalendarUtils.formattedTime(event.getTime2()));
        cv.put(COLUMN_TimeReminder, CalendarUtils.formattedTime(event.getTimeReminder()));
        cv.put(COLUMN_User, event.getUser());
        cv.put(COLUMN_Color, event.getColor());
        cv.put(COLUMN_Repeat, event.getRepeat());
        String Date = CalendarUtils.formattedDateSQL(date);
        String IDEvent = String.valueOf(event.getID());
        String whereClause = "Date = ? AND ID = ?";
        String[] whereArgs = {Date, String.valueOf(IDEvent)};
        db.update(TABLE_NAME,cv,whereClause,whereArgs);
    }
}




