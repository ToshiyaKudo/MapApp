package com.example.kudotoshiya.mapapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.kudotoshiya.mapapp.Item;
import com.example.kudotoshiya.mapapp.R;

import java.util.List;

/**
 * Created by kudo.toshiya on 2015/06/04.
 */
public class CustomAdapter extends ArrayAdapter<Item> {

    private LayoutInflater layoutInflater_;

    private List<Item> list;

    public CustomAdapter(Context context, int resource) {
        super(context, resource);
    }

    public CustomAdapter(Context context, int resource, List<Item> list) {
        super(context, resource, list);
        this.list = list;
        layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    static class ViewHolder {
        TextView address;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder vh = null;

        if (convertView == null) {
            convertView = layoutInflater_.inflate(R.layout.listitem,null);
            vh = new ViewHolder();
            vh.address = (TextView)convertView.findViewById(R.id.text_address);
            convertView.setTag(vh);
        }else{
            vh = (ViewHolder) convertView.getTag();
        }

        Item item = super.getItem(position);
        vh.address.setText(item.getAddress());

        return convertView;
    }
}
