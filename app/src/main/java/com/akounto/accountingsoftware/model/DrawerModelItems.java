package com.akounto.accountingsoftware.model;

import java.util.ArrayList;

public class DrawerModelItems {
    public ArrayList<String> drawerItems = new ArrayList<>();
    public String image;
    public String title;

    public DrawerModelItems(String title2) {
        this.title = title2;
    }

    public String toString() {
        return this.title;
    }
}
