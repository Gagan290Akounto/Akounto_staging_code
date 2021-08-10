package com.akounto.accountingsoftware.response.chartaccount;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class GetChartData {

    @SerializedName("HeadTypes")
    private List<HeadType> HeadTypes;

    public List<HeadType> getHeadTypes() {
        return this.HeadTypes;
    }

    public void setHeadTypes(List<HeadType> headTypes) {
        this.HeadTypes = headTypes;
    }
}
