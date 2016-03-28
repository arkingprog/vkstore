package com.example.arking.vkstore.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.arking.vkstore.R;
import com.example.arking.vkstore.model.userGroup;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class UserGroupAdapter extends BaseAdapter {
    Context context;
    List<userGroup> groupList;

    public UserGroupAdapter(Context context,List<userGroup> groupList) {
        this.context=context;
        this.groupList=groupList;
    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public userGroup getItem(int position) {
        return groupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return groupList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.user_group_list_item, null);
        }
        TextView txtName = (TextView) convertView.findViewById(R.id.groupName);
        txtName.setText(groupList.get(position).getName());
        SimpleDraweeView draweeView = (SimpleDraweeView) convertView.findViewById(R.id.groupImage);
        draweeView.setImageURI(Uri.parse(groupList.get(position).getPhotoURL()));
        return convertView;
    }
}
