package com.example.btl_ltdt_2;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CalendarEvent extends ArrayAdapter<Event> {
    public CalendarEvent(@NonNull Context context, List<Event> events) {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position);

            if(convertView == null)
            {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_main,parent,false);
            }
            TextView eventMainName = convertView.findViewById(R.id.eventMainName);
            LinearLayout eventMain = convertView.findViewById(R.id.EventMain);
            eventMainName.setText(event.getName());
            eventMain.setBackgroundTintList(ColorStateList.valueOf(event.getColor()));

        return convertView;
    }
}
