package com.example.btl_ltdt_2;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeeklyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public final ArrayList<LocalDate> days;
    public  final View parentWeekView;
    public final TextView dayofMonth;
    private final WeeklyApdater.OnItemlistener onItemlistener ;



    public WeeklyViewHolder(@NonNull View itemView, WeeklyApdater.OnItemlistener onItemlistener, ArrayList<LocalDate> days) {
        super(itemView);
        parentWeekView= itemView.findViewById(R.id.parentWeekView);
        dayofMonth = itemView.findViewById(R.id.CellWeekDaytx);
        this.onItemlistener = onItemlistener;
        this.days = days;
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        onItemlistener.onItemClick(getAdapterPosition(),days.get(getAdapterPosition()));
    }
}
