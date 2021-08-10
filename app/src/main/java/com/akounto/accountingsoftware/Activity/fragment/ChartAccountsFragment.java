package com.akounto.accountingsoftware.Activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.fragment.app.Fragment;

import com.akounto.accountingsoftware.Constants.Constant;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.Activity.Accounting.AccountingMenu;
import com.akounto.accountingsoftware.adapter.ChartExpandable;
import com.akounto.accountingsoftware.model.AddWithDrawalCategory;
import com.akounto.accountingsoftware.model.AddWithdrawalCategorySpinnerItem;
import com.akounto.accountingsoftware.network.CustomCallBack;
import com.akounto.accountingsoftware.network.RestClient;
import com.akounto.accountingsoftware.response.chartaccount.GetChartData;
import com.akounto.accountingsoftware.response.chartaccount.GetChartResponse;
import com.akounto.accountingsoftware.response.chartaccount.HeadSubType;
import com.akounto.accountingsoftware.response.chartaccount.HeadType;
import com.akounto.accountingsoftware.util.AddFragments;
import com.akounto.accountingsoftware.util.LocalManager;
import com.akounto.accountingsoftware.util.UiUtil;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;

public class ChartAccountsFragment extends Fragment {
    ExpandableListView chartExpandable;
    private final List<AddWithdrawalCategorySpinnerItem> list = new ArrayList();
    View view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.chart_accounts_fragment, container, false);
        this.view = inflate;
        view.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFragments.addFragmentToDrawerActivity(getActivity(), null, AccountingMenu.class);
            }
        });
        return inflate;
    }

    public void onResume() {
        super.onResume();
        getDataList();
    }

    private void getDataList() {
        RestClient.getInstance(getActivity()).getChart(Constant.X_SIGNATURE,"Bearer " +UiUtil.getAcccessToken(getContext()),UiUtil.getComp_Id(getContext()),"").enqueue(new CustomCallBack<GetChartResponse>(getActivity(), null) {
            public void onResponse(Call<GetChartResponse> call, Response<GetChartResponse> response) {
                super.onResponse(call, response);
                //Log.d("111111----2222", new Gson().toJson((Object) response.body().getData()));
                try{
                if (response.isSuccessful()) {
                    ChartAccountsFragment.this.prepareCategorySpinner(response.body().getData());
                }}catch(Exception e){
                    UiUtil.showToast(getContext(),e.getMessage());
                }
            }

            public void onFailure(Call<GetChartResponse> call, Throwable t) {
                super.onFailure(call, t);
            }
        });
    }

    /* access modifiers changed from: private */
    public void prepareCategorySpinner(GetChartData data) {
        for (HeadType outer : data.getHeadTypes()) {
            this.list.add(new AddWithdrawalCategorySpinnerItem(outer.getName(), AddWithDrawalCategory.HEADER, outer.getId()));
            for (HeadSubType headSubType : outer.getHeadSubTypes()) {
                this.list.add(new AddWithdrawalCategorySpinnerItem(headSubType.getName(), AddWithDrawalCategory.CATEGORY_ITEM, headSubType.getId()));
            }
        }
        LocalManager.getInstance().setCategoriesList(this.list);
        inItExpand(data.getHeadTypes());
    }

    private void inItExpand(List<HeadType> headTypes) {
        this.chartExpandable = this.view.findViewById(R.id.chartExpandable);
        this.chartExpandable.setAdapter(new ChartExpandable(getActivity(), headTypes));
        this.chartExpandable.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            public final boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long j) {
                return ChartAccountsFragment.this.lambda$inItExpand$0$ChartAccountsFragment(expandableListView, view, i, j);
            }
        });
    }

    public /* synthetic */ boolean lambda$inItExpand$0$ChartAccountsFragment(ExpandableListView parent, View v, int groupPosition, long id) {
        this.chartExpandable.smoothScrollToPosition(groupPosition);
        if (parent.isGroupExpanded(groupPosition)) {
            v.findViewById(R.id.expandable_icon).setBackground(getResources().getDrawable(R.drawable.down_arrow));
            return false;
        }
        v.findViewById(R.id.expandable_icon).setBackground(getResources().getDrawable(R.drawable.up_arrow));
        return false;
    }
}
