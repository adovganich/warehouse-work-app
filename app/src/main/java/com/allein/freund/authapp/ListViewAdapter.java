package com.allein.freund.authapp;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.allein.freund.authapp.remote.Item;

import java.util.List;

/**
 * Created by freund on 1/10/18.
 */
public class ListViewAdapter extends BaseAdapter {

    public List<Item> ItemList;
    Activity activity;

    public ListViewAdapter(Activity activity, List<Item> ItemList) {
        super();
        this.activity = activity;
        this.ItemList = ItemList;
    }

    @Override
    public int getCount() {
        return ItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return ItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView sId;
        TextView sName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_row, null);
            holder = new ViewHolder();
            holder.sId = (TextView) convertView.findViewById(R.id.sId);
            holder.sName = (TextView) convertView.findViewById(R.id.sName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Item item = ItemList.get(position);
        holder.sId.setText(String.valueOf(item.getId()));
        holder.sName.setText(String.valueOf(item.getName()));

        return convertView;
    }
}
