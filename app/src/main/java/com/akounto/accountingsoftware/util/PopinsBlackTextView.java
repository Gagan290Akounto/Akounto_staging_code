package com.akounto.accountingsoftware.util;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;
import com.akounto.accountingsoftware.R;

public class PopinsBlackTextView extends AppCompatTextView {
    public PopinsBlackTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    public PopinsBlackTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PopinsBlackTextView(Context context) {
        super(context);
        init(null);
    }

    private void init(AttributeSet attrs) {
        setTypeface(ResourcesCompat.getFont(getContext(), R.font.poppins_black));
    }
}
