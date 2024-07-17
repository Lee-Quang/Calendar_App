package com.example.btl_ltdt_2;

import com.example.btl_ltdt_2.SQL.SQLiteManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;



public class Event {

    private int STT;
    private String Name;
    private String ChiTiet;
    private LocalDate date;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private LocalTime timeReminder;
    private LocalTime time1;
    private LocalTime time2;
    private int color;
    private int ID;
    private int Repeat;
    private String User;
    public static ArrayList<Event> eventsList = new ArrayList<>();

    public static ArrayList<Event> eventsForDate(LocalDate date) {
        ArrayList<Event> events = new ArrayList<>();

        for (Event event : eventsList) {
            if (event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }


    public Event(String name, String chiTiet, LocalDate date, LocalDate dateStart, LocalDate dateEnd, LocalTime time1, LocalTime time2, LocalTime timeReminder, int color, int id, int repeat, String user) {
        this.Name = name;
        this.ChiTiet = chiTiet;
        this.date = date;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.timeReminder = timeReminder;
        this.time2 = time2;
        this.time1 = time1;
        this.color = color;
       this.ID = id;
       this.Repeat = repeat;
        this.User = user;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time1;
    }

    public void setTime(LocalTime time1) {
        this.time1 = time1;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public LocalTime getTime2() {
        return time2;
    }

    public void setTime2(LocalTime time2) {
        this.time2 = time2;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getChiTiet() {
        return ChiTiet;
    }

    public void setChiTiet(String chiTiet) {
        ChiTiet = chiTiet;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getRepeat() {
        return Repeat;
    }

    public void setRepeat(int repeat) {
        Repeat = repeat;
    }

    public LocalTime getTimeReminder() {
        return timeReminder;
    }

    public void setTimeReminder(LocalTime timeReminder) {
        this.timeReminder = timeReminder;
    }

    public int getSTT() {
        return STT;
    }

    public void setSTT(int STT) {
        this.STT = STT;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }
}
