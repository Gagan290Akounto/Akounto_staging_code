package com.akounto.accountingsoftware.util;

import android.content.Context;
import android.graphics.Typeface;

public class TypeFactory {
    private final String P_BLACK = "poppins_black.ttf";
    private final String P_BLACK_ITALIC = "poppins_blackitalic.ttf";
    private final String P_BOLD = "poppins_bold.ttf";
    private final String P_BOLDITELIC = "poppins_bolditalic.ttf";
    private final String P_EXTRABOLD = "poppins_extrabold.ttf";
    Typeface p_bold;
    Typeface poppins_black;
    Typeface poppins_blackitalic;
    Typeface poppins_bolditalic;
    Typeface poppins_extrabold;

    public TypeFactory(Context context) {
        this.poppins_black = Typeface.createFromAsset(context.getAssets(), this.P_BLACK);
        this.poppins_blackitalic = Typeface.createFromAsset(context.getAssets(), this.P_BLACK_ITALIC);
        this.p_bold = Typeface.createFromAsset(context.getAssets(), this.P_BOLD);
        this.poppins_bolditalic = Typeface.createFromAsset(context.getAssets(), this.P_BOLDITELIC);
        this.poppins_extrabold = Typeface.createFromAsset(context.getAssets(), this.P_EXTRABOLD);
    }
}
