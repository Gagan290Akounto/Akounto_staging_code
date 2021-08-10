package com.akounto.accountingsoftware.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class JsonListData {

    @SerializedName("data")
    @Expose
    private List<JsonData> data = null;

    public List<JsonData> getData() {
        return data;
    }

    public void setData(List<JsonData> data) {
        this.data = data;
    }
}
