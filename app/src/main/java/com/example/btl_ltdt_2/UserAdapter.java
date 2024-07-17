package com.example.btl_ltdt_2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {
    public UserAdapter(@NonNull Context context, int resource, List<User> users) {
        super(context, resource, users);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_list,parent,false);
        TextView itemRepeatSelected = convertView.findViewById(R.id.itemUserList);
        User user =this.getItem(position);
        if(convertView != null)
        {
            itemRepeatSelected.setText(user.getName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.user,parent,false);
        TextView itemRepeatSelected = convertView.findViewById(R.id.itemUser);
        User user = this.getItem(position);
        if(convertView != null)
        {
            itemRepeatSelected.setText(user.getName());
        }
        return convertView;
    }
    }

