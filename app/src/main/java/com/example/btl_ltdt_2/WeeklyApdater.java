package com.example.btl_ltdt_2;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeeklyApdater extends RecyclerView.Adapter<WeeklyViewHolder> {
    private final ArrayList<LocalDate> days;
    private final OnItemlistener onItemlistener;
    public WeeklyApdater(ArrayList<LocalDate> days, OnItemlistener onItemListener) {
        this.days = days;
        this.onItemlistener = onItemListener;
    }
    @NonNull
    @Override
    public WeeklyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.week_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = ((View) view).getLayoutParams();
        layoutParams.height = (int) (parent.getHeight());
        return new WeeklyViewHolder(view,onItemlistener,days);
    }

    @Override
    public void onBindViewHolder(@NonNull WeeklyViewHolder holder, int position) {
        final LocalDate date = days.get(position);

        holder.dayofMonth.setText(String.valueOf(date.getDayOfMonth()));
        if(date.equals(CalendarUtils.selectedDate))
        {
            holder.parentWeekView.setBackgroundColor(Color.LTGRAY);

        }
        if(date.equals(LocalDate.now()))
        {
            holder.dayofMonth.setTextColor(Color.BLUE);
        }
        if(date.getMonth().equals(CalendarUtils.selectedDate.getMonth())==false)
        {
            holder.dayofMonth.setTextColor(Color.GRAY);
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
    }
}
