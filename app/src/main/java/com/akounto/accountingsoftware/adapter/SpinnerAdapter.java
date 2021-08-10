package com.akounto.accountingsoftware.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.CountryData;
import com.akounto.accountingsoftware.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SpinnerAdapter  extends ArrayAdapter<CountryData> {

    ArrayList<CountryData> list;
    LayoutInflater inflater;
    ViewHolder holder = null;

    public SpinnerAdapter(Context context, int textViewResourceId, ArrayList<CountryData> list) {
        super(context, textViewResourceId, list);
        inflater = ((Activity) context).getLayoutInflater();
        this.list = list;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        CountryData listItemAddProg = list.get(position);
        View row = convertView;
        if (null == row) {
            holder = new ViewHolder();
            row = inflater.inflate(R.layout.mobile_code_item, parent, false);
            holder.name = (TextView) row.findViewById(R.id.country);
            holder.img = (ImageView) row.findViewById(R.id.img_flag);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        holder.name.setText(listItemAddProg.getName());
        Picasso.with(getContext()).load(Constant.IMG_URL+list.get(position).getCountryCode()+".png").into(holder.img);
        return row;
    }

    static class ViewHolder {
        TextView name;
        ImageView img;
    }
}
