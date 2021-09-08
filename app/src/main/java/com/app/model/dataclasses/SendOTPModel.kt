package com.app.model.dataclasses

import com.app.utils.AppConstants
import com.google.gson.annotations.SerializedName

data class SendOTPModel(

    @SerializedName(AppConstants.ERROR)
    var error: Boolean? = false,

    @SerializedName("responce")
    var responce: String? = null,

    @SerializedName("msgEn")
    var msgEn: String? = null,

    @SerializedName("msgAr")
    var msgAr: String? = null,

    )
