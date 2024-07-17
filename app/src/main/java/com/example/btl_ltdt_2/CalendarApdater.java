package com.example.btl_ltdt_2;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarApdater extends RecyclerView.Adapter<CalendarViewHolder> {
    private final ArrayList<LocalDate> days;
    private final OnItemlistener onItemlistener;

    public CalendarApdater(ArrayList<LocalDate> days, OnItemlistener onItemListener) {
        this.days = days;
        this.onItemlistener = onItemListener;

    }
    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = ((View) view).getLayoutParams();
        //month view
        layoutParams.height = (int) (parent.getHeight()*0.1666666666);
        return new CalendarViewHolder(view,onItemlistener,days);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        final LocalDate date = days.get(position);

            holder.dayofMonth.setText(String.valueOf(date.getDayOfMonth()));
            if(date.equals(LocalDate.now()))
            {
                holder.dayofMonth.setTextColor(Color.BLUE);
            }
            if(date.getMonth().equals(CalendarUtils.selectedDate.getMonth())==false)
            {
                holder.dayofMonth.setTextColor(Color.GRAY);
            }
            //for(LocalDate a : days)
            { ArrayList<Event> events = Event.eventsForDate(date);
            CalendarEvent calendarEvent = new CalendarEvent(holder.parentView.getContext(),events);
            holder.eventList.setAdapter(calendarEvent);
            }

    }


    @Override
    public int getItemCount()
    {
        return days.size();
    }
    public interface OnItemlistener
    {
        void onItemClick(int position, LocalDate date);
        void onItemLong(int position, LocalDate date);
    }

}
