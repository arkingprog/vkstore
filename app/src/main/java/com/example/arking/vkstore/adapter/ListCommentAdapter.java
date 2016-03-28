package com.example.arking.vkstore.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.arking.vkstore.R;
import com.example.arking.vkstore.Util;
import com.example.arking.vkstore.model.Comment.Comment;
import com.example.arking.vkstore.model.Comment.Item;
import com.example.arking.vkstore.model.Comment.Profile;
import com.facebook.drawee.view.SimpleDraweeView;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;



public class ListCommentAdapter extends BaseAdapter {
    Context context;
    Comment comment;
    Long groupId;

    public ListCommentAdapter(Context context,Comment comment,Long groupId) {
        this.context=context;
        this.comment=comment;
        this.groupId=groupId;
    }

    @Override
    public int getCount() {
        return comment.getItems().size();
    }

    @Override
    public Item getItem(int position) {
        return comment.getItems().get(position);
    }

    @Override
    public long getItemId(int position) {
        return comment.getItems().get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_comment_item, null);
        }
        TextView textComment=(TextView)convertView.findViewById(R.id.textComment);
        TextView nameProfile=(TextView)convertView.findViewById(R.id.nameProfile);
        TextView dateComment=(TextView)convertView.findViewById(R.id.dateComment);
        TextView countLike=(TextView)convertView.findViewById(R.id.countLike);
        final ToggleButton btnLike=(ToggleButton)convertView.findViewById(R.id.btnLike);

        final View finalConvertView = convertView;
        if(comment.getItems().get(position).getLikes().getUserLikes()==1)
            btnLike.setChecked(true);
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnLike.isChecked()) {
                    ((TextView) finalConvertView.findViewById(R.id.countLike)).setText(Util.removeLike("market_comment",
                            "-" + groupId,
                            Long.valueOf(comment.getItems().get(position).getId())));
                } else {
                    ((TextView) finalConvertView.findViewById(R.id.countLike)).setText(Util.setLike("market_comment",
                            "-" + groupId,
                            Long.valueOf(comment.getItems().get(position).getId())));               }
            }
        });

        SimpleDraweeView imageProfile=(SimpleDraweeView)convertView.findViewById(R.id.imageProfile);

        textComment.setText(String.valueOf(comment.getItems().get(position).getText()));

        Date time=new Date((long)comment.getItems().get(position).getDate()*1000);
        SimpleDateFormat dateFormat=new SimpleDateFormat("d MMMM y 'в' HH:mm");
        dateComment.setText(String.valueOf(dateFormat.format(time)));

        countLike.setText(String.valueOf(comment.getItems().get(position).getLikes().getCount()));


        for(Profile profile: comment.getProfiles())
        {
            if(comment.getItems().get(position).getFromId().equals(profile.getId()))
            {
                nameProfile.setText(profile.getLastName() + " " + profile.getFirstName());
                imageProfile.setImageURI(Uri.parse(profile.getPhoto50()));
            }
        }

        return convertView;
    }
}
