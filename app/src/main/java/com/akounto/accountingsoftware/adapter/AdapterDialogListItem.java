package com.akounto.accountingsoftware.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.Data.CountryData;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.databinding.MobileCodeItemBinding;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by root on 27-12-2016.
 */

public class AdapterDialogListItem extends RecyclerView.Adapter<AdapterDialogListItem.RecyclerViewHolders> {

    private List<CountryData> itemList;
    private IonItemSelect ionItemSelect;
    private int selectedPos = -1;
    Context mContext;
    public AdapterDialogListItem(Context mContext,List<CountryData> itemList, int selectedPos) {
        this.itemList = itemList;
        this.selectedPos = selectedPos;
        this.mContext=mContext;
    }

    public void registerOnItemClickListener(IonItemSelect ionItemSelect) {
        this.ionItemSelect = ionItemSelect;
    }

    public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

        public MobileCodeItemBinding binding;

        public RecyclerViewHolders(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (ionItemSelect != null)
                ionItemSelect.onItemSelect(getAdapterPosition());
        }
    }


    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mobile_code_item, null);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        holder.binding.country.setText(itemList.get(position).getName());
        holder.binding.countryCode.setText("+"+itemList.get(position).getPhoneCode());
        Picasso.with(mContext).load(Constant.IMG_URL+itemList.get(position).getCountryCode()+".png").into(holder.binding.imgFlag);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public interface IonItemSelect {
        void onItemSelect(int position);
    }

}
