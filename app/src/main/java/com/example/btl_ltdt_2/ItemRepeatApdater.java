package com.example.btl_ltdt_2;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ItemRepeatApdater extends ArrayAdapter<ItemRepeat> {
    public ItemRepeatApdater(@NonNull Context context,int resource, List<ItemRepeat> itemRepeats) {
        super(context, resource, itemRepeats);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_repeat_list,parent,false);
        TextView itemRepeatSelected = convertView.findViewById(R.id.itemRepeatSelected);
        ItemRepeat itemRepeat =this.getItem(position);
        if(convertView != null)
        {
            itemRepeatSelected.setText(itemRepeat.getName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_repeat,parent,false);
        TextView itemRepeatSelected = convertView.findViewById(R.id.itemRepeat);
        ItemRepeat itemRepeat = this.getItem(position);
        if(convertView != null)
        {
            itemRepeatSelected.setText(itemRepeat.getName());
        }
        return convertView;
    }
    }

