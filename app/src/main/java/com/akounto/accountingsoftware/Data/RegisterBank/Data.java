
package com.akounto.accountingsoftware.Data.RegisterBank;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Banks")
    @Expose
    private List<Bank> banks = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Bank> getBanks() {
        return banks;
    }

    public void setBanks(List<Bank> banks) {
        this.banks = banks;
    }

}
