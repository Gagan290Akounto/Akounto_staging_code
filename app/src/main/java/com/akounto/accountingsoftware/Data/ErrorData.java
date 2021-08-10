package com.akounto.accountingsoftware.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ErrorData {

    @SerializedName("error")//An annotation that indicates this member should be serialized to JSON with the provided name value as its field name.
    @Expose//An annotation that indicates this member should be exposed for JSON serialization or deserialization.
    private String error;

    @SerializedName("error_description")
    @Expose
    private String error_description;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }
}
