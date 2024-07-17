package com.example.btl_ltdt_2;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    public final ArrayList<LocalDate> days;
    public  final View parentView;
    public final TextView dayofMonth;
    public final ListView eventList;
    private final CalendarApdater.OnItemlistener onItemlistener ;



    public CalendarViewHolder(@NonNull View itemView, CalendarApdater.OnItemlistener onItemlistener, ArrayList<LocalDate> days) {
        super(itemView);
        parentView = itemView.findViewById(R.id.parentView);
        dayofMonth = itemView.findViewById(R.id.CellDaytx);
        eventList = itemView.findViewById(R.id.eventList);
        this.onItemlistener = onItemlistener;
        this.days = days;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

    }

    @Override
    public void onClick(View view) {
        onItemlistener.onItemClick(getAdapterPosition(),days.get(getAdapterPosition()));

    }

    @Override
    public boolean onLongClick(View v) {
        onItemlistener.onItemLong(getAdapterPosition(),days.get(getAdapterPosition()));
        return true;
    }
}
