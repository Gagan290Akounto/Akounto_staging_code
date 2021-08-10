package com.akounto.accountingsoftware.model;

import java.io.Serializable;

public class AddWithdrawalCategorySpinnerItem implements Serializable {

    /* renamed from: id */
    int f91id;
    AddWithDrawalCategory itemType;
    String title;

    public String getTitle() {
        return this.title;
    }

    public AddWithDrawalCategory getItemType() {
        return this.itemType;
    }

    public int getId() {
        return this.f91id;
    }

    public AddWithdrawalCategorySpinnerItem(String title2, AddWithDrawalCategory itemType2, int id) {
        this.title = title2;
        this.itemType = itemType2;
        this.f91id = id;
    }
}
