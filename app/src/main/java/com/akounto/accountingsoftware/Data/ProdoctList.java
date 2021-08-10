package com.akounto.accountingsoftware.Data;

import com.google.gson.annotations.SerializedName;
import com.akounto.accountingsoftware.response.Product;
import java.util.List;

public class ProdoctList{

    @SerializedName("Data")
    private List<Product> data;

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }
}
