package com.akounto.accountingsoftware.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class TaxSummaryListView extends ListView {
    public TaxSummaryListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TaxSummaryListView(Context context) {
        super(context);
    }

    public TaxSummaryListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(536870911, Integer.MIN_VALUE));
    }
}
