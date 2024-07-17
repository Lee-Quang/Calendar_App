package com.example.btl_ltdt_2;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    public EventAdapter(@NonNull Context context, List<Event> events) {
        super(context,0,events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event =getItem(position);
        if (convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell,parent,false);
        }
        TextView eventCellTV = convertView.findViewById(R.id.eventCellTV);
        TextView eventCellColor = convertView.findViewById(R.id.eventCellColor);
        // DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd MMMM");
        // DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH mm");
        TextView eventTimeCell = convertView.findViewById(R.id.eventTimeCell);
        String eventTime = CalendarUtils.formattedTime(event.getTime())+
                " - " + CalendarUtils.formattedTime(event.getTime2());
        //+event.getDate() + " - " + CalendarUtils.selectedDate;
        String eventTitle = event.getName();
        eventCellColor.setBackgroundTintList(ColorStateList.valueOf(event.getColor()));
        eventCellTV.setText(eventTitle);
        eventTimeCell.setText(eventTime);
        return convertView;

    }
}
