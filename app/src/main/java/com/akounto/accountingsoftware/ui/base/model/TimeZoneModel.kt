package com.akounto.accountingsoftware.ui.base.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TimeZoneModel {
    @SerializedName("Name", alternate = ["name"])
    @Expose
    var name: String? = null
}