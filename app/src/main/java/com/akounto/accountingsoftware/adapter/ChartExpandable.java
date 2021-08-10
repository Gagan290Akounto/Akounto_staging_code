package com.akounto.accountingsoftware.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.AddChatOfAccountActivity;
import com.akounto.accountingsoftware.Activity.fragment.ChartSubDetailsFragment;
import com.akounto.accountingsoftware.response.chartaccount.HeadSubType;
import com.akounto.accountingsoftware.response.chartaccount.HeadType;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.LocalManager;
import java.util.List;

public class ChartExpandable extends BaseExpandableListAdapter {
    private final Context _context;
    List<HeadType> chapterList;
    int[] drawbleList;
    String[] odiaNumberList = {"୧", "୨", "୩", "୪", "୫", "୬", " ୭", "୮", "୯", "୧୦", "୧୧", "୧୨", "୧୩", "୧୪", "୧୫", "୧୬", "୧୭", "୧୮", "୧୯", "୨୦", "୨୧", "୨୨", "୨୩", "୨୪", "୨୫", "୨୬", "୨୭", "୨୮", "୨୯", "୩୦", "୨୧", "୨୧", "୨୧", "୨୧"};

    public ChartExpandable(Context context, List<HeadType> chapterList2) {
        this._context = context;
        this.drawbleList = this.drawbleList;
        this.chapterList = chapterList2;
    }

    public int getGroupCount() {
        return this.chapterList.size();
    }

    public int getChildrenCount(int i) {
        return this.chapterList.get(i).getHeadSubTypes().size();
    }

    public Object getGroup(int i) {
        return null;
    }

    public Object getChild(int i, int i1) {
        return null;
    }

    public long getGroupId(int i) {
        return 0;
    }

    public long getChildId(int i, int i1) {
        return 0;
    }

    public boolean hasStableIds() {
        return false;
    }

    @SuppressLint("WrongConstant")
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String str = (String) getGroup(groupPosition);
        if (convertView == null) {
            convertView = ((LayoutInflater) this._context.getSystemService("layout_inflater")).inflate(R.layout.item_parentexpandable, null);
        }
        ((TextView) convertView.findViewById(R.id.pnameTV)).setText(this.chapterList.get(groupPosition).getName());
        TextView pNmae = convertView.findViewById(R.id.pNmae);
        int totalTypes = 0;
        for (HeadSubType headSubType : this.chapterList.get(groupPosition).getHeadSubTypes()) {
            totalTypes += headSubType.getHeadTransactions().size();
        }
        pNmae.setText(totalTypes + "");
        return convertView;
    }

    @SuppressLint("WrongConstant")
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String str = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = ((LayoutInflater) this._context.getSystemService("layout_inflater")).inflate(R.layout.item_expandablechild, null);
        }
        ((TextView) convertView.findViewById(R.id.chapterName)).setText(this.chapterList.get(groupPosition).getHeadSubTypes().get(childPosition).getName());
        convertView.findViewById(R.id.chilDLL).setOnClickListener(view -> ChartExpandable.this.lambda$getChildView$0$ChartExpandable(groupPosition, childPosition, view));
        convertView.findViewById(R.id.detailsIV).setOnClickListener(view -> ChartExpandable.this.lambda$getChildView$1$ChartExpandable(chapterList.get(groupPosition).getHeadSubTypes().get(childPosition).getId(), view));
        return convertView;
    }

    public /* synthetic */ void lambda$getChildView$0$ChartExpandable(int groupPosition, int childPosition, View v) {
        LocalManager.getInstance().setHeadTransactionsList(this.chapterList.get(groupPosition).getHeadSubTypes().get(childPosition).getHeadTransactions());
        Bundle bundle = new Bundle();
        bundle.putInt("id", this.chapterList.get(groupPosition).getHeadSubTypes().get(childPosition).getId());
        AddFragments.addFragmentToDrawerActivity(this._context, bundle, ChartSubDetailsFragment.class);
    }

    public /* synthetic */ void lambda$getChildView$1$ChartExpandable(int categoryId, View v) {
        Context context = this._context;
        context.startActivity(AddChatOfAccountActivity.buildIntent(context, categoryId));
    }

    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
