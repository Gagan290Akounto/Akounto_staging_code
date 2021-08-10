package com.akounto.accountingsoftware.ui.base.model

import com.akounto.accountingsoftware.response.TransactionStatus
import com.google.gson.annotations.SerializedName

class BaseResponseModel {
    @SerializedName("Data")
    val data: Any? = null

    @SerializedName("TransactionStatus")
    val transactionStatus: TransactionStatus? = null

    var status = 0
    var statusMessage: String? = null
}

