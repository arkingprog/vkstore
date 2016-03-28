package com.example.arking.vkstore.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.arking.vkstore.R;
import com.example.arking.vkstore.model.Message.Item;
import com.example.arking.vkstore.model.Message.Message;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class ListMessagesAdapter extends BaseAdapter {
    Context context;
    Message message;
    List<Item> items;
    public ListMessagesAdapter(Context context, Message message) {
        this.context = context;
        Collections.reverse(message.getItems());
        this.message = message;
    }

    @Override
    public int getCount() {
        return message.getItems().size();
    }

    @Override
    public Item getItem(int position) {
        return message.getItems().get(position);
    }

    @Override
    public long getItemId(int position) {
        return message.getItems().get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_messages_item, null);
        }

        TextView txtMessages=(TextView)convertView.findViewById(R.id.txtMessage);
        TextView txtDate=(TextView)convertView.findViewById(R.id.txtDate);


        LinearLayout contentWithBG=(LinearLayout)convertView.findViewById(R.id.contentWithBackground);
        LinearLayout content=(LinearLayout)convertView.findViewById(R.id.content);

        if(message.getItems().get(position).getOut().equals(1)){
            contentWithBG.setBackgroundResource(R.drawable.out_message_bg);
        }
        else {
            RelativeLayout.LayoutParams lp =
                    (RelativeLayout.LayoutParams) content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            content.setLayoutParams(lp);
            contentWithBG.setBackgroundResource(R.drawable.in_message_bg);
        }

        txtMessages.setText(message.getItems().get(position).getBody());
        Date time=new Date((long)message.getItems().get(position).getDate()*1000);
        SimpleDateFormat dateFormat=new SimpleDateFormat("d MMMM y 'в' HH:mm");
        txtDate.setText(String.valueOf(dateFormat.format(time)));


        return convertView;
    }
}
