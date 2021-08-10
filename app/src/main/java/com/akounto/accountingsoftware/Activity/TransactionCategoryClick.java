package com.akounto.accountingsoftware.Activity;

import android.widget.EditText;
import android.widget.TextView;
import com.akounto.accountingsoftware.model.AddWithdrawalCategorySpinnerItem;

public interface TransactionCategoryClick {
    void onCategoryClick(AddWithdrawalCategorySpinnerItem addWithdrawalCategorySpinnerItem, TextView textView, EditText editText);
}
