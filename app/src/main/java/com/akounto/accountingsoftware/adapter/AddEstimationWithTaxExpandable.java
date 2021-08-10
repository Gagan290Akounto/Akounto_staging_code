package com.akounto.accountingsoftware.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.CommonInvoiceActivity;
import com.akounto.accountingsoftware.response.Product;
import com.akounto.accountingsoftware.response.ProductServiceTaxesItem;
import com.akounto.accountingsoftware.response.TaxResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddEstimationWithTaxExpandable extends BaseExpandableListAdapter {
    private final Context _context;
    List<Product> chapterList;
    int[] drawbleList;
    String[] odiaNumberList = {"୧", "୨", "୩", "୪", "୫", "୬", " ୭", "୮", "୯", "୧୦", "୧୧", "୧୨", "୧୩", "୧୪", "୧୫", "୧୬", "୧୭", "୧୮", "୧୯", "୨୦", "୨୧", "୨୨", "୨୩", "୨୪", "୨୫", "୨୬", "୨୭", "୨୮", "୨୯", "୩୦", "୨୧", "୨୧", "୨୧", "୨୧"};
    Map<Integer, Integer> quantity;
    Map<Integer, Integer> quantityMap;
    Map<Integer, List<TaxResponse>> taxList;

    public AddEstimationWithTaxExpandable(Context context, List<Product> chapterList2, Map<Integer, List<TaxResponse>> taxList2, Map<Integer, Integer> quantity2) {
        this._context = context;
        this.drawbleList = this.drawbleList;
        this.chapterList = chapterList2;
        this.taxList = taxList2;
        this.quantity = quantity2;
    }

    public int getGroupCount() {
        return this.chapterList.size();
    }

    public int getChildrenCount(int i) {
        if (this.taxList.get(Integer.valueOf(this.chapterList.get(i).getId())) == null || this.taxList.get(Integer.valueOf(this.chapterList.get(i).getId())).size() == 0) {
            return 1;
        }
        return this.taxList.get(Integer.valueOf(this.chapterList.get(i).getId())).size();
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
            convertView = ((LayoutInflater) this._context.getSystemService("layout_inflater")).inflate(R.layout.item_parentexpandabletax, null);
        }
        ((TextView) convertView.findViewById(R.id.pnameTV)).setText(this.chapterList.get(groupPosition).getName());
        ((TextView) convertView.findViewById(R.id.pDescTV)).setText(this.chapterList.get(groupPosition).getDescription());
        StringBuilder sb = new StringBuilder();
        sb.append("$");
        double price = this.chapterList.get(groupPosition).getPrice();
        double intValue = this.quantity.get(Integer.valueOf(this.chapterList.get(groupPosition).getId())).intValue();
        Double.isNaN(intValue);
        sb.append(price * intValue);
        ((TextView) convertView.findViewById(R.id.pNmae)).setText(sb.toString());
        convertView.findViewById(R.id.deleteProduct).setOnClickListener(view -> AddEstimationWithTaxExpandable.this.lambda$getGroupView$0$AddEstimationWithTaxExpandable(groupPosition, view));
        return convertView;
    }

    public void lambda$getGroupView$0$AddEstimationWithTaxExpandable(int groupPosition, View v) {
        ((CommonInvoiceActivity) this._context).deleteProduct(groupPosition);
    }

    @SuppressLint("WrongConstant")
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View convertView2;
        int i = groupPosition;
        int i2 = childPosition;
        String childText = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView2 = ((LayoutInflater) this._context.getSystemService("layout_inflater")).inflate(R.layout.item_expandablechildtax, null);
        } else {
            convertView2 = convertView;
        }
        ImageView addTaxIV = convertView2.findViewById(R.id.addTaxIV);
        ImageView detailsIVSub = convertView2.findViewById(R.id.deleteTax);
        convertView2.findViewById(R.id.editIcon).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                AddEstimationWithTaxExpandable.this.lambda$getChildView$1$AddEstimationWithTaxExpandable(i, view);
            }
        });
        convertView2.findViewById(R.id.addTaxIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEstimationWithTaxExpandable.this.lambda$getChildView$2$AddEstimationWithTaxExpandable(i, v);
            }
        });
        ImageView deleteTax = convertView2.findViewById(R.id.deleteTax);
        TextView taxAMount = convertView2.findViewById(R.id.taxAMount);
        TextView chapterName = convertView2.findViewById(R.id.chapterName);
        LinearLayout childQuantityLL = convertView2.findViewById(R.id.childQuantityLL);
        TextView quantitytv = convertView2.findViewById(R.id.quantity);
        if (this.quantity.get(Integer.valueOf(this.chapterList.get(i).getId())) != null) {
            StringBuilder sb = new StringBuilder();
            String str = childText;
            sb.append(this.quantity.get(Integer.valueOf(this.chapterList.get(i).getId())));
            sb.append("");
            quantitytv.setText(sb.toString());
        }
        if (this.taxList.get(Integer.valueOf(this.chapterList.get(i).getId())) == null || this.taxList.get(Integer.valueOf(this.chapterList.get(i).getId())).size() == 0) {
            chapterName.setText("Add Tax");
            taxAMount.setText("");
            deleteTax.setVisibility(4);
        } else {
            List<TaxResponse> taxListChild = this.taxList.get(Integer.valueOf(this.chapterList.get(i).getId()));
            if (i2 == taxListChild.size() - 1) {
                addTaxIV.setVisibility(0);
            } else {
                addTaxIV.setVisibility(4);
                taxAMount.setGravity(5);
            }
            chapterName.setText(taxListChild.get(i2).getName());
            ImageView imageView = convertView2.findViewById(R.id.detailsIV);
            StringBuilder sb2 = new StringBuilder();
            List<TaxResponse> list = taxListChild;
            sb2.append(taxListChild.get(i2).getEffectiveTaxes().get(0).getRate());
            sb2.append("%");
            taxAMount.setText(sb2.toString());
            if (i2 == 0) {
                childQuantityLL.setVisibility(0);
            } else {
                childQuantityLL.setVisibility(8);
            }
            detailsIVSub.setOnClickListener(view -> AddEstimationWithTaxExpandable.this.lambda$getChildView$3$AddEstimationWithTaxExpandable(i, i2, view));
            deleteTax.setVisibility(0);
        }
        return convertView2;
    }

    public void lambda$getChildView$1$AddEstimationWithTaxExpandable(int groupPosition, View v) {
        ((CommonInvoiceActivity) this._context).edit(groupPosition,this.chapterList);
    }

    public void lambda$getChildView$2$AddEstimationWithTaxExpandable(int groupPosition, View v) {
        Product product = this.chapterList.get(groupPosition);
        //product.setProductServiceTaxes(marge(chapterList.get(groupPosition).getProductServiceTaxes(),convert(taxList.get(Integer.valueOf(chapterList.get(groupPosition).getId())))));
        List<TaxResponse> taxResponse = taxList.get(Integer.valueOf(this.chapterList.get(groupPosition).getId()));
        product.setProductServiceTaxes(convert(taxResponse));
        ((CommonInvoiceActivity) this._context).addTax(product);
    }

    public void lambda$getChildView$3$AddEstimationWithTaxExpandable(int groupPosition, int childPosition, View v) {
        ((CommonInvoiceActivity) this._context).deleteTax(this.chapterList.get(groupPosition), childPosition);
    }

    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    public void notifyData(List<Product> purchaseItemListSelected) {
        this.chapterList = purchaseItemListSelected;
        notifyDataSetChanged();
    }

    public void notifyDataMAp(Map<Integer, List<TaxResponse>> taxList, Map<Integer, Integer> quantityMap) {
        this.taxList = taxList;
        this.quantityMap = quantityMap;
        notifyDataSetChanged();
    }


    public static List<ProductServiceTaxesItem> convert(List<TaxResponse> list) {
        List<ProductServiceTaxesItem> taxResponses = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ProductServiceTaxesItem taxResponse = new ProductServiceTaxesItem();
            taxResponse.setName(list.get(i).getName());
            taxResponse.setTaxId(list.get(i).getId());
            taxResponses.add(taxResponse);
        }
        return taxResponses;
    }
    public static List<ProductServiceTaxesItem> marge(List<ProductServiceTaxesItem> list1,List<ProductServiceTaxesItem> list2) {
        for (int i = 0; i < list2.size(); i++) {
            list1.add(list2.get(i));
        }
        return list1;
    }
}
