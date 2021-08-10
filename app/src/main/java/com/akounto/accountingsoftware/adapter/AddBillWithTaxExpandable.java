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
import com.akounto.accountingsoftware.Activity.CreateBillActivity;
import com.akounto.accountingsoftware.response.ProductServiceTaxesItem;
import com.akounto.accountingsoftware.response.PurchaseItem;
import com.akounto.accountingsoftware.response.TaxResponse;
import com.akounto.accountingsoftware.response.currency.Currency;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddBillWithTaxExpandable extends BaseExpandableListAdapter {
    private final Context _context;
    List<PurchaseItem> chapterList;
    int[] drawbleList;
    String[] odiaNumberList = {"୧", "୨", "୩", "୪", "୫", "୬", " ୭", "୮", "୯", "୧୦", "୧୧", "୧୨", "୧୩", "୧୪", "୧୫", "୧୬", "୧୭", "୧୮", "୧୯", "୨୦", "୨୧", "୨୨", "୨୩", "୨୪", "୨୫", "୨୬", "୨୭", "୨୮", "୨୯", "୩୦", "୨୧", "୨୧", "୨୧", "୨୧"};
    Map<Integer, Integer> quantity;
    Currency selectedExchangeCurrency;
    Map<Integer, List<TaxResponse>> taxList;

    public AddBillWithTaxExpandable(Context context, List<PurchaseItem> chapterList2, Map<Integer, List<TaxResponse>> taxList2, Map<Integer, Integer> quantity2) {
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
        String symbol = "$";
        Currency currency = this.selectedExchangeCurrency;
        if (currency != null) {
            symbol = currency.getSymbol();
        }
        ((TextView) convertView.findViewById(R.id.pnameTV)).setText(this.chapterList.get(groupPosition).getName());
        ((TextView) convertView.findViewById(R.id.pDescTV)).setText(this.chapterList.get(groupPosition).getDescription());
        TextView pNmae = convertView.findViewById(R.id.pNmae);
        int quant = 1;
        if (this.quantity.get(Integer.valueOf(this.chapterList.get(groupPosition).getId())) != null) {
            quant = this.quantity.get(Integer.valueOf(this.chapterList.get(groupPosition).getId())).intValue();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(symbol);
        double price = this.chapterList.get(groupPosition).getPrice();
        double d = quant;
        Double.isNaN(d);
        sb.append(price * d);
        pNmae.setText(sb.toString());
        convertView.findViewById(R.id.deleteProduct).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                AddBillWithTaxExpandable.this.lambda$getGroupView$0$AddBillWithTaxExpandable(groupPosition, view);
            }
        });
        ((ImageView) convertView.findViewById(R.id.help_group_indicator)).setImageResource(isExpanded ? R.drawable.ic_up_arrow : R.drawable.ic_down_arrow_circular);
        return convertView;
    }

    public void lambda$getGroupView$0$AddBillWithTaxExpandable(int groupPosition, View v) {
        ((CreateBillActivity) this._context).deleteProduct(groupPosition);
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
        ImageView deleteTax = convertView2.findViewById(R.id.deleteTax);
        convertView2.findViewById(R.id.addTaxIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBillWithTaxExpandable.this.lambda$getChildView$1$AddBillWithTaxExpandable(i, v);
            }
        });
        TextView taxAMount = convertView2.findViewById(R.id.taxAMount);
        TextView chapterName = convertView2.findViewById(R.id.chapterName);
        LinearLayout childQuantityLL = convertView2.findViewById(R.id.childQuantityLL);
        if (this.taxList.get(Integer.valueOf(this.chapterList.get(i).getId())) == null || this.taxList.get(Integer.valueOf(this.chapterList.get(i).getId())).size() == 0) {
            ImageView imageView = addTaxIV;
            childQuantityLL.setVisibility(0);
            chapterName.setText("Add Tax");
            taxAMount.setText("");
            deleteTax.setVisibility(4);
        } else {
            List<TaxResponse> taxListChild = this.taxList.get(Integer.valueOf(this.chapterList.get(i).getId()));
            chapterName.setText(taxListChild.get(i2).getName());
            if (i2 == taxListChild.size() - 1) {
                addTaxIV.setVisibility(0);
            } else {
                addTaxIV.setVisibility(4);
                taxAMount.setGravity(5);
            }
            if (i2 == 0) {
                childQuantityLL.setVisibility(0);
            } else {
                childQuantityLL.setVisibility(8);
            }
            ImageView imageView2 = convertView2.findViewById(R.id.detailsIV);
            taxAMount.setText(taxListChild.get(i2).getEffectiveTaxes().get(0).getRate() + "%");
            convertView2.findViewById(R.id.editIcon).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddBillWithTaxExpandable.this.lambda$getChildView$2$AddBillWithTaxExpandable(i, v);
                }
            });
            TextView quantitytv = convertView2.findViewById(R.id.quantity);
            String str = childText;
            if (this.quantity.get(Integer.valueOf(this.chapterList.get(i).getId())) != null) {
                StringBuilder sb = new StringBuilder();
                ImageView imageView3 = addTaxIV;
                sb.append(this.quantity.get(Integer.valueOf(this.chapterList.get(i).getId())));
                sb.append("");
                quantitytv.setText(sb.toString());
            }
            deleteTax.setOnClickListener(view -> AddBillWithTaxExpandable.this.lambda$getChildView$3$AddBillWithTaxExpandable(i, i2, view));
            deleteTax.setVisibility(0);
        }
        return convertView2;
    }

    public void lambda$getChildView$1$AddBillWithTaxExpandable(int groupPosition, View v) {
        PurchaseItem product = this.chapterList.get(groupPosition);
        List<TaxResponse> taxResponse = taxList.get(Integer.valueOf(this.chapterList.get(groupPosition).getId()));
        product.setProductServiceTaxes(convert(taxResponse));
        ((CreateBillActivity) this._context).addTax(this.chapterList.get(groupPosition), groupPosition);
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
    public void lambda$getChildView$2$AddBillWithTaxExpandable(int groupPosition, View v) {
        ((CreateBillActivity) this._context).edit(this.chapterList.get(groupPosition));
    }

    public void lambda$getChildView$3$AddBillWithTaxExpandable(int groupPosition, int childPosition, View v) {
        ((CreateBillActivity) this._context).deleteTax(this.chapterList.get(groupPosition), childPosition);
    }

    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    public void notifyData(List<PurchaseItem> purchaseItemListSelected, Currency selectedExchangeCurrency2) {
        this.chapterList = purchaseItemListSelected;
        this.selectedExchangeCurrency = selectedExchangeCurrency2;
        notifyDataSetChanged();
    }

    public void notifyDataMAp(Map<Integer, List<TaxResponse>> taxList2, Map<Integer, Integer> quantityMap) {
        this.taxList = taxList2;
        this.quantity = quantityMap;
        notifyDataSetChanged();
    }
}
