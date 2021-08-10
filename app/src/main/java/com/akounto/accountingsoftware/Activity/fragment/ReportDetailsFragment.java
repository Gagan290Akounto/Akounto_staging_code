package com.akounto.accountingsoftware.Activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.akounto.accountingsoftware.R;
import com.akounto.accountingsoftware.response.viewreport.IncomeDetail;
import com.akounto.accountingsoftware.response.viewreport.ViewReportDetails;
import com.github.mikephil.charting.utils.Utils;
import java.util.List;

public class ReportDetailsFragment extends Fragment {
    TextView tv_gross_profit;
    TextView tv_gross_profit_value;
    TextView tv_net_profit;
    TextView tv_net_profit_value;
    TextView tv_total_cost;
    TextView tv_total_cost_value;
    TextView tv_total_expenses;
    TextView tv_total_expenses_value;
    TextView tv_total_income;
    TextView tv_total_income_value;
    View view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view2 = inflater.inflate(R.layout.report_summary_fragment, container, false);
        this.tv_total_income = view2.findViewById(R.id.tv_total_income);
        this.tv_total_income_value = view2.findViewById(R.id.tv_total_income_value);
        this.tv_total_cost = view2.findViewById(R.id.tv_total_cost);
        this.tv_total_cost_value = view2.findViewById(R.id.tv_total_cost_value);
        this.tv_gross_profit = view2.findViewById(R.id.tv_gross_profit);
        this.tv_gross_profit_value = view2.findViewById(R.id.tv_gross_profit_value);
        this.tv_total_expenses = view2.findViewById(R.id.tv_total_expenses);
        this.tv_total_expenses_value = view2.findViewById(R.id.tv_total_expenses_value);
        this.tv_net_profit = view2.findViewById(R.id.tv_net_profit);
        this.tv_net_profit_value = view2.findViewById(R.id.tv_net_profit_value);
        return view2;
    }

    public void setDataOnViews(ViewReportDetails response) {
        List<IncomeDetail> incomeDetailList = response.getData().getInvoiceProfitLossDateRange2().getIncomeDetails();
        if (incomeDetailList.size() > 0) {
            double totalCost = incomeDetailList.get(0).getProductPrice().doubleValue();
            double totalExpenses = Utils.DOUBLE_EPSILON;
            this.tv_total_cost_value.setText(String.valueOf(totalCost));
            if (incomeDetailList.size() == 1) {
                totalExpenses = incomeDetailList.get(1).getProductPrice().doubleValue();
                this.tv_total_expenses_value.setText(String.valueOf(totalExpenses));
            }
            this.tv_total_income_value.setText(String.valueOf(totalCost + totalExpenses));
        }
    }
}
