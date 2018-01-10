package com.allein.freund.authapp;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.allein.freund.authapp.remote.Invoice;

import java.util.List;

/**
 * Created by freund on 1/10/18.
 */
public class ListViewAdapter extends BaseAdapter {

    public List<Invoice> invoiceList;
    Activity activity;

    public ListViewAdapter(Activity activity, List<Invoice> invoiceList) {
        super();
        this.activity = activity;
        this.invoiceList = invoiceList;
    }

    @Override
    public int getCount() {
        return invoiceList.size();
    }

    @Override
    public Object getItem(int position) {
        return invoiceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView sNumber;
        TextView sCustomer;
        TextView sPositions;
        TextView sMoney;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_row, null);
            holder = new ViewHolder();
            holder.sNumber = (TextView) convertView.findViewById(R.id.sNumber);
            holder.sCustomer = (TextView) convertView.findViewById(R.id.sCustomer);
            holder.sPositions = (TextView) convertView.findViewById(R.id.sPositions);
            holder.sMoney = (TextView) convertView.findViewById(R.id.sMoney);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Invoice item = invoiceList.get(position);
        holder.sNumber.setText(String.valueOf(item.getNumber()));
        holder.sCustomer.setText(String.valueOf(item.getCustomer()));
        holder.sPositions.setText(String.valueOf(item.getPositions()));
        holder.sMoney.setText(String.valueOf(item.getMoney()));

        return convertView;
    }
}
