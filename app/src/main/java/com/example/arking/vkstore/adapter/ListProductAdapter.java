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

import com.example.arking.vkstore.model.Market.*;

import com.facebook.drawee.view.SimpleDraweeView;

public class ListProductAdapter extends BaseAdapter {
    public Context context;
    public Market market;

    public ListProductAdapter(Context context, Market market) {
        this.context = context;
        this.market = market;
    }

    @Override
    public int getCount() {
        return market.getItems().size();
    }

    @Override
    public Item getItem(int position) {
        return market.getItems().get(position);
    }

    @Override
    public long getItemId(int position) {
        return market.getItems().get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.product_list_item, null);
        }

        TextView textPrice=(TextView) convertView.findViewById(R.id.productPrice);
        TextView textView=(TextView) convertView.findViewById(R.id.productName);
        textView.setText(market.getItems().get(position).getTitle());
        textPrice.setText(market.getItems().get(position).getPrice().getText());

        SimpleDraweeView draweeView = (SimpleDraweeView) convertView.findViewById(R.id.mainImage);
        draweeView.setImageURI(Uri.parse(market.getItems().get(position).getThumbPhoto()));
        return convertView;
    }
}
