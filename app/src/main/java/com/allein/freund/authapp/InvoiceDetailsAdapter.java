package com.allein.freund.authapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.allein.freund.authapp.remote.InvoiceDetails;

import java.util.List;

/**
 * Created by freund on 1/10/18.
 */
public class InvoiceDetailsAdapter extends BaseAdapter {

    public List<InvoiceDetails> itemsList;
    Activity activity;

    public InvoiceDetailsAdapter(Activity activity, List<InvoiceDetails> detailsList) {
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
        TextView dAmount;
        TextView dCost;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.details_row, null);
            holder = new ViewHolder();
            holder.dName = (TextView) convertView.findViewById(R.id.dName);
            holder.dAmount = (TextView) convertView.findViewById(R.id.dAmount);
            holder.dCost = (TextView) convertView.findViewById(R.id.dCost);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        InvoiceDetails item = itemsList.get(position);
        holder.dName.setText(String.valueOf(item.getName()));
        holder.dAmount.setText(String.valueOf(item.getAmount()));
        holder.dCost.setText(String.valueOf(item.getCost()));

        return convertView;
    }
}
