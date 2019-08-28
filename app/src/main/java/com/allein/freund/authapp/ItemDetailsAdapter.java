package com.allein.freund.authapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.allein.freund.authapp.remote.ItemDetails;

import java.util.List;

/**
 * Created by freund on 1/10/18.
 */
public class ItemDetailsAdapter extends BaseAdapter {

    public List<ItemDetails> itemsList;
    Activity activity;

    public ItemDetailsAdapter(Activity activity, List<ItemDetails> detailsList) {
        super();
        this.activity = activity;
        this.itemsList = detailsList;
    }

    @Override
    public int getCount() {
        return itemsList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView dName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.details_row, null);
            holder = new ViewHolder();
            holder.dName = (TextView) convertView.findViewById(R.id.dName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ItemDetails item = itemsList.get(position);
        holder.dName.setText(String.valueOf(item.getName()));

        return convertView;
    }
}
