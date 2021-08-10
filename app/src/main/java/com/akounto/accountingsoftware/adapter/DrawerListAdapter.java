package com.akounto.accountingsoftware.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.model.DrawerModelItems;
import java.util.ArrayList;

public class DrawerListAdapter extends BaseExpandableListAdapter {
    private final Context context;
    private final ArrayList<DrawerModelItems> drawerModelItems;
    private final LayoutInflater inflater;

    @SuppressLint("WrongConstant")
    public DrawerListAdapter(Context context2, ArrayList<DrawerModelItems> drawerModelItems2) {
        this.context = context2;
        this.drawerModelItems = drawerModelItems2;
        this.inflater = (LayoutInflater) context2.getSystemService("layout_inflater");
    }

    public int getGroupCount() {
        return this.drawerModelItems.size();
    }

    public int getChildrenCount(int groupPosition) {
        return this.drawerModelItems.get(groupPosition).drawerItems.size();
    }

    public Object getGroup(int groupPosition) {
        return this.drawerModelItems.get(groupPosition);
    }

    public Object getChild(int groupPosition, int childPosition) {
        return this.drawerModelItems.get(groupPosition).drawerItems.get(childPosition);
    }

    public long getGroupId(int groupPosition) {
        return 0;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    public boolean hasStableIds() {
        return false;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.item_drawer_list, null);
        }
        DrawerModelItems drawerModelItems2 = (DrawerModelItems) getGroup(groupPosition);
        RelativeLayout relativeLayout = convertView.findViewById(R.id.mainLayout);
        TextView titleTV = convertView.findViewById(R.id.textView1);
        ImageView img = convertView.findViewById(R.id.imageView1);
        ImageView imgIcon = convertView.findViewById(R.id.imageViewArrow);
        if (isExpanded) {
            imgIcon.setVisibility(View.VISIBLE);
            imgIcon.setImageResource(R.drawable.ic_baseline_expand_less);
        } else {
            imgIcon.setVisibility(View.VISIBLE);
            imgIcon.setImageResource(R.drawable.ic_baseline_expand_more);
        }
        String title = drawerModelItems2.title;
        titleTV.setText(title);
        if (title == "Dashboard") {
            img.setImageResource(R.drawable.ic_dashboard);
            imgIcon.setVisibility(View.GONE);
        } else if (title == "Purchase") {
            img.setImageResource(R.drawable.ic_shopping_cart);
        } else if (title == "Sales") {
            img.setImageResource(R.drawable.ic_discount_sales);
        } else if (title == "Accounting") {
            img.setImageResource(R.drawable.ic_keys_accounting);
        } else if (title == "Report") {
            imgIcon.setVisibility(View.GONE);
            img.setImageResource(R.drawable.ic_report_sales);
        } else if (title == "Support") {
            imgIcon.setVisibility(View.GONE);
            img.setImageResource(R.drawable.ic_headphones_support);
        }
        return convertView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.item_drawer_sub_list, null);
        }
        ((TextView) convertView.findViewById(R.id.textViewSublist)).setText((String) getChild(groupPosition, childPosition));
        Log.e("Akram", groupPosition + " ChildView");
        return convertView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
