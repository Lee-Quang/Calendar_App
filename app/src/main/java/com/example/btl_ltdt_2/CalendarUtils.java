package com.example.btl_ltdt_2;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CalendarUtils {
    public static LocalDate selectedDate;
    public static String formattedDateSQL (LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }
    public static LocalDate parseDateSQl(String Date)
    {
        return LocalDate.parse(Date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    public static LocalTime parseTimeSQl(String Time)
    {
        return LocalTime.parse(Time,DateTimeFormatter.ofPattern("HH:mm"));
    }
    public static String formattedTime(LocalTime time)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }
    public static String formattedWeek(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM");

        return date.format(formatter);
    }
public static String MonthFromDate (LocalDate date)
{
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM ");
        return date.format(formatter);
}
public static ArrayList<LocalDate> daysInMonthArray(LocalDate date)
{
    ArrayList<LocalDate> daysInMonthArray = new ArrayList<>();
    YearMonth yearMonth = YearMonth.from(date);
    int daysInMonth = yearMonth.lengthOfMonth();
    LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
    int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();
    int a = 1;
    for(int i = 1;i<=42 ;i++)
    {
        if(i<= dayOfWeek )
        {
            daysInMonthArray.add(yearMonth.atDay(1).minusDays(dayOfWeek-i+1));
        } else if (i > daysInMonth+ dayOfWeek) {
            daysInMonthArray.add(yearMonth.atEndOfMonth().plusDays(a));
            a++;

        } else
        {
            daysInMonthArray.add(LocalDate.of(selectedDate.getYear(),selectedDate.getMonth(),i - dayOfWeek));
        }
    }
    return daysInMonthArray;
}


    public static ArrayList<LocalDate> daysInWeekArray(LocalDate selectedDate) {
        ArrayList<LocalDate> days = new ArrayList<>();
        LocalDate current = sundayForDate(selectedDate);
        LocalDate endDate = current.plusWeeks(1);
        while (current.isBefore(endDate))
        {
            days.add(current);
            current = current.plusDays(1);
        }
        return days;

    }

    private static LocalDate sundayForDate(LocalDate current) {
        LocalDate oneWeekAgo = current.minusWeeks(1);
        while(current.isAfter(oneWeekAgo))
        {
            if(current.getDayOfWeek() == DayOfWeek.SUNDAY)
                return current;
            current = current.minusDays(1);
        }
        return null;
    }

}
